package com.kbph.logistics.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbph.logistics.api.config.ApiConfig;
import com.kbph.logistics.api.constant.Constants;
import com.kbph.logistics.api.domain.ApiCommonDTO;
import com.kbph.logistics.api.domain.CommonInfoDTO;
import com.kbph.logistics.api.domain.OubEntranceCarDTO;
import com.kbph.logistics.api.domain.OubEntranceDongDTO;
import com.kbph.logistics.api.domain.OutboundOperationDTO;
import com.kbph.logistics.api.domain.OutboundTaskDTO;
import com.kbph.logistics.api.enums.ApiInfo;
import com.kbph.logistics.api.mapper.ApiOutboundMapper;
import com.kbph.logistics.api.mapper.LookupForApiMapper;
import com.kbph.logistics.common.enums.Useyn;
import com.kbph.logistics.configuration.error.DeleteCheckedException;
import com.kbph.logistics.configuration.error.NoSaveDataException;
import com.kbph.logistics.configuration.error.RestApiException;
import com.kbph.logistics.configuration.error.TaskProcessingException;
import com.kbph.logistics.configuration.error.UpdateCheckedException;
import com.kbph.logistics.fcm.FcmService;
import com.kbph.logistics.md.type.Oboitst;
import com.kbph.logistics.md.type.Obostat;
import com.kbph.logistics.md.type.Shpdcst;
import com.kbph.logistics.md.type.Shpitst;
import com.kbph.logistics.md.type.Tasksts;
import com.kbph.logistics.md.type.Tpistat;
import com.kbph.logistics.md.type.Tpnstat;
import com.kbph.logistics.md.type.Wplitst;
import com.kbph.logistics.md.type.Wplstat;
import com.kbph.logistics.om.domain.OoubhdDTO;
import com.kbph.logistics.om.domain.OoubitDTO;
import com.kbph.logistics.om.domain.WshphdDTO;
import com.kbph.logistics.om.domain.WshpitDTO;
import com.kbph.logistics.sm.domain.WstkkyDTO;
import com.kbph.logistics.sm.domain.WtakitDTO;
import com.kbph.logistics.sse.util.SseEmitters;
import com.kbph.logistics.tm.domain.TmAppDTO;
import com.kbph.logistics.tm.domain.TmDispatchDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiOutboundService {
	private final ApiOutboundMapper apiOutboundMapper;
	private final LookupForApiMapper lookupForApiMapper;
	private final FcmService fcmService;
	private final ApiConfig apiConfig;
	private final SseEmitters sseEmitters;
	@Value("${spring.datasource.hikari.operation.common-schema}")
	private String commonSchema;

	// 입차정보
	public ApiCommonDTO<OubEntranceCarDTO> saveEntranceCar(ApiCommonDTO<OubEntranceCarDTO> apiCommon){
		log.info("============ saveEntranceCar(출고) api start ============");
		if(apiCommon == null || CollectionUtils.isEmpty(apiCommon.getApiData())) {
			throw new RestApiException("ms.nodata");
		}
		apiCommon.setUseract(Constants.API_USERACT);
		apiCommon.setCommonSchema(commonSchema);
		CommonInfoDTO commonInfo = CommonInfoDTO.builder().commonSchema(commonSchema).schema(apiCommon.getSchema()).useract(Constants.API_USERACT).build();

		for(OubEntranceCarDTO oubCar : apiCommon.getApiData()) {
			TmDispatchDTO tmParam = new TmDispatchDTO();
			tmParam.setVhcfnam(oubCar.getVhcfnam());
			tmParam.setOutboky(oubCar.getOutboky());
			TmDispatchDTO vehicelInfo = lookupForApiMapper.selectVehicleInformationForApi(tmParam, commonInfo);

			oubCar.setVehicky(vehicelInfo.getVehicky());
		}
		apiOutboundMapper.updateEntranceCar(apiCommon); // 입차정보

		log.info("============ saveEntranceCar(출고) api end ============");
		return apiCommon;
	}

	// 입동지시정보
	public ApiCommonDTO<OubEntranceDongDTO> saveEntranceDong(ApiCommonDTO<OubEntranceDongDTO> apiCommon){
		log.info("============ saveEntranceDong(출고) api start ============");
		if(apiCommon == null || CollectionUtils.isEmpty(apiCommon.getApiData())) {
			throw new RestApiException("ms.nodata");
		}
		apiCommon.setUseract(Constants.API_USERACT);

		log.info("apiCommon => {}", apiCommon);

		for(OubEntranceDongDTO data : apiCommon.getApiData()) {
			TmDispatchDTO tmd = new TmDispatchDTO();
			tmd.setOutboky(data.getOutboky());
			tmd.setVhcfnam(data.getVhcfnam());
			CommonInfoDTO commonInfo = CommonInfoDTO.builder().commonSchema(commonSchema).schema(apiCommon.getSchema()).useract(Constants.API_USERACT).build();
			TmDispatchDTO vehicelInfo = lookupForApiMapper.selectVehicleInformationForApi(tmd, commonInfo);
			List<TmDispatchDTO> driverList = lookupForApiMapper.selectVehicleDriverMappingForApi(vehicelInfo, commonInfo);

			for(TmDispatchDTO driver : driverList) {
				TmAppDTO appNotice = new TmAppDTO();
				appNotice.setType(data.getEndflag() == 1 ? Constants.DRIVER_APP_NOTICE_TYPE : Constants.DRIVER_APP_CANCEL_TYPE);
				appNotice.setWarekey(vehicelInfo.getWarekey());
				appNotice.setAreakey(data.getAreakey());
				appNotice.setDestination(data.getTolocky());
				appNotice.setVownkey(driver.getVownkey());
				appNotice.setUseract(driver.getUseract());
				appNotice.setSchema(apiCommon.getSchema());
				appNotice.setVehicky(vehicelInfo.getVehicky());
				appNotice.setOutboky(data.getOutboky());
				appNotice.setCreuser(Constants.API_USERACT);
				appNotice.setLmouser(Constants.API_USERACT);

				try {
					fcmService.fcmPushNotification(appNotice);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			//입동 일때만
			if(data.getEndflag() == 1) {

				OoubhdDTO ooubhdParam = new OoubhdDTO();
				ooubhdParam.setOutboky(data.getOutboky());
				OoubhdDTO ooubhd = lookupForApiMapper.selectOutboundOrderHeaderListForApi(ooubhdParam, commonInfo);
				if(ooubhd == null) {
					throw new RestApiException("ms.nodata");
				}
				ooubhd.setEntdoyn(Useyn.USE.getString());
				if(apiOutboundMapper.updateOoubhdEntdoyn(ooubhd, commonInfo) == 0) {
					throw new UpdateCheckedException("출고오더 상태변경오류");
				}

				sseEmitters.sendEvent("alert", data.getVhcfnam() + " / 입동위치 : " + data.getAreakey());
			}
		}

//		// 이하 자동화 로직
		// 공오더 이슈로 자동화 주석처리.
//		List<OoubhdDTO> ooubhdListForApi = new ArrayList<>();
//		for(OubEntranceCarDTO data : apiCommon.getApiData()) {
//			String doctype = "";
//			Map<String, List<OoubitDTO>> sameShlockyList = new HashMap<>();
//
//			OoubhdDTO ooubhdParam = new OoubhdDTO();
//			ooubhdParam.setOutboky(data.getOutboky());
//			OoubhdDTO ooubhd = lookupForApiMapper.selectOutboundOrderHeaderListForApi(ooubhdParam, commonInfo);
//			ooubhdListForApi.add(ooubhd);
//			if(ooubhd == null) {
//				throw new RestApiException("ms.nodata");
//			}
//
//			List<OoubitDTO> ooubitList = lookupForApiMapper.selectOutboundOrderItemListForApi(ooubhd, commonInfo);
//
//			if(CollectionUtils.isEmpty(ooubitList)) {
//				throw new RestApiException("ms.nodata");
//			}
//
//			//wshphd, it insert (목적지별)
//			Map<String, List<OoubitDTO>> sameRegikeyList = ooubitList.stream().collect(Collectors.groupingBy(OoubitDTO :: getRegimod));
//
//			for(Map.Entry<String, List<OoubitDTO>> sameRegikey : sameRegikeyList.entrySet()) {
//				String shpdcky = lookupForApiMapper.selectShpdckyForApi(commonInfo);
//
////				if("".equals(doctype) || !(DoctypeOutboundOrder.GENERAL_SHIPMENT.getCode().equals(doctype) || DoctypeOutboundOrder.URGENT_RESEND.getCode().equals(doctype) || DoctypeOutboundOrder.SCHEDULED_SHIPMENT.getCode().equals(doctype) || DoctypeOutboundOrder.OTHER_SHIPMENT.getCode().equals(doctype))) {
//					doctype = ooubhd.getDoctype();
////				}
//				ooubhd.setShpdcky(shpdcky);
//				ooubhd.setShpdcst(Shpdcst.NEW.getCode());
//				ooubhd.setDoccate(Doccate.OUTBOUND.getCode());
//				ooubhd.setRegikey(sameRegikey.getKey());
//				ooubhd.setDoctype(DoctypeOutbound.valueOf(DoctypeOutboundOrder.getDoctypeToCode(doctype).name()).getCode());
//
//				if(apiOutboundMapper.insertWshphdForApi(ooubhd, commonInfo) == 0) {
//					throw new InsertCheckedException();
//				}
//
//				for(OoubitDTO ooubitDTO : sameRegikey.getValue()) {
//					//wshpit insert
//					ooubitDTO.setShpdcky(shpdcky);
//					ooubitDTO.setShpitst(Shpitst.NEW.getCode());
//					ooubitDTO.setDoccate(Doccate.OUTBOUND.getCode());
//					ooubitDTO.setDoctype(DoctypeOutbound.valueOf(DoctypeOutboundOrder.getDoctypeToCode(doctype).name()).getCode());
//					ooubitDTO.setShpdcit(lookupForApiMapper.selectShpdcitForApi(shpdcky, commonInfo));
//					if(apiOutboundMapper.insertWshpitForApi(ooubitDTO, commonInfo) == 0) {
//						throw new InsertCheckedException();
//					}
//				}
//			}
//
//			Map<String, List<OoubitDTO>> sameShpLoc = ooubitList.stream().collect(Collectors.groupingBy(OoubitDTO :: getShlocky));
//
//			sameShpLoc.forEach((key, value) ->
//			sameShlockyList.merge(key, value, (existingList, newList) -> {
//					existingList.addAll(newList);
//
//					return existingList;
//				})
//			);
//
//			for(Map.Entry<String, List<OoubitDTO>> sameShlocky : sameShlockyList.entrySet()) {
//				String taskoky = lookupForApiMapper.selectTaskokyForApi(commonInfo);
//
//				for(OoubitDTO ooubitDTO : sameShlocky.getValue()) {
//					WstkkyDTO stockInfo = lookupForApiMapper.hasStockForApi(ooubitDTO, commonInfo);
//
//					if(stockInfo == null) {
//						throw new RestApiException("ms.nodata");
//					}
//
//					ooubitDTO.setTaskoky(taskoky);
//					ooubitDTO.setTaskoit(lookupForApiMapper.selectTaskoitForApi(taskoky, commonInfo));
//					ooubitDTO.setStockky(stockInfo.getStockky());
//					ooubitDTO.setLotnmky(stockInfo.getLotnmky());
//					ooubitDTO.setDoccate(Doccate.OUTBOUND.getCode());
//					ooubitDTO.setDoctype(DoctypeOutbound.valueOf(DoctypeOutboundOrder.getDoctypeToCode(doctype).name()).getCode());
//					ooubitDTO.setTasksts(Tasksts.NEW.getCode());
//
//					if(apiOutboundMapper.insertWtakitForApi(ooubitDTO, commonInfo) == 0) {
//						throw new InsertCheckedException();
//					}
//
//					ooubitDTO.setOboitst(Oboitst.ING.getCode()); //ooubit 진행중으로 상태값 변경
//
//					if(apiOutboundMapper.updateOboitstForApi(ooubitDTO, commonInfo) == 0) {
//						throw new UpdateCheckedException();
//					}
//				}
//			}
//			//진행중으로
//			ooubhd.setObostat(Obostat.ING.getCode());
//			if(apiOutboundMapper.updateObostatForApi(ooubhd, commonInfo) == 0) {
//				throw new UpdateCheckedException();
//			}
//
//			//DO -> DC API(출고선별지시)
//			apiConfig.requestToDC(ooubhdListForApi, ApiInfo.API_OM_WTAKIT, HttpMethod.POST);
//		}
		log.info("============ saveEntranceDong api end ============");

		return apiCommon;
	}

	// 출고조업시작
	public ApiCommonDTO<OutboundOperationDTO> saveOutboundStart(ApiCommonDTO<OutboundOperationDTO> apiCommon){
		log.info("============ saveOutboundStart api start ============");
		if(apiCommon == null || CollectionUtils.isEmpty(apiCommon.getApiData())) {
			throw new RestApiException("ms.nodata");
		}

		//조업시작 벨리데이션
		for(OutboundOperationDTO outboundOperationDTO : apiCommon.getApiData()) {
			int outAndTaskAllList = apiOutboundMapper.checkOutboundStart(outboundOperationDTO, apiCommon);

			outboundOperationDTO.setTasksts(Tasksts.OPERATION_START.getCode());
			int operStrList = apiOutboundMapper.checkOutboundStart(outboundOperationDTO, apiCommon);

			outboundOperationDTO.setTasksts(Tasksts.TASK_COMPLETE.getCode());
			int taskCmpList = apiOutboundMapper.checkOutboundStart(outboundOperationDTO, apiCommon);

			outboundOperationDTO.setTasksts(Tasksts.OPERATION_COMPLETE.getCode());
			int operCmpList = apiOutboundMapper.checkOutboundStart(outboundOperationDTO, apiCommon);

			if(outAndTaskAllList == operStrList) {
				throw new TaskProcessingException("ms.operation.startCompleted");
			}

			if(outAndTaskAllList == taskCmpList) {
				throw new TaskProcessingException("ms.operation.taskCmp");
			}else if(taskCmpList > 0) {
				throw new TaskProcessingException("ms.operation.taskStart");
			}

			if(outAndTaskAllList == operCmpList) {
				throw new TaskProcessingException("ms.operation.allCompleted");
			}
		}

		int count = 0;
		apiCommon.setUseract(Constants.API_USERACT);
		count = apiOutboundMapper.updateOutboundStart(apiCommon);

		if(count == 0) {
			throw new UpdateCheckedException("작업문서 상태변경 오류");
		}

		for(OutboundOperationDTO outboundOperationDTO : apiCommon.getApiData()) {
			List<OoubitDTO> shpdckyAndItList = apiOutboundMapper.getApiShpdckyAndIt(outboundOperationDTO, apiCommon);

			for(OoubitDTO shpdckyAndItData : shpdckyAndItList) {
				shpdckyAndItData.setShpitst(Shpitst.ING.getCode());
				count = apiOutboundMapper.updateApiWshpitStat(shpdckyAndItData, apiCommon);
				if(count == 0) {
					throw new UpdateCheckedException("출고문서 아이템 상태변경 오류");
				}

				int wshpitAllListCount = apiOutboundMapper.getApiWshpitStatCount(shpdckyAndItData, apiCommon);

				shpdckyAndItData.setShpitst(Shpitst.ING.getCode());

				int wshpitIngListCount = apiOutboundMapper.getApiWshpitStatCount(shpdckyAndItData, apiCommon);

				if(wshpitAllListCount == wshpitIngListCount) {
					shpdckyAndItData.setShpdcst(Shpdcst.ING.getCode());
					count = apiOutboundMapper.updateApiWshphdStat(shpdckyAndItData, apiCommon);

					if(count == 0) {
						throw new UpdateCheckedException("출고문서 상태변경 오류");
					}
				}
			}
		}

		log.info("============ saveOutboundStart api end ============");

		return apiCommon;
	}

	// 출고작업리스트갱신
	public ApiCommonDTO<OutboundTaskDTO> updateOutboundTask(ApiCommonDTO<OutboundTaskDTO> apiCommon){
		log.info("============ updateOutboundTask api start ============");
		int count = 0;
		List<OutboundTaskDTO> apiList = new ArrayList<>();
		for(OutboundTaskDTO outboundTaskDTO : apiCommon.getApiData()) {
			// 제품 위치 가져오기
			List<OutboundTaskDTO> getStockInfo = apiOutboundMapper.getApiOutStockInfo(outboundTaskDTO, apiCommon);

			if(CollectionUtils.isEmpty(getStockInfo)) {
				throw new NoSaveDataException();
			}

			for(OutboundTaskDTO outboundStockInfo : getStockInfo) {
				// 단수 update
				// 작업테이블
				count = apiOutboundMapper.updateAPiOutTask(outboundStockInfo, apiCommon);

				if(count == 0){
					throw new UpdateCheckedException();
				}
				// 출고오더테이블
				count = apiOutboundMapper.updateAPiOutboundOrder(outboundStockInfo, apiCommon);

				if(count == 0){
					throw new UpdateCheckedException();
				}
				// 출고문서테이블
				count = apiOutboundMapper.updateAPiOutboundShip(outboundStockInfo, apiCommon);

				if(count == 0){
					throw new UpdateCheckedException();
				}
			}
			apiList.addAll(apiOutboundMapper.getApiOutTaskInfo(outboundTaskDTO, apiCommon));
		}
		// 작업문서 api 전송(patch)
		apiConfig.requestToDC(apiList, ApiInfo.API_OM_WTAKIT, HttpMethod.PATCH);

		log.info("============ updateOutboundTask api end ============");
		return apiCommon;
	}

	// 출고작업완료
	public ApiCommonDTO<OutboundTaskDTO> saveOutboundTask(ApiCommonDTO<OutboundTaskDTO> apiCommon){
		log.info("============ saveOutboundTask api start ============");
		if(apiCommon == null || CollectionUtils.isEmpty(apiCommon.getApiData())) {
			throw new RestApiException("ms.nodata");
		}
		int count = 0;
		apiCommon.setUseract(Constants.API_USERACT);
		CommonInfoDTO commonInfo = CommonInfoDTO.builder()
																				.commonSchema(commonSchema)
																				.schema(apiCommon.getSchema())
																				.useract(Constants.API_USERACT)
																				.build();

		for(OutboundTaskDTO outboundTaskDTO : apiCommon.getApiData()) {
			int outAndTaskAllList = apiOutboundMapper.checkOutboundStart(outboundTaskDTO, apiCommon);

			outboundTaskDTO.setTasksts(Tasksts.NEW.getCode());
			int newList = apiOutboundMapper.checkOutboundStart(outboundTaskDTO, apiCommon);

			outboundTaskDTO.setTasksts(Tasksts.TASK_COMPLETE.getCode());
			int taskCmpList = apiOutboundMapper.checkOutboundStart(outboundTaskDTO, apiCommon);

			outboundTaskDTO.setTasksts(Tasksts.OPERATION_COMPLETE.getCode());
			int operCmpList = apiOutboundMapper.checkOutboundStart(outboundTaskDTO, apiCommon);


			if(outAndTaskAllList == newList) {
				throw new TaskProcessingException("ms.operation.startFirst");
			}

			if(outAndTaskAllList == taskCmpList) {
				throw new TaskProcessingException("ms.operation.taskCmp");
			}

			if(outAndTaskAllList == operCmpList) {
				throw new TaskProcessingException("ms.operation.allCompleted");
			}

			WstkkyDTO stockParam = new WstkkyDTO();
			stockParam.setTaskoky(outboundTaskDTO.getTaskoky());
			stockParam.setSkumkey(outboundTaskDTO.getSkumkey());
			stockParam.setFrlayer(outboundTaskDTO.getFrlayer());
			stockParam.setTolayer(outboundTaskDTO.getTolayer());

			WtakitDTO taskParam = new WtakitDTO();
			taskParam.setOutboky(outboundTaskDTO.getOutboky());
			taskParam.setTaskoky(outboundTaskDTO.getTaskoky());
			taskParam.setSkumkey(outboundTaskDTO.getSkumkey());
			taskParam.setFrlayer(outboundTaskDTO.getFrlayer());
			taskParam.setTolayer(outboundTaskDTO.getTolayer());

			WstkkyDTO getStockInfo = lookupForApiMapper.hasStockForApi(stockParam, commonInfo);

			if(getStockInfo == null) {
				throw new NoSaveDataException("해당 재고가 없습니다.");
			}

			// 재고삭제
			if(apiOutboundMapper.deleteStockForApi(taskParam, commonInfo) == 0) {
				throw new DeleteCheckedException("재고 삭제 오류");
			}

			// 출고시간
			if(apiOutboundMapper.updateWshpitShpDateForApi(taskParam, commonInfo) == 0) {
				throw new UpdateCheckedException("출고시간 업데이트 오류");
			}

			//wtakit 상태값 update
			taskParam.setTasksts(Tasksts.TASK_COMPLETE.getCode());
			if(apiOutboundMapper.updateWtakitTaskstsForApi(taskParam, commonInfo) == 0) {
				throw new UpdateCheckedException("작업문서 상태변경 오류");
			}
		}
		log.info("============ saveOutboundTask api end ============");
		return apiCommon;
	}

	// 출고조업완료
	public ApiCommonDTO<OutboundOperationDTO> saveOutboundCompl(ApiCommonDTO<OutboundOperationDTO> apiCommon){
		log.info("============ saveOutboundCompl api start ============");
		if(apiCommon == null || CollectionUtils.isEmpty(apiCommon.getApiData())) {
			throw new RestApiException("ms.nodata");
		}

		int count = 0;
		apiCommon.setUseract(Constants.API_USERACT);
		List<WstkkyDTO> fromLocationStock = new ArrayList<>();
		ObjectMapper om = new ObjectMapper(); // 변환 util

		CommonInfoDTO commonInfo = CommonInfoDTO.builder().commonSchema(commonSchema).schema(apiCommon.getSchema()).useract(Constants.API_USERACT).build();

		for(OutboundOperationDTO outboundOperationDTO : apiCommon.getApiData()) {
			int outAndTaskAllList = apiOutboundMapper.checkOutboundStart(outboundOperationDTO, apiCommon);

			outboundOperationDTO.setTasksts(Tasksts.OPERATION_START.getCode());
			int operStrList = apiOutboundMapper.checkOutboundStart(outboundOperationDTO, apiCommon);

			outboundOperationDTO.setTasksts(Tasksts.NEW.getCode());
			int newList = apiOutboundMapper.checkOutboundStart(outboundOperationDTO, apiCommon);

			outboundOperationDTO.setTasksts(Tasksts.TASK_COMPLETE.getCode());
			int taskCmpList = apiOutboundMapper.checkOutboundStart(outboundOperationDTO, apiCommon);

			outboundOperationDTO.setTasksts(Tasksts.OPERATION_COMPLETE.getCode());
			int operCmpList = apiOutboundMapper.checkOutboundStart(outboundOperationDTO, apiCommon);


			if(outAndTaskAllList == newList) {
				throw new TaskProcessingException("ms.operation.startFirst");
			}

			if(outAndTaskAllList == operStrList) {
				throw new TaskProcessingException("ms.operation.completeFirst");
			}

			if(outAndTaskAllList == operCmpList) {
				throw new TaskProcessingException("ms.operation.allCompleted");
			}

			if(taskCmpList > 0 && outAndTaskAllList != taskCmpList) {
				throw new TaskProcessingException("ms.operation.notAllCompleted");
			}
		}
		count = apiOutboundMapper.updateOutboundComplete(apiCommon);

		if(count == 0) {
			throw new UpdateCheckedException();
		}

		for(OutboundOperationDTO outboundOperationDTO : apiCommon.getApiData()) {
			WtakitDTO wtakitDTO = new WtakitDTO();
			OoubhdDTO ooubhdDTO = new OoubhdDTO();
			WshphdDTO wshphdDTO = new WshphdDTO();
			//ooubhd, it 상태 완료
			wtakitDTO.setOutboky(outboundOperationDTO.getOutboky());
			wtakitDTO.setSkumkey(outboundOperationDTO.getSkumkey());
			wtakitDTO.setTolayer(outboundOperationDTO.getTolayer());
			wtakitDTO.setFrlayer(outboundOperationDTO.getFrlayer());
			wtakitDTO.setTaskoky(outboundOperationDTO.getTaskoky());
			wtakitDTO.setOboitst(Oboitst.CMP.getCode());
			if(apiOutboundMapper.updateOboitstCMPForApi(wtakitDTO, commonInfo) == 0) {
				throw new UpdateCheckedException("출고오더 아이템 상태변경 오류");
			}

			ooubhdDTO.setOutboky(outboundOperationDTO.getOutboky());
			List<OoubitDTO> ooubitValidationList = apiOutboundMapper.selectOoubitListForApiValidation(ooubhdDTO, apiCommon);
			int ooubitCmpCnt = 0;
			for(OoubitDTO ooubitCmp : ooubitValidationList) {
				if(Oboitst.CMP.getCode().equals(ooubitCmp.getOboitst())) {
					ooubitCmpCnt++;

					ooubitCmp.setAreakey(ooubitCmp.getFrareky());
					ooubitCmp.setLocakey(ooubitCmp.getFrlocky());
					if (fromLocationStock.stream().noneMatch(dto ->
								dto.getAreakey().equals(ooubitCmp.getAreakey()) &&
								dto.getLocakey().equals(ooubitCmp.getLocakey()))) {
						fromLocationStock.add(om.convertValue(ooubitCmp, WstkkyDTO.class));
					}
				}
			}

			if(ooubitCmpCnt == ooubitValidationList.size()) {
				ooubhdDTO.setObostat(Obostat.CMP.getCode());
				apiOutboundMapper.updateObostatCMPForApi(ooubhdDTO, commonInfo);
			}

			//wshphd, it 상태 완료
			wtakitDTO.setShpitst(Shpitst.CMP.getCode());
			if(apiOutboundMapper.updateShpitstCMPForApi(wtakitDTO, commonInfo) == 0) {
				throw new UpdateCheckedException("출고문서 아이템 상태 업데이트 오류");
			}

			List<WshpitDTO> targetShpit = lookupForApiMapper.selectCmpTargetWshpit(wtakitDTO, commonInfo);

			for(WshpitDTO target : targetShpit) {
				wtakitDTO.setWarekey(target.getWarekey());
				wtakitDTO.setShpdcky(target.getShpdcky());

				List<WtakitDTO> taskList = lookupForApiMapper.selectWtakitListForApiValidation(wtakitDTO, commonInfo);
				List<WtakitDTO> noEndTaskList = taskList.stream().filter(
																								e -> Tasksts.NEW.getCode().equals(e.getTasksts())
																								|| Tasksts.OPERATION_START.getCode().equals(e.getTasksts())
																								|| Tasksts.TASK_COMPLETE.getCode().equals(e.getTasksts()))
																						.toList();
				if(noEndTaskList.isEmpty()) {
					target.setShpdcst(Shpdcst.CMP.getCode());
					if(apiOutboundMapper.updateShpdcstCMPForApi(target, commonInfo) == 0) {
						throw new UpdateCheckedException("출고문서 상태 업데이트 오류");
					}
				}
			}

			WshpitDTO wshpitParam = new WshpitDTO();
			wshpitParam.setOutboky(outboundOperationDTO.getOutboky());
			wshpitParam.setSkumkey(outboundOperationDTO.getSkumkey());

			// 배차 아이템 송장발행완료 처리
			int tplnitUpdateCnt = apiOutboundMapper.updateTplnitStatForApi(wshpitParam, Tpistat.INVCMP.getCode(), commonInfo);

			wshphdDTO.setOutboky(outboundOperationDTO.getOutboky());
			// 배차 아이템이 전부 송장발행완료이면
			Integer tplnitCountValidationList = lookupForApiMapper.selectTplnitCountValidationListForApi(wshphdDTO, commonInfo);

			if(tplnitCountValidationList == null || tplnitCountValidationList == 0) {
				int tplnhdUpdateCnt = apiOutboundMapper.updateTplnhdStatForApi(wshphdDTO, Tpnstat.INVCMP.getCode(), commonInfo);// 배차 헤더 송장발행완료 처리
			}

			List<OoubitDTO> outboundKeyList = lookupForApiMapper.selectShpplkyAndVhplnkyForApi(wtakitDTO, commonInfo);

			for(OoubitDTO outboundKey : outboundKeyList) {
				//TPWPWG 출고완료확정 Y UPDATE
				wshphdDTO.setVhplnky(outboundKey.getVhplnky());
				wshphdDTO.setShpplky(outboundKey.getShpplky());
				wshphdDTO.setPlnsize(outboundKey.getPlnsize());
				wshphdDTO.setSkumkey(outboundOperationDTO.getSkumkey());
				wshphdDTO.setShpcfyn(Useyn.USE.getString());
				if(apiOutboundMapper.updateTpwpwgShpcfynForApi(wshphdDTO, commonInfo) == 0) {
					throw new UpdateCheckedException("배차별 출고예정 상태 변경 오류");
				}

				// SHPPLKY로 헤더 조회 후 잔여중량이 0고 예정확정여부 지시여부 완료확정여부 ALL Y인지 확인 후
				WshphdDTO getShipmentCompletionStatus = lookupForApiMapper.checkShipmentCompletionStatusForApi(wshphdDTO, commonInfo);

				if(getShipmentCompletionStatus != null) {
					//wplnhd, it 상태 완료
					wshphdDTO.setWplstat(Wplstat.CMP.getCode());
					if(apiOutboundMapper.updateWplnhdStatForApi(wshphdDTO, commonInfo) == 0) {
						throw new UpdateCheckedException("출고계획 상태변경오류");
					}

					wshphdDTO.setWplitst(Wplitst.CMP.getCode());
					if(apiOutboundMapper.updateWplnitStatForApi(wshphdDTO, commonInfo) == 0) {
						throw new UpdateCheckedException("출고계획아이템 상태변경오류");
					}
				}

				wtakitDTO.setTasksts(Tasksts.SHPOUT.getCode());
				// 작업 출고완료 처리
				if(apiOutboundMapper.updateWtakitTaskstsForApi(wtakitDTO, commonInfo) == 0) {
					throw new UpdateCheckedException("작업문서 상태변경오류");
				}
			}
		}

		//실사된 from to 베드 리스트 - api용
		List<WstkkyDTO> apiPhyList = new ArrayList<>();
		//from 단 sort
		for(WstkkyDTO wstkkyDTO : fromLocationStock) {
			int fromLayerCount = 1;
			List<WstkkyDTO> fromLocakeyStockList = apiOutboundMapper.getApiLocationStockInfo(wstkkyDTO, commonInfo);

			for(WstkkyDTO fromLocakeyStock : fromLocakeyStockList) {
				fromLocakeyStock.setStlayer(fromLayerCount);
				fromLocakeyStock.setLolayer(fromLayerCount++);
				count = apiOutboundMapper.updateApiStockLayerSort(fromLocakeyStock, commonInfo);

				if(count == 0) {
					throw new UpdateCheckedException();
				}

				apiPhyList.add(fromLocakeyStock);
			}
		}
		//DO -> DC 실사정보(단수 정렬 정보) API CALL
		apiConfig.requestToDC(apiPhyList, ApiInfo.API_SM_WPHYIT, HttpMethod.PATCH);

		 // 관제 출고완료 송신
		apiConfig.requestToDC(apiCommon.getApiData(), ApiInfo.API_OM_WSHPIT, HttpMethod.POST);
		log.info("============ saveOutboundCompl api end ============");

		return apiCommon;
	}
}
