package com.kbph.logistics.api.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbph.logistics.api.config.ApiConfig;
import com.kbph.logistics.api.constant.Constants;
import com.kbph.logistics.api.domain.ApiCommonDTO;
import com.kbph.logistics.api.domain.CommonInfoDTO;
import com.kbph.logistics.api.domain.ConversionWasnifDTO;
import com.kbph.logistics.api.domain.ExcelWasnifInfo;
import com.kbph.logistics.api.domain.InbEntranceCarDTO;
import com.kbph.logistics.api.domain.InbEntranceDongDTO;
import com.kbph.logistics.api.domain.InboundOperationDTO;
import com.kbph.logistics.api.domain.InboundTaskDTO;
import com.kbph.logistics.api.domain.InspectionDTO;
import com.kbph.logistics.api.enums.ApiInfo;
import com.kbph.logistics.api.mapper.ApiInboundMapper;
import com.kbph.logistics.api.mapper.LookupForApiMapper;
import com.kbph.logistics.common.enums.Useyn;
import com.kbph.logistics.common.util.DateUtil;
import com.kbph.logistics.configuration.error.DeleteCheckedException;
import com.kbph.logistics.configuration.error.DuplicateLayerException;
import com.kbph.logistics.configuration.error.InsertCheckedException;
import com.kbph.logistics.configuration.error.RestApiException;
import com.kbph.logistics.configuration.error.TaskProcessingException;
import com.kbph.logistics.configuration.error.UpdateCheckedException;
import com.kbph.logistics.fcm.FcmService;
import com.kbph.logistics.im.domain.OstrhdDTO;
import com.kbph.logistics.im.domain.OstritDTO;
import com.kbph.logistics.im.domain.WasnhdDTO;
import com.kbph.logistics.im.domain.WasnitDTO;
import com.kbph.logistics.im.domain.WasnitSeacrhParamDTO;
import com.kbph.logistics.im.domain.WrcvhdDTO;
import com.kbph.logistics.im.domain.WrcvitDTO;
import com.kbph.logistics.im.domain.WrcvitSeacrhParamDTO;
import com.kbph.logistics.im.mapper.InboundMapper;
import com.kbph.logistics.md.domain.MvhcmaDTO;
import com.kbph.logistics.md.mapper.PartnerMapper;
import com.kbph.logistics.md.type.Adjdcst;
import com.kbph.logistics.md.type.Adjitst;
import com.kbph.logistics.md.type.Adjmode;
import com.kbph.logistics.md.type.Asnitst;
import com.kbph.logistics.md.type.Asnstat;
import com.kbph.logistics.md.type.DoccateInventory;
import com.kbph.logistics.md.type.DoctypeInventory;
import com.kbph.logistics.md.type.Rcvdcst;
import com.kbph.logistics.md.type.Rcvitst;
import com.kbph.logistics.md.type.Stritst;
import com.kbph.logistics.md.type.Strstat;
import com.kbph.logistics.md.type.Tasksts;
import com.kbph.logistics.sm.domain.WadjitDTO;
import com.kbph.logistics.sm.domain.WstkkyDTO;
import com.kbph.logistics.sm.domain.WtakitDTO;
import com.kbph.logistics.sm.mapper.InventoryMapper;
import com.kbph.logistics.sy.domain.UserVO;
import com.kbph.logistics.tm.domain.TmAppDTO;
import com.kbph.logistics.tm.domain.TmDispatchDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiInboundService {
	private final ApiInboundMapper apiInboundMapper;
	private final LookupForApiMapper lookupForApiMapper;
	private final InboundMapper inboundMapper;
	private final InventoryMapper inventoryMapper;
	private final FcmService fcmService;
	private final ApiConfig apiConfig; // API
	@Value("${spring.datasource.hikari.operation.common-schema}")
	private String commonSchema;
	@Value("${spring.datasource.hikari.operation.schema-prefix}${spring.datasource.hikari.operation.schema}")
	private String schema;
	private final PartnerMapper partnerMapper;

	public int conversionWasnifInfo(ApiCommonDTO<ExcelWasnifInfo> apiCommon) {
		UserVO userVO = new UserVO();
		userVO.setSchema(apiCommon.getSchema());
		userVO.setUseract(Constants.API_USERACT);
		userVO.setCommonSchema(commonSchema);
		MvhcmaDTO mvhcma = new MvhcmaDTO();
		mvhcma.setDmwplnm(apiCommon.getSchema().replace("_", "-").toLowerCase());
		mvhcma.setIfprsts(Useyn.UNUSE.getString());
		List<MvhcmaDTO> mvhcmaList = partnerMapper.selectMvhcmaList(mvhcma, userVO);
		if(!mvhcmaList.isEmpty()) {		//IFPRSTS='N'인 차량리스트가 있을때 API 보낸 후 해당 데이터의 IFPRSTS = 'Y'로 변경
			apiConfig.requestToDC(mvhcmaList, ApiInfo.API_MD_MVHCMA, HttpMethod.POST);
			int updateMvhcmaCnt = 0;
			for(MvhcmaDTO mvhcmaData : mvhcmaList) {
				mvhcmaData.setIfprsts(Useyn.USE.getString());
				updateMvhcmaCnt += partnerMapper.updateMvhcmaIfprsts(mvhcma, userVO);
			}

			if(updateMvhcmaCnt != mvhcmaList.size()) {
				throw new UpdateCheckedException();
			}
		}

		List<ConversionWasnifDTO> conversionList = apiInboundMapper.selectConversionInfo(apiCommon);
		int dataCnt = 0;
		if(conversionList.isEmpty()) {
			return dataCnt;
		}
		int count = 0;
		String adjsoky = inventoryMapper.selectAdjsoky(userVO);
		for (ConversionWasnifDTO conversionWasnif : conversionList) {
			conversionWasnif.setAdjsoky(adjsoky);
			conversionWasnif.setAdjsoit(++count);
			conversionWasnif.setAdjgrky(inventoryMapper.selectAdjgrky(userVO));
			conversionWasnif.setDoccate(DoccateInventory.STOCK.getCode());
			conversionWasnif.setDoctype(DoctypeInventory.CONVERSION.getCode());
			conversionWasnif.setAdjdcst(Adjdcst.CMP.getCode());
			conversionWasnif.setAdjitst(Adjitst.CMP.getCode());
			conversionWasnif.setAdjmode(Adjmode.BEFORE.getCode());
			conversionWasnif.setUseract(Constants.API_USERACT);
			int beforeInsertCnt = apiInboundMapper.insertConversionAdjustment(conversionWasnif, apiCommon.getSchema());
			conversionWasnif.setAdjsoit(++count);
			conversionWasnif.setAdjmode(Adjmode.AFTER.getCode());
			int afterInsertCnt = apiInboundMapper.insertConversionAdjustment(conversionWasnif, apiCommon.getSchema());
			if (beforeInsertCnt != afterInsertCnt) {
				throw new InsertCheckedException();
			}
			int updateCnt = apiInboundMapper.updateConversionInfo(conversionWasnif, apiCommon.getSchema());
			conversionWasnif.setIfprsts(updateCnt == 1 ? Constants.CONVERSION_FLAG : Constants.ERROR_FLAG);
			conversionWasnif.setIferrnm(updateCnt == 1 ? "" : Constants.CONVERSION_ERROR_MSG);
			dataCnt += apiInboundMapper.updateTempTableFlags(conversionWasnif, apiCommon.getSchema());
		}

		if(dataCnt != conversionList.size()) {
			throw new UpdateCheckedException();
		}

		WadjitDTO paramDTO = new WadjitDTO();
		paramDTO.setWarekey(conversionList.get(0).getWarekey());
		paramDTO.setAdjsoky(adjsoky);

		List<WadjitDTO> wadjitApiList = inventoryMapper.getWadjitApi(paramDTO, userVO);

		apiConfig.requestToDC(wadjitApiList, ApiInfo.API_SM_WADJIT,	HttpMethod.PATCH);

		return dataCnt;
	}

	// 입차정보
	public ApiCommonDTO<InbEntranceCarDTO> saveEntranceCar(ApiCommonDTO<InbEntranceCarDTO> apiCommon) {
		log.info("============ saveEntranceCar(입고) api start ============");
		if (apiCommon == null || CollectionUtils.isEmpty(apiCommon.getApiData())) {
			throw new RestApiException("ms.nodata");
		}
		apiCommon.setUseract(Constants.API_USERACT);
		int count = 0;

		//진행 된 입차정보 검증 - 0이면 통과
		for(InbEntranceCarDTO inbEntranceCarDTO : apiCommon.getApiData()){
			count = apiInboundMapper.checkEntranceCar(inbEntranceCarDTO, apiCommon);

			if(count != 0) {
				throw new RestApiException("ms.datanotfound");
			}
		}

		// 입차정보 + 자동 검수대상확정
		count = apiInboundMapper.updateEntranceCar(apiCommon);

		if (count != apiCommon.getApiData().size()) {
			throw new UpdateCheckedException();
		}

		log.info("============ saveEntranceDong api end ============");
		return apiCommon;
	}

	// 입동지시정보
	public ApiCommonDTO<InbEntranceDongDTO> saveEntranceDong(ApiCommonDTO<InbEntranceDongDTO> apiCommon) {
		log.info("============ saveEntranceDong(출고) api start ============");
		if (apiCommon == null || CollectionUtils.isEmpty(apiCommon.getApiData())) {
			throw new RestApiException("ms.nodata");
		}
		apiCommon.setUseract(Constants.API_USERACT);
		int count = 0;

		log.info("apiCommon => {}", apiCommon);
		for (InbEntranceDongDTO data : apiCommon.getApiData()) {
			TmDispatchDTO tmd = new TmDispatchDTO();
			tmd.setEoasnky(data.getEoasnky());
			tmd.setVhcfnam(data.getVhcfnam());
			CommonInfoDTO commonInfo = CommonInfoDTO.builder().commonSchema(commonSchema).schema(schema)
					.useract(Constants.API_USERACT).build();
			TmDispatchDTO vehicelInfo = lookupForApiMapper.selectVehicleInformationForApi(tmd, commonInfo);
			List<TmDispatchDTO> driverList = lookupForApiMapper.selectVehicleDriverMappingForApi(vehicelInfo,
					commonInfo);

			for (TmDispatchDTO driver : driverList) {
				TmAppDTO appNotice = new TmAppDTO();
				appNotice.setType(
						data.getEndflag() == 1 ? Constants.DRIVER_APP_NOTICE_TYPE : Constants.DRIVER_APP_CANCEL_TYPE);
				appNotice.setWarekey(vehicelInfo.getWarekey());
				appNotice.setAreakey(data.getAreakey());
				appNotice.setDestination(data.getTolocky());
				appNotice.setVownkey(driver.getVownkey());
				appNotice.setVehicky(vehicelInfo.getVehicky());
				appNotice.setSchema(apiCommon.getSchema());
				appNotice.setUseract(driver.getUseract());
				appNotice.setCreuser(Constants.API_USERACT);
				appNotice.setLmouser(Constants.API_USERACT);

				try {
					fcmService.fcmPushNotification(appNotice);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			//입동 일떄만
			if(data.getEndflag() == 1) {
				//입동지시여부 Y update
				count = apiInboundMapper.updateEntranceDong(data, apiCommon);

				if(count == 0) {
					throw new UpdateCheckedException();
				}
			}
		}
		log.info("============ saveEntranceDong api end ============");

		return apiCommon;
	}

	// 검수완료실적
	public ApiCommonDTO<InspectionDTO> saveInspectionCompl(ApiCommonDTO<InspectionDTO> apiCommon) {
		log.info("============ saveInspectionCompl api start ============");
		if (apiCommon == null || CollectionUtils.isEmpty(apiCommon.getApiData())) {
			throw new RestApiException("ms.nodata");
		}

		int count = 0;

		//이미 진행된 검수 검증 - 0이면 통과
		for(InspectionDTO inspectionDTO : apiCommon.getApiData()) {
			count = apiInboundMapper.checkItemInspection(inspectionDTO, apiCommon);

			if(count != 0) {
				throw new RestApiException("ms.datanotfound");
			}
		}

		apiCommon.setUseract(Constants.API_USERACT);

		// PASS -> ING로 변경
		count = apiInboundMapper.updateItemInspection(apiCommon);

		if (count == 0) {
			throw new UpdateCheckedException();
		}

		List<WasnitDTO> notCancelWasnitList = apiInboundMapper.selectApiWasnitList(apiCommon.getApiData().get(0),
				apiCommon);

		// 불합이면 오더헤더 아이템, 예정헤더 삭제
		if ("N".equals(apiCommon.getApiData().get(0).getAsnitst())) {
			List<WasnitDTO> cancelWasnitList = apiInboundMapper.selectApiWasnitCancelList(apiCommon.getApiData().get(0),
					apiCommon);

			for(WasnitDTO wasnitDTO : cancelWasnitList) {
				if(StringUtils.hasText(wasnitDTO.getCostrky())) {
					wasnitDTO.setStritst(Stritst.CANCEL.getCode());
					count = apiInboundMapper.updateOstritStat(wasnitDTO, apiCommon);

					if (count == 0) {
						throw new UpdateCheckedException();
					}
				}

				//제품 마스터 삭제
				count = apiInboundMapper.deleteInspectionFailMskuwc(wasnitDTO, apiCommon);

				if(count == 0) {
					throw new DeleteCheckedException();
				}
			}

			if(StringUtils.hasText(cancelWasnitList.get(0).getCostrky())) {
				// 입고오더 헤더 cancel
				cancelWasnitList.get(0).setStrstat(Strstat.CANCEL.getCode());
				count = apiInboundMapper.updateOstrhdStat(cancelWasnitList.get(0), apiCommon);

				if (count == 0) {
					throw new UpdateCheckedException();
				}
			}

			// 입고예정 헤더 cancel
			cancelWasnitList.get(0).setAsnstat(Asnstat.CANCEL.getCode());
			cancelWasnitList.get(0).setRsncode("DEFECT");
			count = apiInboundMapper.updateWasnhdStat(cancelWasnitList.get(0), apiCommon);
			if (count == 0) {
				throw new UpdateCheckedException();
			}

			// 입고예정 취소 API를 위한 set
			List<WasnitDTO> apiCancelWasnitList = new ArrayList<>();
			cancelWasnitList.get(0).setCncldat(DateUtil.getDateTime("yyyyMMdd HHmmss"));
			cancelWasnitList.get(0).setRsncdnm(Asnstat.CANCEL.getCode());
			apiCancelWasnitList.add(cancelWasnitList.get(0));

			apiConfig.requestToDC(apiCancelWasnitList, ApiInfo.API_IM_WASNIF_CANCEL, HttpMethod.PATCH);
		} else {
			// 입고 헤더 업데이트 (PASS -> ING)

			notCancelWasnitList.get(0).setAsnstat(Asnstat.PROCEED.getCode());
			count = apiInboundMapper.updateWasnhdStat(notCancelWasnitList.get(0), apiCommon);

			if (count == 0) {
				throw new UpdateCheckedException();
			}

			// 입고 지시 및 문서생성
			ObjectMapper om = new ObjectMapper(); // 변환 util
			UserVO userInfo = new UserVO();
			userInfo.setSchema(apiCommon.getSchema());
			userInfo.setUseract(apiCommon.getUseract());
			WasnhdDTO asnParam = new WasnhdDTO(); // asn head 조회 param
			asnParam.setEoasnky(notCancelWasnitList.get(0).getEoasnky()); // asn 번호 가져오기
			WasnhdDTO asnHead = inboundMapper.selectPlainWasnhd(asnParam, userInfo); // wasnhd 조회

			// 입고문서
			WrcvhdDTO rcvHead = om.convertValue(asnHead, WrcvhdDTO.class); // 조회한 asn을 기반으로 변환
			String rcvdcky = inboundMapper.selectRcvdcky(userInfo); // 입고문서번호 채번
			rcvHead.setRcvdcky(rcvdcky); // 채번한 문서번호 추가
			rcvHead.setRcvdcst(Rcvdcst.NEW.getCode()); // 입고문서 상태
			rcvHead.setVownkey(asnHead.getVownkey());
			rcvHead.setVehicky(asnHead.getVehicky());
			rcvHead.setVhcfnam(asnHead.getVhcfnam());
			rcvHead.setDrvernm(asnHead.getDrvernm());
			rcvHead.setRcvrscd(asnHead.getRsncode());
			rcvHead.setRcvrsnm(asnHead.getRsncdnm());

			count = inboundMapper.insertWrcvhd(rcvHead, userInfo); // 입고문서헤더 생성
			if(count == 0) {
				throw new InsertCheckedException();
			}

			List<WrcvitDTO> rcvItemList = new ArrayList<>(); // 입고문서 아이템 리스트
			for(int idx=0; idx<notCancelWasnitList.size(); idx++) { // 반영
				int itemKey = idx+1;
				WasnitDTO asn = notCancelWasnitList.get(idx);

				asn.setRcvdcky(rcvdcky); // 문서번호
				asn.setRcvdcit(itemKey); // 문서아이템번호
				WrcvitDTO tmpRcvit = om.convertValue(asn, WrcvitDTO.class); // 변환
				tmpRcvit.setRcvitst(Rcvitst.NEW.getCode()); // 문서 상태(신규)
				tmpRcvit.setRcvdcdt(DateUtil.getDate("yyyyMMdd")); // 입고문서 날짜
				tmpRcvit.setRcvdctm(DateUtil.getTime("HHmmss")); // 입고문서 시간
				tmpRcvit.setDoccate(asnHead.getDoccate()); // 문서유형
				tmpRcvit.setDoctype(asnHead.getDoctype()); // 문서타입
				tmpRcvit.setRcvrscd(asnHead.getRsncode()); // reason code
				tmpRcvit.setRcvrsnm(asnHead.getRsncdnm()); // reason
				tmpRcvit.setBtrcate(asn.getBtrcate());
				rcvItemList.add(tmpRcvit); // 대입
			}

			count = inboundMapper.insertWrcvitList(rcvItemList, userInfo); // 입고문서 아이템 생성
			if(count == 0) {
				throw new InsertCheckedException();
			}

			//하차 포인트별 지시
			List<WasnitDTO> asnSameUllockyList = inboundMapper.getWasnitUllockyGroup(asnHead, userInfo);
			List<WtakitDTO> taskItemList = new ArrayList<>(); // task 리스트
			Map<String, Integer> sameToLockyCnt = new HashMap<>();
			for(WasnitDTO asnSameUllockyData : asnSameUllockyList) {
				int idx = 0;
				String taskoky = inventoryMapper.selectTaskoky(userInfo);  // 문서번호 채번
				List<WasnitDTO> asnUllockyGroupList = inboundMapper.getWasnitUllockyGroupList(asnSameUllockyData, userInfo);

				for(WasnitDTO asnUllockyGroupData : asnUllockyGroupList) {
					WtakitDTO tmpTask = om.convertValue(asnUllockyGroupData, WtakitDTO.class);
					tmpTask.setTaskoky(taskoky); // 작업번호
					tmpTask.setTaskoit(++idx); // 작업아이템번호
					tmpTask.setTasksts(Tasksts.NEW.getCode()); // 작업상태(신규)
					tmpTask.setLotnmky(" "); // LOT번호(NOT NULL)
					tmpTask.setDoccate(asnHead.getDoccate()); // 문서유형
					tmpTask.setDoctype(asnHead.getDoctype()); // 문서타입
					tmpTask.setTaskdat(DateUtil.getDate("yyyyMMdd")); // 작업날짜 = 입고문서 생성 날짜로
					tmpTask.setFrareky(asnUllockyGroupData.getToareky()); // from 창고동
					tmpTask.setFrlocky(asnUllockyGroupData.getUllocky()); // from 베드(하차포인트)
					tmpTask.setFrlayer(asnUllockyGroupData.getStlayer()); // from layer(차량 단)
					tmpTask.setUllocky(asnUllockyGroupData.getUllocky());
					tmpTask.setParsncd("IN");
					tmpTask.setTcmpqty(0); // 완료개수(임시)
					tmpTask.setFordqty(1); // 지시 개수(임시)

					String tolocky = asnUllockyGroupData.getTolocky();

					if(sameToLockyCnt.containsKey(tolocky)) {
						sameToLockyCnt.put(tolocky, sameToLockyCnt.get(tolocky) + 1);
					} else {
						sameToLockyCnt.put(tolocky, 1);
					}

					tmpTask.setTolayer(sameToLockyCnt.get(tolocky));
					taskItemList.add(tmpTask);
				}
			}

			count = inboundMapper.insertWtakitList(taskItemList, userInfo); // task 생성
			if(count == 0) {
				throw new InsertCheckedException();
			}

			// API 호출
			for(WtakitDTO item : taskItemList) {
				item.setVehicky(asnHead.getVehicky());
			}
			apiConfig.requestToDC(taskItemList, ApiInfo.API_IM_WRCVIT, HttpMethod.POST);
		}

		log.info("============ saveInspectionCompl api end ============");

		return apiCommon;
	}

	// 입고조업시작
	public ApiCommonDTO<InboundOperationDTO> saveInboundStart(ApiCommonDTO<InboundOperationDTO> apiCommon) {
		log.info("============ saveInboundStart api start ============");
		if (apiCommon == null || CollectionUtils.isEmpty(apiCommon.getApiData())) {
			throw new RestApiException("ms.nodata");
		}

		//조업시작 벨리데이션
		for(InboundOperationDTO inboundOperationDTO : apiCommon.getApiData()) {
			int asnAndTaskAllList = apiInboundMapper.checkInboundStart(inboundOperationDTO, apiCommon);

			inboundOperationDTO.setTasksts(Tasksts.OPERATION_START.getCode());
			int operStrList = apiInboundMapper.checkInboundStart(inboundOperationDTO, apiCommon);

			inboundOperationDTO.setTasksts(Tasksts.TASK_COMPLETE.getCode());
			int taskCmpList = apiInboundMapper.checkInboundStart(inboundOperationDTO, apiCommon);

			inboundOperationDTO.setTasksts(Tasksts.COMPLETE.getCode());
			int operCmpList = apiInboundMapper.checkInboundStart(inboundOperationDTO, apiCommon);

			if(asnAndTaskAllList == operStrList) {
				throw new TaskProcessingException("ms.operation.startCompleted");
			}

			if(asnAndTaskAllList == taskCmpList) {
				throw new TaskProcessingException("ms.operation.taskCmp");
			}else if(taskCmpList > 0) {
				throw new TaskProcessingException("ms.operation.taskStart");
			}

			if(asnAndTaskAllList == operCmpList) {
				throw new TaskProcessingException("ms.operation.allCompleted");
			}
		}

		int count = 0;
		apiCommon.setUseract(Constants.API_USERACT);
		count = apiInboundMapper.updateInboundStart(apiCommon);

		if (count == 0) {
			throw new UpdateCheckedException();
		}

		// taskoky로 wrcvhd it 조회
		// ing 변환
		List<WrcvitDTO> selectRcvkeyAndItemKeyList = apiInboundMapper
				.selectApiTaskRcvkeyList(apiCommon.getApiData().get(0));
		UserVO userVO = new UserVO();
		userVO.setUseract(Constants.API_USERACT);
		userVO.setSchema(apiCommon.getSchema());
		count = apiInboundMapper.updateApiWrcvitStatus(selectRcvkeyAndItemKeyList, Rcvitst.PROCEED.getCode(), true,
				userVO);
		if (count == 0) {
			throw new UpdateCheckedException();
		}
		WrcvhdDTO wrcvhdDTO = new WrcvhdDTO();
		wrcvhdDTO.setRcvdcky(selectRcvkeyAndItemKeyList.get(0).getRcvdcky());
		count = apiInboundMapper.updateApiWrcvhdStatus(wrcvhdDTO, Rcvdcst.PROCEED.getCode(), userVO);
		if (count == 0) {
			throw new UpdateCheckedException();
		}
		log.info("============ saveInboundStart api end ============");
		return apiCommon;
	}

	// 입고작업완료
	public ApiCommonDTO<InboundTaskDTO> saveInboundTask(ApiCommonDTO<InboundTaskDTO> apiCommon) {
		log.info("============ saveInboundTask api start ============");
		if (apiCommon == null || CollectionUtils.isEmpty(apiCommon.getApiData())) {
			throw new RestApiException("ms.nodata");
		}

		//작업완료 벨리데이션
		for(InboundTaskDTO inboundTaskDTO : apiCommon.getApiData()) {
			int asnAndTaskAllList = apiInboundMapper.checkInboundStart(inboundTaskDTO, apiCommon);

			inboundTaskDTO.setTasksts(Tasksts.NEW.getCode());
			int newList = apiInboundMapper.checkInboundStart(inboundTaskDTO, apiCommon);

			inboundTaskDTO.setTasksts(Tasksts.TASK_COMPLETE.getCode());
			int taskCmpList = apiInboundMapper.checkInboundStart(inboundTaskDTO, apiCommon);

			inboundTaskDTO.setTasksts(Tasksts.COMPLETE.getCode());
			int operCmpList = apiInboundMapper.checkInboundStart(inboundTaskDTO, apiCommon);


			if(asnAndTaskAllList == newList) {
				throw new TaskProcessingException("ms.operation.startFirst");
			}

			if(asnAndTaskAllList == taskCmpList) {
				throw new TaskProcessingException("ms.operation.taskCmp");
			}

			if(asnAndTaskAllList == operCmpList) {
				throw new TaskProcessingException("ms.operation.allCompleted");
			}
		}


		int count = 0;
		apiCommon.setUseract(Constants.API_USERACT);

		// wasnit 아이템 조회 to창고동 to 베드 to단 교체 후 insert
		for (InboundTaskDTO inboundTaskDTO : apiCommon.getApiData()) {

			inboundTaskDTO.setSchema(apiCommon.getSchema());
			WasnitDTO inboundOneItem = apiInboundMapper.selectApiWasnitAndTaskData(inboundTaskDTO);

			inboundOneItem.setStockky(apiInboundMapper.getApiStockky(apiCommon));
			inboundOneItem.setLotnmky(apiInboundMapper.getApiLotnmky(apiCommon));

			inboundTaskDTO.setStockky(inboundOneItem.getStockky());
			inboundTaskDTO.setLotnmky(inboundOneItem.getLotnmky());

			inboundOneItem.setToareky(inboundTaskDTO.getToareky());
			inboundOneItem.setTolocky(inboundTaskDTO.getTolocky());
			inboundOneItem.setTolayer(inboundTaskDTO.getTolayer());
			inboundOneItem.setUseract(apiCommon.getUseract());

			// 재고 유무 확인
			WstkkyDTO hasSameLocStock = apiInboundMapper.isApiSameLocationAndLayer(inboundOneItem, apiCommon);

			if (hasSameLocStock != null) {
				throw new DuplicateLayerException(); // 재고존재
			}

			count = apiInboundMapper.insertApiWskky(inboundOneItem);

			if (count == 0) {
				throw new InsertCheckedException();
			}
		}
		// 작업완료 상태 변경
		count = apiInboundMapper.updateInboundTask(apiCommon);

		if (count == 0) {
			throw new UpdateCheckedException();
		}

		log.info("============ saveInboundStart api end ============");

		return apiCommon;
	}

	// 입고조업완료
	public ApiCommonDTO<InboundOperationDTO> saveInboundCompl(ApiCommonDTO<InboundOperationDTO> apiCommon) {
		log.info("============ saveInboundCompl api start ============");
		if (apiCommon == null || CollectionUtils.isEmpty(apiCommon.getApiData())) {
			throw new RestApiException("ms.nodata");
		}

		//조업완료 벨리데이션
		for(InboundOperationDTO inboundOperationDTO : apiCommon.getApiData()) {
			int asnAndTaskAllList = apiInboundMapper.checkInboundStart(inboundOperationDTO, apiCommon);

			inboundOperationDTO.setTasksts(Tasksts.NEW.getCode());
			int newList = apiInboundMapper.checkInboundStart(inboundOperationDTO, apiCommon);

			inboundOperationDTO.setTasksts(Tasksts.OPERATION_START.getCode());
			int operStrList = apiInboundMapper.checkInboundStart(inboundOperationDTO, apiCommon);

			inboundOperationDTO.setTasksts(Tasksts.TASK_COMPLETE.getCode());
			int taskCmpList = apiInboundMapper.checkInboundStart(inboundOperationDTO, apiCommon);

			inboundOperationDTO.setTasksts(Tasksts.COMPLETE.getCode());
			int operCmpList = apiInboundMapper.checkInboundStart(inboundOperationDTO, apiCommon);

			if(asnAndTaskAllList == newList) {
				throw new TaskProcessingException("ms.operation.startFirst");
			}

			if(asnAndTaskAllList == operStrList) {
				throw new TaskProcessingException("ms.operation.completeFirst");
			}

			if(asnAndTaskAllList == operCmpList) {
				throw new TaskProcessingException("ms.operation.allCompleted");
			}

			if(taskCmpList > 0 && asnAndTaskAllList != taskCmpList) {
				throw new TaskProcessingException("ms.operation.notAllCompleted");
			}
		}

		apiCommon.setUseract(Constants.API_USERACT);
		int count = 0;
		// 조업완료 상태값 X => OPERCMP
		count = apiInboundMapper.updateInboundComplete(apiCommon);

		if (count == 0) {
			throw new UpdateCheckedException();
		}

		//조업완료 후 재고 실적 update
		count = apiInboundMapper.updateInboundApiWstkky(apiCommon);
		if(count == 0) {
			throw new UpdateCheckedException();
		}

		int isAsnTaskAllOperCmp = apiInboundMapper.isApiAsnTaskAllOperCmp(apiCommon.getApiData().get(0), apiCommon);
		if(isAsnTaskAllOperCmp == 0) {
			WrcvitSeacrhParamDTO serachWrcvitItemDTO = WrcvitSeacrhParamDTO.builder()
					.eoasnky(apiCommon.getApiData().get(0).getEoasnky()).build();
			List<WrcvhdDTO> rcvHeadList = apiInboundMapper.selectApiWrcvitList(serachWrcvitItemDTO, apiCommon);

			UserVO userInfo = new UserVO();
			userInfo.setSchema(apiCommon.getSchema());
			userInfo.setUseract(apiCommon.getUseract());

			ObjectMapper om = new ObjectMapper();

			String rcvdcst = "";
			String asnstat = "";
			String strstat = "";

			for (WrcvhdDTO wrcvhd : rcvHeadList) {
				WrcvitSeacrhParamDTO wrcvitParam = WrcvitSeacrhParamDTO.builder().rcvdcky(wrcvhd.getRcvdcky())
						.warekey(wrcvhd.getWarekey()).build();
				List<WrcvitDTO> wrcvitList = inboundMapper.selectWrcvitStatusList(wrcvitParam, userInfo);

				List<WasnitDTO> tmpWasnitList = new ArrayList<>();
				List<OstritDTO> tmpOstritList = new ArrayList<>();
				for (WrcvitDTO wrcvit : wrcvitList) {
					int tmpTaskUpdateCnt = inventoryMapper.updateWtakitStatusToRcvdckyAndRcvdcit(
							om.convertValue(wrcvit, WtakitDTO.class), Tasksts.COMPLETE.getCode(), userInfo);
					wrcvit.setRchsqty(1);
					tmpWasnitList.add(om.convertValue(wrcvit, WasnitDTO.class));
					if (wrcvit.getCostrky() != null && wrcvit.getCostrit() != null) {
						OstritDTO ostrit = om.convertValue(wrcvit, OstritDTO.class);
						ostrit.setStrcqty(wrcvit.getRchsqty());
						tmpOstritList.add(ostrit);
					}
					if (tmpTaskUpdateCnt == 0) {
						throw new UpdateCheckedException();
					}
				}

				int tmpDocItemUpdateCnt = inboundMapper.updateWrcvitStatus(wrcvitList, Rcvitst.COMPLETE.getCode(), true,
						userInfo);
				if (tmpDocItemUpdateCnt == 0) {
					throw new UpdateCheckedException();
				}

				// wasnit 총 아이템 개수 === wrcvit cmp 개수
				// 맞으면 CMP 안맞으면 ING
				WasnitSeacrhParamDTO serachItemDTO = WasnitSeacrhParamDTO.builder()
						.eoasnky(apiCommon.getApiData().get(0).getEoasnky()).warekey(wrcvhd.getWarekey()).build();

				List<WasnitDTO> asnitstAllList = inboundMapper.selectWasnitKeyAndItemNotCancel(serachItemDTO, userInfo);

//				List<WrcvitDTO> wrcvitINGList = inboundMapper.selectWrcvitRVCMPList(wrcvitParam, userInfo);

				List<String> rcvitstList = new ArrayList<>();
				rcvitstList.add(Rcvitst.COMPLETE.getCode());
				wrcvitParam.setRcvitstList(rcvitstList);
				List<WrcvitDTO> wrcvitCMPList = inboundMapper.selectWrcvitRVCMPList(wrcvitParam, userInfo);

				if (asnitstAllList.size() == wrcvitCMPList.size()) {
					asnstat = Asnstat.COMPLETE.getCode();
					strstat = Strstat.COMPLETE.getCode();
					rcvdcst = Rcvdcst.COMPLETE.getCode();
				} else {
					asnstat = Asnstat.PROCEED.getCode();
					strstat = Strstat.PROCEED.getCode();
					rcvdcst = Rcvdcst.PROCEED.getCode();
				}

				int tmpDocHeaderUpdateCnt = inboundMapper.updateWrcvhdStatus(wrcvhd, rcvdcst, userInfo);
				if (tmpDocHeaderUpdateCnt == 0) {
					throw new UpdateCheckedException();
				}

				int tmpAsnItemUpdateCnt = inboundMapper.updateWasnitStatus(tmpWasnitList, Asnitst.COMPLETE.getCode(), true,
						userInfo);
				if (tmpAsnItemUpdateCnt == 0) {
					throw new UpdateCheckedException();
				}

				int tmpAsnHeaderUpdateCnt = inboundMapper
						.updateWasnhdStatus(om.convertValue(tmpWasnitList.get(0), WasnhdDTO.class), asnstat, userInfo);
				if (tmpAsnHeaderUpdateCnt == 0) {
					throw new UpdateCheckedException();
				}

				if (!tmpOstritList.isEmpty()) {

					int tmpOrderItemUpdateCnt = inboundMapper.updateOstritStatus(tmpOstritList, Stritst.COMPLETE.getCode(),
							true, userInfo);
					if (tmpOrderItemUpdateCnt == 0) {
						throw new UpdateCheckedException();
					}

					int tmpOrderHeaderUpdateCnt = inboundMapper
							.updateOstrhdStatus(om.convertValue(tmpOstritList.get(0), OstrhdDTO.class), strstat, userInfo);
					if (tmpOrderHeaderUpdateCnt == 0) {
						throw new UpdateCheckedException();
					}
				}
			}
			// 입고완료확정 do -> dc api
			if (Asnstat.COMPLETE.getCode().equals(asnstat)) {
				apiConfig.requestToDC(apiCommon.getApiData(), ApiInfo.API_IM_WRCVIT_CMP, HttpMethod.POST);
			}
			log.info("============ saveInboundCompl api end ============");
		}

		return apiCommon;
	}
}
