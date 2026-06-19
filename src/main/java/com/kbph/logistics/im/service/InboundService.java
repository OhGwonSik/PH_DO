package com.kbph.logistics.im.service;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbph.logistics.api.config.ApiConfig;
import com.kbph.logistics.api.enums.ApiInfo;
import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.enums.Inout;
import com.kbph.logistics.common.enums.Useyn;
import com.kbph.logistics.common.util.DateUtil;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.error.DeleteCheckedException;
import com.kbph.logistics.configuration.error.DuplicateLayerException;
import com.kbph.logistics.configuration.error.InsertCheckedException;
import com.kbph.logistics.configuration.error.NoSaveDataException;
import com.kbph.logistics.configuration.error.RequiredNotValueException;
import com.kbph.logistics.configuration.error.TaskProcessingException;
import com.kbph.logistics.configuration.error.UpdateCheckedException;
import com.kbph.logistics.im.domain.InbInspectionDTO;
import com.kbph.logistics.im.domain.InboundCheckDTO;
import com.kbph.logistics.im.domain.InboundOrderForTreeGridDTO;
import com.kbph.logistics.im.domain.OstrhdDTO;
import com.kbph.logistics.im.domain.OstritDTO;
import com.kbph.logistics.im.domain.OstritSeacrhParamDTO;
import com.kbph.logistics.im.domain.WasnhdDTO;
import com.kbph.logistics.im.domain.WasnitDTO;
import com.kbph.logistics.im.domain.WasnitSeacrhParamDTO;
import com.kbph.logistics.im.domain.WrcvhdDTO;
import com.kbph.logistics.im.domain.WrcvitDTO;
import com.kbph.logistics.im.domain.WrcvitSeacrhParamDTO;
import com.kbph.logistics.im.mapper.InboundMapper;
import com.kbph.logistics.md.domain.MskuwcDTO;
import com.kbph.logistics.md.mapper.UnitsMapper;
import com.kbph.logistics.md.type.Aprovyn;
import com.kbph.logistics.md.type.Asnitst;
import com.kbph.logistics.md.type.Asnstat;
import com.kbph.logistics.md.type.Doccate;
import com.kbph.logistics.md.type.DoctypeInbound;
import com.kbph.logistics.md.type.DoctypeInboundOrder;
import com.kbph.logistics.md.type.Rcvdcst;
import com.kbph.logistics.md.type.Rcvitst;
import com.kbph.logistics.md.type.Stritst;
import com.kbph.logistics.md.type.Strstat;
import com.kbph.logistics.md.type.Tasksts;
import com.kbph.logistics.rm.component.StoreStrategyComponent;
import com.kbph.logistics.rm.domain.WpaordDTO;
import com.kbph.logistics.rm.mapper.StoreStrategyMapper;
import com.kbph.logistics.sm.domain.WstkkyDTO;
import com.kbph.logistics.sm.domain.WtakitDTO;
import com.kbph.logistics.sm.mapper.InventoryMapper;
import com.kbph.logistics.sy.domain.UserVO;
import com.kbph.logistics.tm.mapper.TmDispatchMapper;
import com.kbph.logistics.tm.type.Dispatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * @author : OP
 * @version : 1.0.0
 * @since : 2024-07-09
 * @note : Service Class for Inbound
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-07-09					   OP           					create Service class
 * 2024-08-22					t.s.park        			create Inbound order method
 * 2024-08-29					t.s.park        		create Inbound document method
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InboundService {
	private final InboundMapper inboundMapper; // 입고
	private final InventoryMapper inventoryMapper; // 재고
	private final UnitsMapper unitsMapper; // 제품
	private final StoreStrategyMapper storeStrategyMapper; // 적치전략
	private final ApiConfig apiConfig; // API
	private final StoreStrategyComponent storestrategyComponent; // 적치전략 생성
	private final TmDispatchMapper tmDispatchMapper;

	//상세착지 키 분리
	private String getDestkey(String destkey) {
		if(destkey == null || destkey.isEmpty()) {
			return null;
		}

		int start = destkey.indexOf('(');
		int end = destkey.indexOf(')');

		if(start != -1 && end != -1 && start < end) {
			return destkey.substring(start + 1, end);
		}
		return destkey;
	}

	// 입고오더 생성
	public int createInboundOrder(List<InboundOrderForTreeGridDTO> inboundOrderList) {
		if(CollectionUtils.isEmpty(inboundOrderList)) {
			throw new NoSaveDataException();
		}

		ObjectMapper om = new ObjectMapper(); // 동일 컬럼 변환을 위함
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int insertOstrhdCnt = 0;
		int insertOstritCnt = 0;
		int insertWasnhdCnt = 0;
		int insertWasnitCnt = 0;
		int insertMskuwcCnt = 0;
		int count = 0;

		List<WstkkyDTO> toLocationStock = new ArrayList<>();
		//실사생성된 베드 리스트 - api용
		List<WstkkyDTO> apiPhyList = new ArrayList<>();
		List<WstkkyDTO> apiPhyCreateList = new ArrayList<>();

		for(InboundOrderForTreeGridDTO inboundOrder : inboundOrderList) { // 리스트 생성

			int strcqty = 0; // 입고완료수량
			String eoasnky = inboundMapper.selectEoasnky(userInfo); // asn 키 채번
			WasnhdDTO asnHeader = om.convertValue(inboundOrder, WasnhdDTO.class); // 동일한 필드는 변환하여 미리 대입
			asnHeader.setEoasnky(eoasnky); // header key 삽입

			//전략문서가 아닐경우만 자동승인
			String strstat = Strstat.PROCEED.getCode(); // 입고오더헤더정보 상태
			String stritst = Stritst.PROCEED.getCode(); // 입고오더아이템정보 상태
			String asnstat = Asnstat.ALL_CONFIRMED.getCode(); // 입고예정헤더정보 상태
			String asnitst = Asnitst.CONFIRMED.getCode(); // 입고예정아이템정보 상태

			if(asnHeader.getDoctype() != null
					&& (DoctypeInboundOrder.POSCO_STRATEGY.getCode().equals(asnHeader.getDoctype())
							|| DoctypeInboundOrder.OUTSIDE_STRATEGY.getCode().equals(asnHeader.getDoctype())
							|| DoctypeInboundOrder.TOLL_STRATEGY.getCode().equals(asnHeader.getDoctype()))) { // 전략 추가시 조건 추가
				asnstat = Asnstat.NEW.getCode(); // 입고예정헤더정보 상태
				asnitst = Asnitst.NEW.getCode(); // 입고예정아이템정보 상태
			}

			// 실사입고인경우
			if(DoctypeInboundOrder.PHYSICAL_CREATE.getCode().equals(asnHeader.getDoctype())) {
				strstat = Strstat.COMPLETE.getCode();
				stritst = Stritst.COMPLETE.getCode();
				asnstat = Asnstat.COMPLETE.getCode();
				asnitst = Asnitst.COMPLETE.getCode();
				strcqty = 1;
			}

			//입고오더 헤더
			String costrky = inboundMapper.selectCostrky(userInfo); // 입고오더 키 채번
			String warekey = inboundOrder.getWarekey();
			inboundOrder.setCostrky(costrky); // header key 삽입
			inboundOrder.setStrstat(strstat); // 입고오더 상태
			List<InboundOrderForTreeGridDTO> tmpOstrList = new ArrayList<>();
			tmpOstrList.add(inboundOrder);
			int tmpOstrhdCnt = inboundMapper.insertOstrhdList(tmpOstrList, userInfo); // 입고오더 헤더 생성

			if(tmpOstrhdCnt == 0) {
				throw new InsertCheckedException();
			}

			insertOstrhdCnt += tmpOstrhdCnt;

			asnHeader.setAsnstat(asnstat);
			asnHeader.setDoccate(Doccate.INBOUND.getCode()); // 문서유형
			asnHeader.setDoctype(DoctypeInbound.valueOf(DoctypeInboundOrder.getDoctypeToCode(inboundOrder.getDoctype()).name()).getCode()); // 문서타입
			asnHeader.setAsndate(inboundOrder.getStrdate()); // 입고예정날짜
			asnHeader.setAsntime(inboundOrder.getStrtime()); // 입고예정시간

			WpaordDTO wpaordParam = new WpaordDTO(); // 입고문서 전략 조회 param
			wpaordParam.setWarekey(asnHeader.getWarekey());
			wpaordParam.setDoccate(asnHeader.getDoccate());
			wpaordParam.setDoctype(asnHeader.getDoctype());

			WpaordDTO storeStr = storeStrategyMapper.selectWpaord(wpaordParam, userInfo); // 현재 asn의 문서유형+타입으로 전략 검색
			asnHeader.setPastrky((storeStr != null) ? storeStr.getPastrky() : null); // 일치하는 전략 대입

			List<WasnhdDTO> tmpAsnhd = new ArrayList<>();
			tmpAsnhd.add(asnHeader);
			int tmpWasnhdCnt = inboundMapper.insertWasnhdList(tmpAsnhd, userInfo); // 입고예정정보 헤더 생성

			if(tmpWasnhdCnt == 0) {
				throw new InsertCheckedException();
			}
			insertWasnhdCnt += tmpWasnhdCnt;

			//입고아이템
			List<WasnitDTO> asnItemList = new ArrayList<>();
			List<MskuwcDTO> skuList = new ArrayList<>();
			List<InboundOrderForTreeGridDTO> osrtitList = inboundOrder.getChildren(); // 트리(헤더)의 1차 노드(아이템)들
			for(int idx=0; idx<osrtitList.size(); idx++) {
				int itemKey = idx+1;
				InboundOrderForTreeGridDTO osrtit = osrtitList.get(idx);

				osrtit.setCostrky(costrky); // 입고오더번호
				osrtit.setCostrit(itemKey); // 입고오더 아이템번호
				osrtit.setWarekey(warekey); // 창고키
				osrtit.setStritst(stritst); // 입고오더아이템 상태
				osrtit.setStrcqty(strcqty); // 입고완료 수량
				if(osrtit.getRegimod() == null || osrtit.getRegimod().isEmpty()) {
					osrtit.setRegimod(osrtit.getRegiorg());
				}
				osrtit.setDestorg(getDestkey(osrtit.getDestorg())); // 원본상세착지 키 분리
				if(osrtit.getDestmod() == null || osrtit.getDestmod().isEmpty()) {
					osrtit.setDestmod(osrtit.getDestorg());
				}else {
					osrtit.setDestmod(getDestkey(osrtit.getDestmod())); // 변경상세착지 키 분리
				}
				if(osrtit.getDemodnm() == null || osrtit.getDemodnm().isEmpty()) {
					osrtit.setDemodnm(osrtit.getDeorgnm());
				} else {
					osrtit.setDemodnm(getDestkey(osrtit.getDeorgnm())); // 변경상세착지 키 분리
				}
				osrtit.setVehicky(inboundOrder.getVehicky()); // API 전송 데이터
				osrtit.setVhcfnam(inboundOrder.getVhcfnam()); // API 전송 데이터
				osrtit.setEoasnky(eoasnky);
				osrtit.setDoccate(inboundOrder.getDoccate()); // 문서유형
				osrtit.setDoctype(inboundOrder.getDoctype()); // 문서타입
				osrtit.setRsncode(inboundOrder.getRsncode()); // 사유코드
				osrtit.setParsnnm(inboundOrder.getParsnnm()); // 사유내용

				WasnitDTO tmpAsnit = om.convertValue(osrtit, WasnitDTO.class); // 동일한 필드는 변환하여 미리 대입
				tmpAsnit.setEoasnky(eoasnky); // asn key
				tmpAsnit.setEoasnit(itemKey); // asn item number
				tmpAsnit.setAsnitst(asnitst); // asn 아이템 상태

				tmpAsnit.setAsndqty(osrtit.getStroqty()); // asn 입고예정 수량
				tmpAsnit.setRchsqty(osrtit.getStrcqty()); // asn 입고완료 수량
				tmpAsnit.setDoccate(asnHeader.getDoccate()); // asn 문서유형
				tmpAsnit.setDoctype(asnHeader.getDoctype()); // asn 문서타입
				tmpAsnit.setRsncdnm(inboundOrder.getParsnnm()); // 사유내용

				skuList.add(om.convertValue(tmpAsnit, MskuwcDTO.class)); // 제품 정보 추출하여 삽입
				asnItemList.add(tmpAsnit); // 저장할 리스트 추가
			}

			int tmpOstritCnt = inboundMapper.insertOstritList(osrtitList , userInfo); // 입고오더 아이템 생성
			if(tmpOstritCnt == 0) {
				throw new InsertCheckedException();
			}
			insertOstritCnt += tmpOstritCnt;

			for(WasnitDTO wasnitDTO : asnItemList) {
				// null , 빈문자, 공백 - 값이 있는 경우만
				if(StringUtils.hasText(wasnitDTO.getTolocky()) &&
					    StringUtils.hasText(wasnitDTO.getUllocky()) &&
					    StringUtils.hasText(wasnitDTO.getEquipky())) {
					count = inboundMapper.checkRelationship(wasnitDTO, userInfo);

					if(count != 2) {
						throw new UpdateCheckedException("ms.checkTolocAndEquipky");
					}
				}
			}

			//전략문서일 경우 param storeStrategyAsnItem , 전략문서가 아닐경우 asnItemList
			int tmpWasnitCnt = inboundMapper.insertWasnitList(asnItemList, userInfo);	// 입고예정정보 아이템 생성
			if(tmpWasnitCnt == 0) {
				throw new InsertCheckedException();
			}

			insertWasnitCnt += tmpWasnitCnt;

			for(MskuwcDTO sku : skuList) {
				int tmpSkuInsertCount = unitsMapper.insertMskuwc(sku, userInfo); // 제품 생성
				if(tmpSkuInsertCount == 0) {
					throw new InsertCheckedException();
				}
				insertMskuwcCnt += tmpSkuInsertCount;
			}

			if(DoctypeInbound.PHYSICAL_CREATE.getCode().equals(asnHeader.getDoctype())) {
				//TASK, 입고문서 생성 및 실사생성 일 경우 CMP 로직 추가
				count = createInboundDocument(asnItemList);

				if(count == 0) {
					throw new InsertCheckedException();
				}

				//shift
				int layer = 0;
				for(WasnitDTO wasnitDTO : asnItemList) {
					List<WstkkyDTO> getLocationStockList = inboundMapper.getFromLocationAndLayerStock(wasnitDTO, userInfo);
					layer = wasnitDTO.getTolayer();
					for(WstkkyDTO getLocationStockData : getLocationStockList) {
						getLocationStockData.setLolayer(++layer);

						count = inboundMapper.updateLayerStockShift(getLocationStockData, userInfo);

						if(count == 0) {
							throw new UpdateCheckedException();
						}
					}

					WtakitDTO wtakitDTO = inboundMapper.getAsnTaskoky(wasnitDTO, userInfo);

					//재고 insert
					wasnitDTO.setStockky(inboundMapper.selectStockky(userInfo));
					wasnitDTO.setLotnmky(inboundMapper.selectLotnmky(userInfo));
					wasnitDTO.setTaskoky(wtakitDTO.getTaskoky());
					wasnitDTO.setTaskoit(wtakitDTO.getTaskoit());
					count = inboundMapper.saveDoe033Wstkky(wasnitDTO, userInfo);

					if(count == 0) {
						throw new InsertCheckedException();
					}

					count = inboundMapper.updateWtakitStockky(wasnitDTO, userInfo);

					if(count == 0) {
						throw new UpdateCheckedException();
					}

					if (toLocationStock.stream().noneMatch(dto ->
					dto.getToareky().equals(wasnitDTO.getToareky()) &&
					dto.getTolocky().equals(wasnitDTO.getTolocky()))) {
						toLocationStock.add(om.convertValue(wasnitDTO, WstkkyDTO.class));
					}

					//실사생성 api를 위한 조회 후 map에 set
					WstkkyDTO phyCreateStock = inboundMapper.getPhyCreateApiData(wasnitDTO, userInfo);
					apiPhyCreateList.add(phyCreateStock);
				}

				//shift api
				//조회후 map에 set
				for(WstkkyDTO wstkkyDTO : toLocationStock) {
					List<WstkkyDTO> toLocakeyStockList = inboundMapper.getPhyCreateLocationStockInfo(wstkkyDTO, userInfo);
					toLocakeyStockList.forEach(apiPhyList::add);
				}

				// 재고 insert 실사문서
				apiConfig.requestToDC(apiPhyCreateList, ApiInfo.API_SM_WPHYIT_CREATE, HttpMethod.POST);
				// 재고 단정렬 실사문서
				apiConfig.requestToDC(apiPhyList, ApiInfo.API_SM_WPHYIT, HttpMethod.PATCH);
			}

			//전략문서가 아닐 경우(기타입고도 제외) 자동 예정확정처리라 관제측 인터페이스
			if(asnHeader.getDoctype() != null
					&& (!DoctypeInbound.POSCO_STRATEGY.getCode().equals(asnHeader.getDoctype())
							&& !DoctypeInbound.OUTSIDE_STRATEGY.getCode().equals(asnHeader.getDoctype())
							&& !DoctypeInbound.TOLL_STRATEGY.getCode().equals(asnHeader.getDoctype())
							&& !DoctypeInbound.PHYSICAL_CREATE.getCode().equals(asnHeader.getDoctype()))) {
				// API를 위한 set
				for(WasnitDTO item : asnItemList) {
					item.setVehicky(inboundOrder.getVehicky());
					item.setVhcfnam(inboundOrder.getVhcfnam());
				}
				apiConfig.requestToDC(asnItemList, ApiInfo.API_IM_WASNIF, HttpMethod.POST);
			}
		}

		return insertOstrhdCnt + insertOstritCnt + insertWasnhdCnt + insertWasnitCnt + insertMskuwcCnt; // 검증용 리턴
	}

	// 직송처리
	public int saveDirectShipping(List<WasnhdDTO> saveList){
		if(CollectionUtils.isEmpty(saveList)) {
			throw new NoSaveDataException();
		}

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int totalCnt = 0;
		for(WasnhdDTO wasnhd : saveList) {
			wasnhd.setVhplnky(tmDispatchMapper.selectVhplnky(userInfo));
			wasnhd.setDoccate(Doccate.TRANSIT.getCode()); // 문서유형
			wasnhd.setDoctype(DoctypeInboundOrder.DIRECT_SHIPPING.getCode());					 // 문서타입
			wasnhd.setLoadkey(Dispatch.OWNER.getString());
			wasnhd.setAprovyn(Aprovyn.APPROVAL.getCode());
			int headResult = inboundMapper.saveDirectShippingHeader(wasnhd, userInfo);

			int itemResult = inboundMapper.saveDirectShippingItem(wasnhd, userInfo);
			int checkWasnitItem = inboundMapper.wasnitCount(wasnhd, userInfo);
			if(headResult != 1 || itemResult != checkWasnitItem) {
				throw new InsertCheckedException();
			}

			int deleteHeadCnt = inboundMapper.deleteWasnhd(wasnhd, userInfo);
			int deleteItemCnt = inboundMapper.deleteWasnit(wasnhd, userInfo);

			if(deleteHeadCnt != 1 || deleteItemCnt != checkWasnitItem) {
				throw new DeleteCheckedException();
			}
			totalCnt += headResult;

			wasnhd.setCncldat(DateUtil.getDateTime("yyyyMMdd HHmmss"));
			wasnhd.setRsncdnm(DoctypeInboundOrder.DIRECT_SHIPPING.getDescription());
		}

		apiConfig.requestToDC(saveList, ApiInfo.API_IM_WASNIF_CANCEL, HttpMethod.PATCH);

		return totalCnt;
	}

	//입고예정 삭제
	public int cancelAsnItem(List<WasnitDTO> requestDTO) {
		if(CollectionUtils.isEmpty(requestDTO)) {
			throw new NoSaveDataException();
		}
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		for(WasnitDTO wasnitDTO : requestDTO) {
			count = inboundMapper.deleteDoe024Wasnit(wasnitDTO, userInfo);

			if(count == 0) {
				throw new DeleteCheckedException();
			}

			count = inboundMapper.deleteDoe024Mskuwc(wasnitDTO, userInfo);

			if(count == 0) {
				throw new DeleteCheckedException();
			}
		}

		//차량단 shift
		List<WasnitDTO> getAsnItemListStlayer = inboundMapper.getDoe024AsnItemListStlayer(requestDTO.get(0), userInfo);
		int stlayer = 1;
		for(WasnitDTO getAsnItemDataStlayer : getAsnItemListStlayer) {
			getAsnItemDataStlayer.setStlayer(stlayer++);
			count = inboundMapper.updateDoe024AsnItemStlayer(getAsnItemDataStlayer, userInfo);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}

		return count;
	}

	// wasnhd 조회
	public List<WasnhdDTO> getWasnhdList(WasnhdDTO wasnhdDTO){
		return inboundMapper.getWasnhdList(wasnhdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// wasnit 조회
	public List<WasnitDTO> getWasnitList(WasnitDTO wasnitDTO){
		return inboundMapper.getWasnitList(wasnitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// wasnit 조회
	public List<WasnitDTO> getDoe024WasnitList(WasnitDTO wasnitDTO){
		// 상태값 추가
		return inboundMapper.getDoe024WasnitList(wasnitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// wasnit 조회(확정시점 + 전략)
	public List<WasnitDTO> getWasnitListForConfirm(List<WasnitDTO> wasnitList){
		return storestrategyComponent.setAsnLocationWithStoreStrategy(wasnitList);
	}

	// task 할당 안된 asn item 조회
	public List<WasnitDTO> getWasnitListWithoutTask(WasnitDTO wasnitDTO) {
		return inboundMapper.selectWasnitListWithoutTask(wasnitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 최상단 체크
	public int saveAsnTopLayer(List<WasnitDTO> saveList) {
		if(CollectionUtils.isEmpty(saveList)) {
			throw new NoSaveDataException();
		}

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int headUpdateCount = 0;
		int itemUpdate1stCount = 0;
		int itemUpdateSwapCount = 0;

		WasnitDTO wasnitDTO = saveList.get(0); // 저장데이터 하나만 가져오기 때문에 index 0
		itemUpdate1stCount = inboundMapper.updateWasnitToTopLayer(wasnitDTO, userInfo); // target 단수 최상단으로
		itemUpdateSwapCount = inboundMapper.updateWasnitSwapLayer(wasnitDTO, userInfo); // 원래 최상단 단과 단수 교체

		if(itemUpdate1stCount == 0  || itemUpdateSwapCount == 0) {
			throw new UpdateCheckedException();
		}

		WasnhdDTO wasnhdDTO = new WasnhdDTO();
		wasnhdDTO.setWarekey(wasnitDTO.getWarekey());
		wasnhdDTO.setEoasnky(wasnitDTO.getEoasnky());
		headUpdateCount = inboundMapper.updateWasnhdAsnpiyn(wasnhdDTO, userInfo); // 예정검수여부 체크 Y로

		if(headUpdateCount == 0) {
			throw new UpdateCheckedException();
		}

		return headUpdateCount + itemUpdate1stCount + itemUpdateSwapCount;
	}

	// 입고예정정보 헤드 상태 저장(확정)
	public int saveAsnStatusForAllConfirm(List<WasnhdDTO> saveList){
		if(CollectionUtils.isEmpty(saveList)) {
			throw new NoSaveDataException();
		}

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int totalUpdateWasnhdCnt = 0;
		int totalUpdateWasnitCnt = 0;

		for(WasnhdDTO asnHead : saveList) {
			WasnitSeacrhParamDTO paramForTarget = WasnitSeacrhParamDTO.builder()
																	.eoasnky(asnHead.getEoasnky())
																	.warekey(asnHead.getWarekey())
																	.build();
			List<WasnitDTO> targetWasnitList = inboundMapper.selectWasnitKeyAndItem(paramForTarget, userInfo); // 헤더에 대한 타겟 아이템들 조회

			// 필수값 검증
			for(WasnitDTO wasnitDTO : targetWasnitList) {
				// null , 빈문자, 공백
				if(!StringUtils.hasText(wasnitDTO.getToareky()) &&
					    !StringUtils.hasText(wasnitDTO.getTolocky()) &&
					    !StringUtils.hasText(wasnitDTO.getUllocky()) &&
					    !StringUtils.hasText(wasnitDTO.getEquipky())) {
					throw new RequiredNotValueException(); // 필수값 입력 exception
				}
			}

			int updateWasnitCnt = inboundMapper.updateWasnitStatus(targetWasnitList, Asnitst.CONFIRMED.getCode(), true, userInfo); // 상태 업데이트(CANCEL 제외)

			if(updateWasnitCnt == 0) {
				throw new UpdateCheckedException();
			}

			totalUpdateWasnitCnt += updateWasnitCnt; // 업데이트가 일어났다면 카운트 집계

			List<String> asnitstList = new ArrayList<>();
			asnitstList.add(Asnitst.NEW.getCode());
			asnitstList.add(Asnitst.CANCEL.getCode());

			WasnitSeacrhParamDTO paramForStat = WasnitSeacrhParamDTO.builder()
																	.eoasnky(asnHead.getEoasnky())
																	.warekey(asnHead.getWarekey())
																	.asnitstList(asnitstList)
																	.build();
			List<WasnitDTO> wasnitStatusList = inboundMapper.selectWasnitKeyAndItem(paramForStat, userInfo); // 아이템의 리스트 상태에 따라서 헤더 상태를 업데이트 하기 위해 조회

			if(inboundMapper.updateWasnhdStatus(asnHead, (wasnitStatusList.isEmpty() ? Asnstat.ALL_CONFIRMED.getCode() : Asnstat.PART_CONFIRMED.getCode()), userInfo)==0) {
				throw new UpdateCheckedException();
			}
			totalUpdateWasnhdCnt++;

			// API를 위한 set
			for(WasnitDTO item : targetWasnitList) {
				item.setVehicky(asnHead.getVehicky());
				item.setVhcfnam(asnHead.getVhcfnam());
			}
//			boolean isExcel = "411".equals(asnHead.getDoctype()); // (auto 생성인 DOCTYPE) check 필요

//			apiConfig.requestToDC(targetWasnitList, ApiInfo.API_IM_WASNIF, (isExcel ? HttpMethod.POST : HttpMethod.PATCH));
			apiConfig.requestToDC(targetWasnitList, ApiInfo.API_IM_WASNIF, HttpMethod.POST); // 모든 처리 동기화
		}

		return totalUpdateWasnhdCnt + totalUpdateWasnitCnt;
	}

	// 입고예정정보 아이템 상태 저장(확정)
	public int saveAsnStatusForPartConfirm(List<WasnitDTO> saveList) {
		if(CollectionUtils.isEmpty(saveList)) {
			throw new NoSaveDataException();
		}

		// 필수값 검증
		for(WasnitDTO wasnitDTO : saveList) {
			// null , 빈문자, 공백
			if(!StringUtils.hasText(wasnitDTO.getToareky()) &&
				    !StringUtils.hasText(wasnitDTO.getTolocky()) &&
				    !StringUtils.hasText(wasnitDTO.getUllocky()) &&
				    !StringUtils.hasText(wasnitDTO.getEquipky())) {
				throw new RequiredNotValueException(); // 필수값 입력 exception
			}
		}

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();

		for(WasnitDTO wasnitDTO : saveList) {
			if(StringUtils.hasText(wasnitDTO.getDestorg())) {
				wasnitDTO.setDestorg(getDestkey(wasnitDTO.getDestorg())); // 원본상세착지 키 분리
				wasnitDTO.setDestmod(wasnitDTO.getDestorg());
			}
		}

		int updateWasnitCnt = inboundMapper.updateDoe024WasnitStatus(saveList, Asnitst.CONFIRMED.getCode(), true, userInfo); // asn item 대상 업데이트(Cancel 제외)

		if(updateWasnitCnt == 0) {
			throw new UpdateCheckedException();
		}

		List<String> asnitstList = new ArrayList<>();
		asnitstList.add(Asnitst.NEW.getCode());
		asnitstList.add(Asnitst.CANCEL.getCode());

		WasnitSeacrhParamDTO paramForStat = WasnitSeacrhParamDTO.builder()
																.eoasnky(saveList.get(0).getEoasnky())
																.warekey(saveList.get(0).getWarekey())
																.asnitstList(asnitstList)
																.build();
		List<WasnitDTO> wasnitNewStatusList = inboundMapper.selectWasnitKeyAndItem(paramForStat, userInfo); // 아이템의 리스트 상태에 따라서 헤더 상태를 업데이트 하기 위해 조회
		WasnhdDTO wasnhd = new WasnhdDTO();
		wasnhd.setEoasnky(paramForStat.getEoasnky());
		wasnhd.setWarekey(paramForStat.getWarekey());
		WasnhdDTO wasnHead = inboundMapper.selectWasnhdKey(wasnhd, userInfo);
		wasnHead.setHdsavyn(Useyn.USE.getString());
		int updateWasnhdCnt = inboundMapper.updateDoe024WasnhdStatus(wasnHead, (wasnitNewStatusList.isEmpty() ? Asnstat.ALL_CONFIRMED.getCode() : Asnstat.PART_CONFIRMED.getCode()), userInfo); // asn 헤더 업데이트(조건별 상태)

		if(updateWasnhdCnt == 0) {
			throw new UpdateCheckedException();
		}

		// API를 위한 set
		WasnhdDTO head = inboundMapper.selectWasnhdKey(wasnhd, userInfo);
		for(WasnitDTO item : saveList) {
			item.setVehicky(head.getVehicky());
			item.setVhcfnam(head.getVhcfnam());
		}

		if(Useyn.USE.getString().equals(saveList.get(0).getAsncfyn())) {
			apiConfig.requestToDC(saveList, ApiInfo.API_IM_WASNIF, HttpMethod.PATCH);
		}else {
			apiConfig.requestToDC(saveList, ApiInfo.API_IM_WASNIF, HttpMethod.POST);
		}


		return updateWasnitCnt + updateWasnhdCnt;
	}

	// 입고예정정보 상태 저장(취소)
	public int saveAsnStatusForAllCancel(List<WasnhdDTO> saveList){
		if(CollectionUtils.isEmpty(saveList)) {
			throw new NoSaveDataException();
		}

		ObjectMapper om = new ObjectMapper();
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int totalUpdateWasnhdCnt = 0;
		int totalUpdateWasnitCnt = 0;
		int totalUpdateOstrhdCnt = 0;
		int totalUpdateOstritCnt = 0;
		int count = 0;

		for(WasnhdDTO asnHead : saveList) {
			WasnitSeacrhParamDTO paramForTarget = WasnitSeacrhParamDTO.builder()
																				.eoasnky(asnHead.getEoasnky())
																				.warekey(asnHead.getWarekey())
																				.build();
			List<WasnitDTO> targetWasnitList = inboundMapper.selectWasnitKeyAndItem(paramForTarget, userInfo); // 입고예정정보 아이템 조회
			for(WasnitDTO wasnit : targetWasnitList) {
				wasnit.setRsncode(asnHead.getRsncode());
				wasnit.setParsnnm(asnHead.getRcarsnm());

				//제품마스터 삭제
				count = inboundMapper.deleteDoe024Mskuwc(wasnit, userInfo);

				if(count == 0) {
					throw new DeleteCheckedException();
				}

			} // 전체 취소시 헤더의 reason code item에 삽입
			int updateWasnitCnt = inboundMapper.updateWasnitStatus(targetWasnitList, Asnitst.CANCEL.getCode(), true, userInfo); // asn 아이템 상태 업데이트
			if(updateWasnitCnt == 0) {
				throw new UpdateCheckedException();
			}
			totalUpdateWasnitCnt += updateWasnitCnt;

			List<OstritDTO> targetOstritList = new ArrayList<>();
			for(WasnitDTO wasnit : targetWasnitList) {
				if(wasnit.getCostrky() != null && wasnit.getCostrit() != null) { // 입고오더 키값들이 있을 경우
					OstritDTO tmp = om.convertValue(wasnit, OstritDTO.class); // ASN -> 입고오더로 변환
					tmp.setRsncode(wasnit.getRsncode()); // 취소사유코드 삽입
					tmp.setParsnnm(wasnit.getRcarsnm());
					targetOstritList.add(tmp);
				}

				// API를 위한 set
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
				wasnit.setCncldat(dateFormat.format(new Date()));
				wasnit.setRsncdnm(asnHead.getRsncode()); // 취소사유명
			}

			if(!targetOstritList.isEmpty()) {
				int updateOstritCnt = inboundMapper.updateOstritStatus(targetOstritList, Stritst.CANCEL.getCode(), true, userInfo); // 입고오더 아이템 상태 업데이트
				if(updateOstritCnt == 0) {
					throw new UpdateCheckedException();
				}
				totalUpdateOstritCnt += updateOstritCnt;
			}

			List<String> asnitstList = new ArrayList<>();
			asnitstList.add(Asnitst.NEW.getCode());
			asnitstList.add(Asnitst.CONFIRMED.getCode());
			WasnitSeacrhParamDTO paramForAsnitst = WasnitSeacrhParamDTO.builder()
																				.eoasnky(asnHead.getEoasnky())
																				.warekey(asnHead.getWarekey())
																				.asnitstList(asnitstList)
																				.build();
			List<WasnitDTO> wasnitStatusList = inboundMapper.selectWasnitKeyAndItem(paramForAsnitst, userInfo); // 아이템의 리스트 상태에 따라서 헤더 상태를 업데이트 하기 위해 조회

			//값이 안들어가는 문제로 값 재셋팅
			if(wasnitStatusList.isEmpty()) {
				asnHead.setRsncode(asnHead.getRsncode());
				asnHead.setParsnnm(asnHead.getRcarsnm());
				if(inboundMapper.updateWasnhdStatus(asnHead, Asnstat.CANCEL.getCode(), userInfo) == 0) { // asn 헤더 상태 업데이트(취소)
					throw new UpdateCheckedException();
				}
				totalUpdateWasnhdCnt++;
			}

			if(!targetOstritList.isEmpty()) {
				OstritDTO param = targetOstritList.get(0);
				List<String> stritstList = new ArrayList<>();
				stritstList.add(Stritst.NEW.getCode());
				stritstList.add(Asnitst.CONFIRMED.getCode());
				OstritSeacrhParamDTO paramForStritst = OstritSeacrhParamDTO.builder()
																					.costrky(param.getCostrky())
																					.warekey(param.getWarekey())
																					.stritstList(stritstList).build();
				List<OstritDTO> ostritStatusList = inboundMapper.selectOstritKeyAndItem(paramForStritst, userInfo);
				if(ostritStatusList.isEmpty()) {
					OstrhdDTO tmpOstrhd = om.convertValue(paramForStritst, OstrhdDTO.class);
					tmpOstrhd.setRsncode(asnHead.getRsncode());
					tmpOstrhd.setParsnnm(asnHead.getRcarsnm());
					if(inboundMapper.updateOstrhdStatus(tmpOstrhd, Strstat.CANCEL.getCode(), userInfo)==0) {
						throw new UpdateCheckedException();
					}
					totalUpdateOstrhdCnt++;
				}
			}
			// API 호출 (임시 주석)
			apiConfig.requestToDC(targetWasnitList, ApiInfo.API_IM_WASNIF_CANCEL, HttpMethod.PATCH);
		}

		return totalUpdateWasnhdCnt + totalUpdateWasnitCnt + totalUpdateOstrhdCnt + totalUpdateOstritCnt;
	}

	// 입고예정정보 아이템 상태 저장(취소)
	public int saveAsnStatusForPartCancel(List<WasnitDTO> saveList){
		if(CollectionUtils.isEmpty(saveList)) {
			throw new NoSaveDataException();
		}

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int updateWasnitCnt = inboundMapper.updateWasnitStatus(saveList, Asnitst.CANCEL.getCode(), true, userInfo); // asn item 취소 상태 update
		ObjectMapper om = new ObjectMapper();

		if(updateWasnitCnt == 0) {
			throw new UpdateCheckedException();
		}

		List<OstritDTO> targetOstritList = new ArrayList<>(); // 입고오더로 변환
		for(WasnitDTO wasnit : saveList) {
			if(wasnit.getCostrky() != null && wasnit.getCostrit() != null) { // asn에 입고오더 키값들이 없으면 변환 x
				OstritDTO tmpOstrit = om.convertValue(wasnit, OstritDTO.class);
				wasnit.setRsncode(wasnit.getRcarscd());
				wasnit.setRsncdnm(wasnit.getRcarsnm());
				tmpOstrit.setRsncode(wasnit.getRcarscd());
				tmpOstrit.setParsnnm(wasnit.getRcarsnm());
				targetOstritList.add(tmpOstrit);
			}

			// API를 위한 set
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
			wasnit.setCncldat(dateFormat.format(new Date()));
		}

		int updateOstritCnt = 0;
		if(!targetOstritList.isEmpty()) {
			int tmpOstritCnt = inboundMapper.updateOstritStatus(targetOstritList, Stritst.CANCEL.getCode(), true, userInfo); // 입고오더 취소 상태 update
			if(tmpOstritCnt == 0) {
				throw new UpdateCheckedException();
			}
			updateOstritCnt += tmpOstritCnt;
		}

		List<String> asnitstList = new ArrayList<>();
		asnitstList.add(Asnitst.NEW.getCode());
		asnitstList.add(Asnitst.CONFIRMED.getCode());
		WasnitSeacrhParamDTO paramForStat = WasnitSeacrhParamDTO.builder()
																									.eoasnky(saveList.get(0).getEoasnky())
																									.warekey(saveList.get(0).getWarekey())
																									.asnitstList(asnitstList)
																									.build();

		int wasnitNewStatCnt = 0;
		int wasnitConfirmStatCnt = 0;
		List<WasnitDTO> wasnitStatusList = inboundMapper.selectWasnitKeyAndItem(paramForStat, userInfo); // 아이템의 리스트 상태에 따라서 헤더 상태를 업데이트 하기 위해 조회

		for(WasnitDTO wasnit : wasnitStatusList) { // 조건 처리를 위해 상태값 별 카운트
			if(Asnitst.NEW.getCode().equals(wasnit.getAsnitst())) {
				wasnitNewStatCnt++;
			} else if(Asnitst.CONFIRMED.getCode().equals(wasnit.getAsnitst())) {
				wasnitConfirmStatCnt++;
			}
		}

		WasnhdDTO wasnhd = new WasnhdDTO();
		wasnhd.setEoasnky(paramForStat.getEoasnky());
		wasnhd.setWarekey(paramForStat.getWarekey());

		int updateOstrhdCnt = 0;
		String asnstat = Asnstat.CANCEL.getCode(); // 혹시 몰라 Default 값으로 set
		if(wasnitNewStatCnt == 0 && wasnitConfirmStatCnt == 0) {
			if(!targetOstritList.isEmpty()) {
				OstrhdDTO targetOstrhd = om.convertValue(targetOstritList.get(0), OstrhdDTO.class);
				updateOstrhdCnt += inboundMapper.updateOstrhdStatus(targetOstrhd, Strstat.CANCEL.getCode(), userInfo); // 입고오더 상태 update(취소)

				if(updateOstrhdCnt == 0) {
					throw new UpdateCheckedException();
				}
			}
		} else if(wasnitConfirmStatCnt > 0) {
			asnstat = Asnstat.PART_CONFIRMED.getCode();
		} else if(wasnitNewStatCnt > 0 && wasnitConfirmStatCnt == 0) {
			asnstat = Asnstat.PART_CANCEL.getCode();
		}

		int updateWasnhdCnt = inboundMapper.updateWasnhdStatus(inboundMapper.selectWasnhdKey(wasnhd, userInfo), asnstat, userInfo); // asn 헤더 상태 업데이트
		if(updateWasnhdCnt == 0) {
			throw new UpdateCheckedException();
		}

		// API 호출 (임시 주석)
		apiConfig.requestToDC(saveList, ApiInfo.API_IM_WASNIF_CANCEL, HttpMethod.PATCH);

		return updateWasnitCnt + updateWasnhdCnt + updateOstrhdCnt + updateOstritCnt; // 검증용
	}

	public List<WasnhdDTO> getDoe027HeadList(WasnhdDTO wasnhdDTO){
		List<String> asnstatList = new ArrayList<>();
		asnstatList.add(Asnstat.PART_CONFIRMED.getCode());
		asnstatList.add(Asnstat.ALL_CONFIRMED.getCode());
		wasnhdDTO.setAsnstatList(asnstatList);
		List<String> asnitstList = new ArrayList<>();
		asnitstList.add(Asnitst.CONFIRMED.getCode());
		wasnhdDTO.setAsnityn(Useyn.USE.getString());
		return inboundMapper.getWasnhdList(wasnhdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<WasnitDTO> getDoe027ItemList(WasnitDTO wasnitDTO){
		wasnitDTO.setAsnitst(Asnitst.CONFIRMED.getCode());

		return inboundMapper.getWasnitList(wasnitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public int saveHeadPassDoe027(InbInspectionDTO requestList){
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

			//아이템 조회 update
			for(WasnitDTO wasnitDTO : requestList.getItemParam()) {

				//API를 위한 set
				wasnitDTO.setVehicky(requestList.getHeadParam().getVehicky());
				wasnitDTO.setRsncode(requestList.getHeadParam().getRsncode());
				wasnitDTO.setRsncdnm(requestList.getHeadParam().getRsncdnm());
				wasnitDTO.setAsnitst(Asnitst.PASS_INSPECTION.getCode());

				count = inboundMapper.updateAsnitstDoe027(wasnitDTO, userVO);

				if(count == 0) {
					throw new UpdateCheckedException();
				}
			}

			// 헤더 update
			requestList.getHeadParam().setAsnstat(Asnstat.PASS_INSPECTION.getCode());
			count = inboundMapper.updateHeadAsnstatDoe027(requestList.getHeadParam(), userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}

			//입고문서생성 및 입고지시
			count = createInboundDocument(requestList.getItemParam());

		return count;
	}

	public int saveHeadFailDoe027(GridDTO<WasnhdDTO> requestList){
		ObjectMapper om = new ObjectMapper(); // 변환 util
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		for(WasnhdDTO wasnhdDTO : requestList.getUpdateList()) {

			List<OstritDTO> targetOstritList = new ArrayList<>(); // 입고오더로 변환

			WasnitSeacrhParamDTO paramForTarget = WasnitSeacrhParamDTO.builder()
																		.eoasnky(wasnhdDTO.getEoasnky())
																		.warekey(wasnhdDTO.getWarekey())
																		.build();

			List<WasnitDTO> targetWasnitList = inboundMapper.selectWasnitKeyAndItemAll(paramForTarget, userVO); // 헤더에 대한 타겟 아이템들 조회
			//아이템 조회 update
			for(WasnitDTO wasnitDTO : targetWasnitList) {

				wasnitDTO.setRsncode(wasnhdDTO.getRsncode());
				wasnitDTO.setAsnitst(Asnitst.CANCEL.getCode());

				count = inboundMapper.updateAsnitstDoe027(wasnitDTO, userVO);

				if(count == 0) {
					throw new UpdateCheckedException();
				}

				if(wasnitDTO.getCostrky() != null && wasnitDTO.getCostrit() != null) { // asn에 입고오더 키값들이 없으면 변환 x
					OstritDTO tmpOstrit = om.convertValue(wasnitDTO, OstritDTO.class);
					wasnitDTO.setRsncode(wasnhdDTO.getRsncode());
					wasnitDTO.setRsncdnm(wasnhdDTO.getRcarsnm());
					tmpOstrit.setRsncode(wasnhdDTO.getRsncode());
					tmpOstrit.setParsnnm(wasnhdDTO.getRcarsnm());
					targetOstritList.add(tmpOstrit);
				}

				count = inboundMapper.deleteDoe024Mskuwc(wasnitDTO, userVO);

				if(count == 0) {
					throw new DeleteCheckedException();
				}
			}

			// 헤더 update
			wasnhdDTO.setAsnstat(Asnstat.CANCEL.getCode());
			count = inboundMapper.updateHeadAsnstatDoe027(wasnhdDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}


			//입고오더가 없을 경우
			if(!CollectionUtils.isEmpty(targetOstritList)) {
				//입고오더아이템 취소
				count = inboundMapper.updateOstritStatus(targetOstritList, Stritst.CANCEL.getCode(), true, userVO); // 입고오더 취소 상태 update

				if(count == 0) {
					throw new UpdateCheckedException();
				}
				//입고오더 헤더 취소
				OstrhdDTO targetOstrhd = om.convertValue(targetOstritList.get(0), OstrhdDTO.class);
				count = inboundMapper.updateOstrhdStatus(targetOstrhd, Strstat.CANCEL.getCode(), userVO); // 입고오더 상태 update(취소)

				if(count == 0) {
					throw new UpdateCheckedException();
				}
			}

			// API를 위한 set
			for(WasnitDTO wasnitDTO : targetWasnitList) {
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
				wasnitDTO.setCncldat(dateFormat.format(new Date()));
				wasnitDTO.setRsncdnm(wasnitDTO.getRsncode());
			}

			// API 호출 - 입고예정 취소
			apiConfig.requestToDC(targetWasnitList, ApiInfo.API_IM_WASNIF_CANCEL, HttpMethod.PATCH);
		}
		return count;
	}

	public int savePassDoe027(InboundCheckDTO<WasnhdDTO, WasnitDTO> requestList){
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		for(WasnitDTO wasnitDTO : requestList.getItemGrid().getUpdateList()) {
			wasnitDTO.setAsnitst(Asnitst.PASS_INSPECTION.getCode());
			count = inboundMapper.updateAsnitstDoe027(wasnitDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}

		// 헤더 상태값도 바꿔주기
		WasnitSeacrhParamDTO serachItemDTO = WasnitSeacrhParamDTO.builder()
																		.eoasnky(requestList.getHeadGrid().getEoasnky())
																		.warekey(requestList.getHeadGrid().getWarekey())
																		.build();
		List<WasnitDTO> asnitstAllList =  inboundMapper.selectWasnitKeyAndItemAll(serachItemDTO, userVO);
		List<String> asnitstList = new ArrayList<>();
		asnitstList.add(Asnitst.PASS_INSPECTION.getCode());
		serachItemDTO.setAsnitstList(asnitstList);
		List<WasnitDTO> asnitstPassList =  inboundMapper.selectWasnitKeyAndItem(serachItemDTO, userVO);

		if(asnitstAllList.size() == asnitstPassList.size()) {
			requestList.getHeadGrid().setAsnstat(Asnstat.PASS_INSPECTION.getCode());
		} else {
			requestList.getHeadGrid().setAsnstat(Asnstat.PART_PASS_INSPECTION.getCode());
		}

		count = inboundMapper.updateHeadAsnstatDoe027(requestList.getHeadGrid(), userVO);

		if(count == 0) {
			throw new UpdateCheckedException();
		}

		// API를 위한 set
		for(WasnitDTO item : requestList.getItemGrid().getUpdateList()) {
			item.setVehicky(requestList.getHeadGrid().getVehicky());
		}

		//입고문서생성 및 입고지시
		count = createInboundDocument(requestList.getItemGrid().getUpdateList());

		return count;
	}

	public int saveFailDoe027(InboundCheckDTO<WasnhdDTO,WasnitDTO> requestList){
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		OstritDTO searchOstritDTO = new OstritDTO();
		OstrhdDTO updateOstrhdDTO = new OstrhdDTO();
		String rsncode = requestList.getItemGrid().getUpdateList().get(0).getRsncode();
		String parsnnm = requestList.getItemGrid().getUpdateList().get(0).getParsnnm();
		for(WasnitDTO wasnitDTO : requestList.getItemGrid().getUpdateList()) {
			wasnitDTO.setAsnitst(Asnitst.CANCEL.getCode());
			count = inboundMapper.updateAsnitstDoe027(wasnitDTO, userVO);
			if(count == 0) {
				throw new UpdateCheckedException();
			}

			//수동 -> 입고오더 생성 -> 입고예정정보
			//문서타입으로 갈라치면 이코드
//			if(!DoctypeInbound.POSCO_STRATEGY.getCode().equals(requestList.getHeadGrid().getDoctype()) &&
//					!DoctypeInbound.TOLL_STRATEGY.getCode().equals(requestList.getHeadGrid().getDoctype()) &&
//					!DoctypeInbound.OUTSIDE_STRATEGY.getCode().equals(requestList.getHeadGrid().getDoctype())) {
//				wasnitDTO.setStritst(Asnitst.CANCEL.getCode());
//				count = inboundMapper.updateStritstDoe027(wasnitDTO, userVO);
//				if(count == 0) {
//					throw new UpdateCheckedException();
//				}
//			}
			//입고오더 번호로 갈라칠거면 이코드
			if(wasnitDTO.getCostrky() != null && !wasnitDTO.getCostrky().isBlank()) {

				wasnitDTO.setStritst(Asnitst.CANCEL.getCode());
				count = inboundMapper.updateStritstDoe027(wasnitDTO, userVO);

				 if(count == 0) {
					throw new UpdateCheckedException();
				}
			}
		}

		WasnitSeacrhParamDTO serachWasnitDTO = WasnitSeacrhParamDTO.builder()
																			.eoasnky(requestList.getHeadGrid().getEoasnky())
																			.warekey(requestList.getHeadGrid().getWarekey())
																			.build();

		//입고예정번호의 아이템들이 다 CALCEL로 바뀌었으면 헤더값도 바꿔주기
		//입고 예정 아이템 갯수 == CANCEL된 갯수 -> 헤더값 CANCEL
		List<WasnitDTO> eoasnitAllList = inboundMapper.selectWasnitKeyAndItem(serachWasnitDTO, userVO);

		List<String> asnitstList = new ArrayList<>();
		asnitstList.add(Asnitst.CONFIRMED.getCode());
		serachWasnitDTO.setAsnitstList(asnitstList);
		List<WasnitDTO> eoasnitConfList = inboundMapper.selectWasnitKeyAndItem(serachWasnitDTO, userVO);

		asnitstList.clear();

		asnitstList.add(Asnitst.CANCEL.getCode());
		serachWasnitDTO.setAsnitstList(asnitstList);
		List<WasnitDTO> eoasnitCancelList = inboundMapper.selectWasnitKeyAndItem(serachWasnitDTO, userVO);

		if(!CollectionUtils.isEmpty(eoasnitConfList)) {
			requestList.getHeadGrid().setAsnstat(Asnstat.PART_CANCEL.getCode());
			requestList.getHeadGrid().setRsncode(rsncode);
			requestList.getHeadGrid().setParsnnm(parsnnm);
			count = inboundMapper.updateHeadAsnstatDoe027(requestList.getHeadGrid(), userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}else if(eoasnitAllList.size() == eoasnitCancelList.size()) {
			requestList.getHeadGrid().setAsnstat(Asnstat.CANCEL.getCode());
			count = inboundMapper.updateHeadAsnstatDoe027(requestList.getHeadGrid(), userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}

		//수동 -> 입고오더 생성 -> 입고예정정보
		//입고예정정보 can -> 입고오더 can
		//독타입으로 갈라칠거면 이 코드로
//		if(!DoctypeInbound.POSCO_STRATEGY.getCode().equals(requestList.getHeadGrid().getDoctype()) &&
//				!DoctypeInbound.TOLL_STRATEGY.getCode().equals(requestList.getHeadGrid().getDoctype()) &&
//				!DoctypeInbound.OUTSIDE_STRATEGY.getCode().equals(requestList.getHeadGrid().getDoctype())) {
//			//입고오더 헤더값 cancel하는 로직 입고오더 개수 == 입고오더 취소 개수
//			String costrky = "";
//
//			for(WasnitDTO wasnitDTO : requestList.getItemGrid().getUpdateList()) {
//				costrky = costrky.equals(wasnitDTO.getCostrky()) ? costrky : wasnitDTO.getCostrky();
//				searchOstritDTO.setCostrky(costrky);
//
//				List<OstritDTO> ostritAllList = inboundMapper.getOstritList(searchOstritDTO, userVO);
//
//				searchOstritDTO.setStritst(Asnitst.CANCEL.getCode());
//				List<OstritDTO> ostritCancelList = inboundMapper.getOstritList(searchOstritDTO, userVO);
//
//				if(ostritAllList.size() == ostritCancelList.size()) {
//					updateOstrhdDTO.setCostrky(costrky);
//					updateOstrhdDTO.setStrstat(Asnitst.CANCEL.getCode());
//					count = inboundMapper.updateOstrhdDoe027(updateOstrhdDTO, userVO);
//					if(count == 0) {
//						throw new UpdateCheckedException();
//					}
//				}
//			}
//		}

		//입고 오더 번호로 갈라칠거면 이코드
		for(WasnitDTO wasnitDTO : requestList.getItemGrid().getUpdateList()) {
			if(wasnitDTO.getCostrky() != null && !wasnitDTO.getCostrky().isBlank()) {

				searchOstritDTO.setCostrky(wasnitDTO.getCostrky());

				List<OstritDTO> ostritAllList = inboundMapper.getOstritList(searchOstritDTO, userVO);

				searchOstritDTO.setStritst(Asnitst.CANCEL.getCode());
				List<OstritDTO> ostritCancelList = inboundMapper.getOstritList(searchOstritDTO, userVO);

				if(ostritAllList.size() == ostritCancelList.size()) {
					updateOstrhdDTO.setCostrky(wasnitDTO.getCostrky());
					updateOstrhdDTO.setStrstat(Asnitst.CANCEL.getCode());

					count = inboundMapper.updateOstrhdDoe027(updateOstrhdDTO, userVO);

					if(count == 0) {
						throw new UpdateCheckedException();
					}
				}
			}
		}
		return count;
	}

	// 입고예정정보 입차정보 입력
	public int saveInboundVehicleEntranceInfo(List<WasnhdDTO> saveList){
		if(CollectionUtils.isEmpty(saveList)) {
			throw new NoSaveDataException();
		}
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int updateCnt = inboundMapper.updateWasnhdVehicleEntranceInfo(saveList, userInfo); // 입차정보 입력
		if(updateCnt == 0) {
			throw new UpdateCheckedException();
		}

		//입고 검수대상 확정 자동처리
		saveList.stream().forEach(e -> e.setAsnityn(Useyn.USE.getString())); // 대상 모두 확정
		updateCnt = inboundMapper.updateWasnhdFlagForInspectionTarget(saveList, userInfo); // 플래그 update

		if(updateCnt == 0) {
			throw new UpdateCheckedException();
		}

		return updateCnt;
	}

	// 입고검수대상 확정
	public int setWasnhdFlagForInspectionTarget(List<WasnhdDTO> saveList) {
		if(CollectionUtils.isEmpty(saveList)) {
			throw new NoSaveDataException();
		}
		saveList.stream().forEach(e -> e.setAsnityn(Useyn.USE.getString())); // 대상 모두 확정
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int updateCnt = inboundMapper.updateWasnhdFlagForInspectionTarget(saveList, userInfo); // 플래그 update

		if(updateCnt == 0) {
			throw new UpdateCheckedException();
		}

		return updateCnt; // 검증용
	}

	public List<WasnhdDTO> getDoe033HeadList(WasnhdDTO wasnhdDTO){
		List<String> asnitstList = new ArrayList<>();
		asnitstList.add(Asnstat.PROCEED.getCode());
		wasnhdDTO.setAsnstatList(asnitstList);
		wasnhdDTO.setTasksts(Tasksts.OPERATION_COMPLETE.getCode());

		return inboundMapper.getDoe033HeadList(wasnhdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<WasnitDTO> getDoe033ItemList(WasnitDTO wasnitDTO){
		wasnitDTO.setAsnitst(Asnitst.PROCEED.getCode());

		return inboundMapper.getDoe033ItemList(wasnitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 입고문서 생성 / 지시 등록
	public int createInboundDocument(List<WasnitDTO> saveList) {
		if(CollectionUtils.isEmpty(saveList)) {
			throw new NoSaveDataException();
		}
		ObjectMapper om = new ObjectMapper(); // 변환 util
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo(); // user data
		String asnstat = "";
		// query count 변수
		int wrcvhdCnt = 0;
		int wrcvitCnt = 0;
		int wasnhdCnt = 0;
		int wasnitCnt = 0;
		int wtakitCnt = 0;

		WasnhdDTO asnParam = new WasnhdDTO(); // asn head 조회 param
		asnParam.setEoasnky(saveList.get(0).getEoasnky()); // asn 번호 가져오기
		WasnhdDTO asnHead = inboundMapper.selectPlainWasnhd(asnParam, userInfo); // wasnhd 조회

		if(asnHead == null) {
			throw new UpdateCheckedException();
		}

		asnHead.setRsncode(saveList.get(0).getRsncode());
		asnHead.setRsncdnm(saveList.get(0).getParsnnm());

		boolean physicalCreateDocFlag = DoctypeInbound.PHYSICAL_CREATE.getCode().equals(asnHead.getDoctype());

		//실사입고 (생성)때는 필요없는 로직
		if(!physicalCreateDocFlag) {
			// 전체 아이템 개수 === PASS개수 헤더 상태값 ING로 바뀌게
			// 아니면 헤더 상태값은 PTASS
			WasnitSeacrhParamDTO serachItemDTO = WasnitSeacrhParamDTO.builder()
																			.eoasnky(asnHead.getEoasnky())
																			.warekey(asnHead.getWarekey())
																			.build();

			List<WasnitDTO> asnitstAllList =  inboundMapper.selectWasnitKeyAndItem(serachItemDTO, userInfo);
			List<String> asnitstList = new ArrayList<>();
			asnitstList.add(Asnitst.PASS_INSPECTION.getCode());
			asnitstList.add(Asnitst.CANCEL.getCode());
			asnitstList.add(Asnitst.PROCEED.getCode());
			serachItemDTO.setAsnitstList(asnitstList);
			List<WasnitDTO> asnitstPassList =  inboundMapper.selectWasnitKeyAndItem(serachItemDTO, userInfo);

			if(asnitstAllList.size() == asnitstPassList.size()) {
				asnstat = Asnstat.PROCEED.getCode();
			} else {
				asnstat = Asnstat.PART_PASS_INSPECTION.getCode();
			}

			wasnhdCnt = inboundMapper.updateWasnhdStatus(asnHead, asnstat, userInfo); // 진행으로 변경
			wasnitCnt = inboundMapper.updateWasnitStatus(saveList, Asnitst.PROCEED.getCode(), true, userInfo); // 진행

			if(wasnhdCnt == 0 || wasnitCnt == 0) {
				throw new UpdateCheckedException();
			}
		}

		// 입고문서
		WrcvhdDTO rcvHead = om.convertValue(asnHead, WrcvhdDTO.class); // 조회한 asn을 기반으로 변환
		String rcvdcky = inboundMapper.selectRcvdcky(userInfo); // 입고문서번호 채번
		rcvHead.setRcvdcky(rcvdcky); // 채번한 문서번호 추가
		rcvHead.setRcvdcst(physicalCreateDocFlag ? Rcvdcst.COMPLETE.getCode() : Rcvdcst.NEW.getCode()); // 입고문서 상태
		rcvHead.setVownkey(asnHead.getVownkey());
		rcvHead.setVehicky(asnHead.getVehicky());
		rcvHead.setVhcfnam(asnHead.getVhcfnam());
		rcvHead.setDrvernm(asnHead.getDrvernm());
		rcvHead.setRcvrscd(asnHead.getRsncode());
		rcvHead.setRcvrsnm(asnHead.getRsncdnm());
		wrcvhdCnt = inboundMapper.insertWrcvhd(rcvHead, userInfo); // 입고문서헤더 생성
		if(wrcvhdCnt == 0) {
			throw new InsertCheckedException();
		}

		List<WrcvitDTO> rcvItemList = new ArrayList<>(); // 입고문서 아이템 리스트
		for(int idx=0; idx<saveList.size(); idx++) { // 반영
			int itemKey = idx+1;
			WasnitDTO asn = saveList.get(idx);

			asn.setRcvdcky(rcvdcky); // 문서번호
			asn.setRcvdcit(itemKey); // 문서아이템번호
			WrcvitDTO tmpRcvit = om.convertValue(asn, WrcvitDTO.class); // 변환
			tmpRcvit.setRcvitst(physicalCreateDocFlag ? Rcvitst.COMPLETE.getCode() : Rcvitst.NEW.getCode()); // 문서 상태(신규)
			tmpRcvit.setDoccate(asnHead.getDoccate()); // 문서유형
			tmpRcvit.setDoctype(asnHead.getDoctype()); // 문서타입
			tmpRcvit.setRcvrscd(asn.getRsncode()); // reason code
			tmpRcvit.setRcvrsnm(asn.getRsncdnm()); // reason
			tmpRcvit.setBtrcate(asn.getBtrcate());
			rcvItemList.add(tmpRcvit); // 대입
		}

		wrcvitCnt = inboundMapper.insertWrcvitList(rcvItemList, userInfo); // 입고문서 아이템 생성
		if(wrcvitCnt == 0) {
			throw new InsertCheckedException();
		}

		//하차 포인트별 지시
		List<WasnitDTO> asnSameUllockyList = inboundMapper.getWasnitUllockyGroup(asnHead, userInfo);
		List<WtakitDTO> taskItemList = new ArrayList<>(); // task 리스트
		for(WasnitDTO asnSameUllockyData : asnSameUllockyList) {
			int idx = 0;
			Map<String, Integer> sameToLockyCnt = new HashMap<>();
			String taskoky = inventoryMapper.selectTaskoky(userInfo);  // 문서번호 채번
			List<WasnitDTO> asnUllockyGroupList = inboundMapper.getWasnitUllockyGroupList(asnSameUllockyData, userInfo);

			for(WasnitDTO asnUllockyGroupData : asnUllockyGroupList) {
				WtakitDTO tmpTask = om.convertValue(asnUllockyGroupData, WtakitDTO.class);
				tmpTask.setTaskoky(taskoky); // 작업번호
				tmpTask.setTaskoit(++idx); // 작업아이템번호
				tmpTask.setTasksts(physicalCreateDocFlag ? Tasksts.COMPLETE.getCode() : Tasksts.NEW.getCode()); // 작업상태(신규)
				tmpTask.setLotnmky(" "); // LOT번호(NOT NULL)
				tmpTask.setDoccate(asnHead.getDoccate()); // 문서유형
				tmpTask.setDoctype(asnHead.getDoctype()); // 문서타입
				tmpTask.setTaskdat(DateUtil.getDate("yyyyMMdd")); // 작업날짜 = 입고문서 생성 날짜로
				tmpTask.setFrareky(asnUllockyGroupData.getToareky()); // from 창고동
				tmpTask.setFrlocky(asnUllockyGroupData.getUllocky()); // from 베드(하차포인트)
				tmpTask.setFrlayer(asnUllockyGroupData.getStlayer()); // from layer(차량 단)
				tmpTask.setUllocky(asnUllockyGroupData.getUllocky());
				tmpTask.setParsncd(Inout.IN.getString()); // reason code
				tmpTask.setPhysicalCreateDocFlag(physicalCreateDocFlag);
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

		wtakitCnt = inboundMapper.insertWtakitList(taskItemList, userInfo); // task 생성
		if(wtakitCnt == 0) {
			throw new InsertCheckedException();
		}

		// 실사생성때 호출 X
		if(!physicalCreateDocFlag) {
			// API 호출
			for(WtakitDTO item : taskItemList) {
				item.setVehicky(asnHead.getVehicky());
			}
			apiConfig.requestToDC(taskItemList, ApiInfo.API_IM_WRCVIT, HttpMethod.POST);
		}
		return wrcvhdCnt + wrcvitCnt + wasnhdCnt + wasnitCnt + wtakitCnt; // 검증용
	}

	// 조업시작 벨리데이션
	public int saveDoe033StartOperValidation(WasnhdDTO requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 1;

		// 입고예정번호만 있으면 된다.
		WasnitDTO wasnitDTO = new WasnitDTO();
		wasnitDTO.setWarekey(requestDTO.getWarekey());
		wasnitDTO.setEoasnky(requestDTO.getEoasnky());
		wasnitDTO.setTaskoky(requestDTO.getTaskoky());
		wasnitDTO.setAsnitst("");
		List<WasnitDTO> getWasnitINGList = inboundMapper.getDoe033ItemList(wasnitDTO, userVO);
		// 조업시작리스트
		wasnitDTO.setTasksts(Tasksts.NEW.getCode());
		List<WasnitDTO> getWtakitNEWList = inboundMapper.getDoe033ItemList(wasnitDTO, userVO);

		// 조업 시작이 되엇냐
		if(getWasnitINGList.size() != getWtakitNEWList.size()) {
			throw new TaskProcessingException("ms.operation.startCompleted");
		}

		return count;
	}

	// 조업시작
	public int saveDoe033StartOper(WasnhdDTO requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		WrcvhdDTO wrcvhdDTO = new WrcvhdDTO();
		List<WrcvitDTO> wrcvitList = new ArrayList<>();
		WrcvitDTO wrcvitDTO = new WrcvitDTO();

		//task 리스트 조회
		WasnitDTO paramDTO = new WasnitDTO();
		paramDTO.setWarekey(requestDTO.getWarekey());
		paramDTO.setEoasnky(requestDTO.getEoasnky());
		paramDTO.setTaskoky(requestDTO.getTaskoky());

		List<WasnitDTO> asnItemTaskList = inboundMapper.getDoe033ItemList(paramDTO, userVO);

		for(WasnitDTO wasnitDTO : asnItemTaskList) {
			wasnitDTO.setTasksts(Tasksts.OPERATION_START.getCode());
			count = inboundMapper.saveDoe033WtakitOperStart(wasnitDTO, userVO);

			if(count == 0) {
				throw new InsertCheckedException();
			}

			wrcvhdDTO.setWarekey(requestDTO.getWarekey());
			wrcvhdDTO.setRcvdcky(requestDTO.getRcvdcky());
			count = inboundMapper.updateWrcvhdStatus(wrcvhdDTO, Rcvdcst.PROCEED.getCode(), userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}

			wrcvitDTO.setWarekey(requestDTO.getWarekey());
			wrcvitDTO.setRcvdcky(requestDTO.getRcvdcky());
			wrcvitDTO.setRcvdcit(wasnitDTO.getRcvdcit());

			wrcvitList.add(wrcvitDTO);

			count = inboundMapper.updateWrcvitStatus(wrcvitList, Rcvitst.PROCEED.getCode(), true, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}

		return count;
	}

	// 작업완료 벨리데이션
	public int saveDoe033CmpTaskValidation(WasnhdDTO requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 1;

		// 입고예정번호만 있으면 된다.
		WasnitDTO wasnitDTO = new WasnitDTO();
		wasnitDTO.setWarekey(requestDTO.getWarekey());
		wasnitDTO.setEoasnky(requestDTO.getEoasnky());
		wasnitDTO.setTaskoky(requestDTO.getTaskoky());
		wasnitDTO.setAsnitst("");

		wasnitDTO.setAsnitst(Asnitst.PROCEED.getCode());
		List<WasnitDTO> getWasnitINGList = inboundMapper.getDoe033ItemList(wasnitDTO, userVO);

		List<String> taskstsList = new ArrayList<>();

		taskstsList.add(Tasksts.OPERATION_START.getCode());
		taskstsList.add(Tasksts.TASK_COMPLETE.getCode());
		wasnitDTO.setTaskstsList(taskstsList);
		List<WasnitDTO> getWasnitOPERSTRList = inboundMapper.getDoe033ItemList(wasnitDTO, userVO);

		taskstsList.clear();
		wasnitDTO.setTaskstsList(taskstsList);
		taskstsList.add(Tasksts.TASK_COMPLETE.getCode());
		wasnitDTO.setTaskstsList(taskstsList);
		List<WasnitDTO> getWasnitTASKCMPList = inboundMapper.getDoe033ItemList(wasnitDTO, userVO);
		// 조업 시작이 되엇냐
		if(getWasnitINGList.size() != getWasnitOPERSTRList.size()) {
			throw new TaskProcessingException("ms.operation.startFirst");
		}

		if(getWasnitINGList.size() == getWasnitTASKCMPList.size()) {
			throw new TaskProcessingException("ms.operation.taskCmp");
		}

		return count;
	}

	// 작업완료
	public int saveDoe033CmpTask(GridDTO<WasnitDTO> requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;
		String tolocky = "";
		int layer = 0;

		for(WasnitDTO wasnitDTO : requestDTO.getUpdateList()) {
			wasnitDTO.setStockky(inboundMapper.selectStockky(userVO));
			wasnitDTO.setLotnmky(inboundMapper.selectLotnmky(userVO));

			if(!wasnitDTO.getTolocky().equals(tolocky)) {
				layer = inboundMapper.getDoe033Layer(wasnitDTO, userVO);
				tolocky = wasnitDTO.getTolocky();
			}

			// wstkky insert
			wasnitDTO.setTolayer((++layer));

			//재고유무 확인
			WstkkyDTO hasSameLocStock = inboundMapper.isSameLocationAndLayer(wasnitDTO, userVO);

			if(hasSameLocStock != null) {
				throw new DuplicateLayerException(); //재고존재
			}

			count = inboundMapper.saveDoe033Wstkky(wasnitDTO, userVO);

			if(count == 0) {
				throw new InsertCheckedException();
			}
			// wtakit 작업완료처리
			wasnitDTO.setTasksts(Tasksts.TASK_COMPLETE.getCode());
			count = inboundMapper.saveDoe033Wtakit(wasnitDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}
		return count;
	}

	// 조업완료-벨리데이션
	public int saveDoe033CmpOperValidation(WasnhdDTO requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 1;

		// 입고예정번호만 있으면 된다.
		WasnitDTO wasnitDTO = new WasnitDTO();

		wasnitDTO.setWarekey(requestDTO.getWarekey());
		wasnitDTO.setEoasnky(requestDTO.getEoasnky());
		wasnitDTO.setTaskoky(requestDTO.getTaskoky());
		wasnitDTO.setAsnitst(Asnitst.PROCEED.getCode());
		List<WasnitDTO> getWasnitINGList = inboundMapper.getDoe033ItemList(wasnitDTO, userVO);

		wasnitDTO.setTasksts(Tasksts.OPERATION_START.getCode());
		List<WasnitDTO> getWasnitOPERSTRList = inboundMapper.getDoe033ItemList(wasnitDTO, userVO);

		// 조업 시작일 경우 -> 벨리데이션
		if(getWasnitINGList.size() == getWasnitOPERSTRList.size()) {
			throw new TaskProcessingException("ms.operation.completeFirst");
		}

		wasnitDTO.setTasksts(Tasksts.TASK_COMPLETE.getCode());
		List<WasnitDTO> getWasnitTASKCMPList = inboundMapper.getDoe033ItemList(wasnitDTO, userVO);

		// 작업이 모두 끝났나 확인
		if(getWasnitINGList.size() != getWasnitTASKCMPList.size()) {
			throw new TaskProcessingException("ms.operation.notAllCompleted");
		}

		return count;
	}

	// 조업완료
	public int saveDoe033CmpOper(WasnhdDTO requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		WasnitDTO paramDTO = new WasnitDTO();
		paramDTO.setWarekey(requestDTO.getWarekey());
		paramDTO.setEoasnky(requestDTO.getEoasnky());
		paramDTO.setTaskoky(requestDTO.getTaskoky());

		List<WasnitDTO> asnItemTaskList = inboundMapper.getDoe033ItemList(paramDTO, userVO);

		for(WasnitDTO wasnitDTO : asnItemTaskList) {
			//wtakit 상태값 업데이트 (조업완료 상태값 => 완료 상태값으로 변경 / 자동화 요구사항 반영)
			wasnitDTO.setTasksts(Tasksts.OPERATION_COMPLETE.getCode());
			count = inboundMapper.saveDoe033WtakitOperCmp(wasnitDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}

		// 조업이 모두 완료되었을 경우
		int isAsnTaskAllOperCmp = inboundMapper.isAsnTaskAllOperCmp(requestDTO, userVO);
		if(isAsnTaskAllOperCmp == 0) {
			//입고완료확정 자동화
			List<WrcvhdDTO> wrcvhdList = new ArrayList<>();
			ObjectMapper om = new ObjectMapper();
			wrcvhdList.add(om.convertValue(requestDTO, WrcvhdDTO.class));
			count = saveInboundComplete(wrcvhdList);
		}

		return count;
	}

	// 지시한 입고문서 헤더 조회(신규만)
	public List<WrcvhdDTO> getOrderedReceiveDocumentHeaderList(@RequestBody WrcvhdDTO wrcvhdDTO) {
		List<String> rcvdcstList = new ArrayList<>();
		rcvdcstList.add(Rcvdcst.NEW.getCode());
//		rcvdcstList.add(Rcvdcst.PROCEED.getCode());
		rcvdcstList.add(Rcvdcst.PART_CANCEL.getCode());

		List<String> taskstsList = new ArrayList<>();
		taskstsList.add(Tasksts.NEW.getCode());
		taskstsList.add(Tasksts.CANCEL.getCode());
//		taskstsList.add(Tasksts.OPERATION_START.getCode());
//		taskstsList.add(Tasksts.OPERATION_COMPLETE.getCode());

		wrcvhdDTO.setRcvdcstList(rcvdcstList);
		wrcvhdDTO.setTaskstsList(taskstsList);

		return inboundMapper.selectTaskWrcvhdList(wrcvhdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 지시한 입고문서 아이템 조회
	public List<WrcvitDTO> getOrderedReceiveDocumentItemList(WrcvitDTO wrcvitDTO) {
		return inboundMapper.selectTaskWrcvitList(wrcvitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 조업완료한 입고문서 헤더 조회
	public List<WrcvhdDTO> getCompleteReceiveDocumentHeaderList(WrcvhdDTO wrcvhdDTO) {
//		wrcvhdDTO.setRcvdcst(Rcvdcst.PROCEED.getCode());
//		wrcvhdDTO.setTasksts(Tasksts.OPERATION_COMPLETE.getCode());

		return inboundMapper.selectDoe034TaskWrcvhdList(wrcvhdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 조업완료한 입고문서 아이템 조회
	public List<WrcvitDTO> getCompleteReceiveDocumentItemList(WrcvitDTO wrcvitDTO) {
		return inboundMapper.selectTaskWrcvitList(wrcvitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 지시한 입고문서 헤더 취소
	public int cancelOrderedReceiveDocumentHeaderList(List<WrcvhdDTO> saveList) {
		if(CollectionUtils.isEmpty(saveList)) {
			throw new NoSaveDataException();
		}
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		ObjectMapper om = new ObjectMapper();
		// update count
		int taskUpdateCnt = 0; // WTAKIT
		int documentHeaderUpdateCnt = 0; // WRCVHD
		int documentItemUpdateCnt = 0; // WRCVIT

		List<WasnitDTO> asnitList = new ArrayList<>();
		for(WrcvhdDTO wrcvhd : saveList) {
			wrcvhd.setRsncode(wrcvhd.getRcarscd());
			wrcvhd.setRsncdnm(wrcvhd.getRcarsnm());
			List<WtakitDTO> taskList =  inventoryMapper.selectWtakitListForInbound(om.convertValue(wrcvhd, WtakitDTO.class), userInfo);
			List<WrcvitDTO> rcvList = new ArrayList<>();
			for(WtakitDTO task : taskList) {
				int tmpTaskCnt = inventoryMapper.updateWtakitStatusToRcvdckyAndRcvdcit(task, Tasksts.CANCEL.getCode(), userInfo); // 태스크 상태 업데이트(취소)
				if(tmpTaskCnt == 0) {
					throw new InsertCheckedException();
				}
				taskUpdateCnt += tmpTaskCnt;
				task.setRcarscd(wrcvhd.getRcarscd());
				task.setRcarsnm(wrcvhd.getRcarsnm());
				rcvList.add(om.convertValue(task, WrcvitDTO.class));
				asnitList.add(om.convertValue(task, WasnitDTO.class));
			}

			documentItemUpdateCnt = inboundMapper.updateWrcvitStatus(rcvList, Rcvitst.CANCEL.getCode(), true, userInfo); // rcvit 업데이트(취소)
			if(documentItemUpdateCnt == 0) {
				throw new UpdateCheckedException();
			}
			documentHeaderUpdateCnt = inboundMapper.updateWrcvhdStatus(wrcvhd, Rcvdcst.ALL_CANCEL.getCode(), userInfo);
		}

		// ASN 상태 수정
		int updateWasnitCnt = inboundMapper.updateWasnitStatus(asnitList, Asnitst.PASS_INSPECTION.getCode(), true, userInfo); // asn item 대상 업데이트(Cancel 제외)
		if(updateWasnitCnt == 0) {
			throw new UpdateCheckedException();
		}

		List<Map<String, String>> asnWithRcvList = new ArrayList<>();
		for(WasnitDTO asn : asnitList){
			Map<String, String> tmpAsnit = new HashMap<>();
			tmpAsnit.put("eoasnky", asn.getEoasnky());
			tmpAsnit.put("warekey", asn.getWarekey());
			tmpAsnit.put("rcarscd", asn.getRcarscd());
			tmpAsnit.put("rcarsnm", asn.getRcarsnm());
			asnWithRcvList.add(tmpAsnit);
		}
		asnWithRcvList = asnWithRcvList.stream().distinct().toList();
		List<String> asnitstList = new ArrayList<>();
//		asnitstList.add(Asnitst.PASS_INSPECTION.getCode());
		asnitstList.add(Asnitst.PROCEED.getCode());
		asnitstList.add(Asnitst.CANCEL.getCode());
		asnitstList.add(Asnitst.COMPLETE.getCode());

		for(Map<String, String> asnWithRcv : asnWithRcvList) {
			WasnitSeacrhParamDTO paramForStat = WasnitSeacrhParamDTO.builder()
																	.eoasnky(asnWithRcv.get("eoasnky"))
																	.warekey(asnWithRcv.get("warekey"))
																	.asnitstList(asnitstList)
																	.build();

//			int wasnitPassInspectionStatCnt = 0;
			int wasnitProceedStatCnt = 0;
			int wasnitCancelStatCnt = 0;
			int wasnitCompleteStatCnt = 0;
			List<WasnitDTO> wasnitStatusList = inboundMapper.selectWasnitKeyAndItem(paramForStat, userInfo); // 아이템의 리스트 상태에 따라서 헤더 상태를 업데이트 하기 위해 조회

			for(WasnitDTO wasnit : wasnitStatusList) { // 조건 처리를 위해 상태값 별 카운트
				if(Asnitst.PROCEED.getCode().equals(wasnit.getAsnitst())) {
					wasnitProceedStatCnt++;
				} else if(Asnitst.PASS_INSPECTION.getCode().equals(wasnit.getAsnitst())) {
//					wasnitPassInspectionStatCnt++;
				} else if(Asnitst.CANCEL.getCode().equals(wasnit.getAsnitst())) {
					wasnitCancelStatCnt++;
				} else if(Asnitst.COMPLETE.getCode().equals(wasnit.getAsnitst())) {
					wasnitCompleteStatCnt++;
				}
			}

			WasnhdDTO wasnhd = new WasnhdDTO();
			wasnhd.setEoasnky(paramForStat.getEoasnky());
			wasnhd.setWarekey(paramForStat.getWarekey());
			String asnstat = Asnstat.PROCEED.getCode(); // 혹시 몰라 Default 값으로 set
//			if(wasnitProceedStatCnt > 0) {
//				asnstat = Asnstat.PROCEED.getCode();
//			} else
			if(wasnitProceedStatCnt == 0 && wasnitCancelStatCnt == 0 && wasnitCompleteStatCnt == 0) {
				asnstat = Asnstat.PASS_INSPECTION.getCode();
			} else if(wasnitProceedStatCnt == 0 && wasnitCancelStatCnt > 0 && wasnitCompleteStatCnt == 0) {
				asnstat = Asnstat.PART_PASS_INSPECTION.getCode();
			}

			WasnhdDTO wasnhdDTO = inboundMapper.selectWasnhdKey(wasnhd, userInfo);
			wasnhdDTO.setAsnityn(Useyn.USE.getString());
			wasnhdDTO.setRsncode(asnWithRcv.get("rcarscd"));
			wasnhdDTO.setRsncdnm(asnWithRcv.get("rcarsnm"));
			int updateWasnhdCnt = inboundMapper.updateWasnhdStatus(wasnhdDTO, asnstat, userInfo); // asn 헤더 상태 업데이트
			if(updateWasnhdCnt == 0) {
				throw new UpdateCheckedException();
			}
		}

		// API를 위한 Set
		for(WrcvhdDTO wrcvhd : saveList) {
			wrcvhd.setRsncdnm(wrcvhd.getRcarscd());
		}

		// 입고지시취소 API CALL
		apiConfig.requestToDC(saveList, ApiInfo.API_IM_WRCVIT_CANCEL, HttpMethod.PATCH);

		return taskUpdateCnt + documentHeaderUpdateCnt + documentItemUpdateCnt; // 검증용
	}

	// 지시한 입고문서 아이템 취소
	public int cancelOrderedReceiveDocumentItemList(List<WrcvitDTO> saveList) {
		if(CollectionUtils.isEmpty(saveList)) {
			throw new NoSaveDataException();
		}
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		ObjectMapper om = new ObjectMapper();
		// update count
		int taskUpdateCnt = 0; // WTAKIT
		int documentHeaderUpdateCnt = 0; // WRCVHD
		int documentItemUpdateCnt = 0; // WRCVIT

		List<WasnitDTO> asnitList = new ArrayList<>();
		for(WrcvitDTO rcv : saveList) {
			int tmpTaskCnt = inventoryMapper.updateWtakitStatusToRcvdckyAndRcvdcit(om.convertValue(rcv, WtakitDTO.class), Tasksts.CANCEL.getCode(), userInfo); // 태스크 상태 업데이트(취소)
			asnitList.add(om.convertValue(rcv, WasnitDTO.class));
			if(tmpTaskCnt == 0) {
				throw new UpdateCheckedException();
			}
			taskUpdateCnt += tmpTaskCnt;
		}

		documentItemUpdateCnt = inboundMapper.updateWrcvitStatus(saveList, Rcvitst.CANCEL.getCode(), true, userInfo); // rcvit 업데이트(취소)
		if(documentItemUpdateCnt == 0) {
			throw new UpdateCheckedException();
		}

		WrcvitSeacrhParamDTO paramForStat = WrcvitSeacrhParamDTO.builder()
																									.rcvdcky(saveList.get(0).getRcvdcky())
																									.warekey(saveList.get(0).getWarekey())
																									.build();

		List<WrcvitDTO> wrcvitStatusList = inboundMapper.selectWrcvitStatusList(paramForStat, userInfo); // 아이템의 리스트 상태에 따라서 헤더 상태를 업데이트 하기 위해 조회
		int rcvitNewStatCnt = 0;
		int rcvitCancelStatCnt = 0;
		int rcvitOtherStatCnt = 0;
		for(WrcvitDTO wrcvit : wrcvitStatusList) { // 조건 처리를 위해 상태값 별 카운트
			if(Rcvitst.NEW.getCode().equals(wrcvit.getRcvitst())) {
				rcvitNewStatCnt++;
			} else if(Rcvitst.CANCEL.getCode().equals(wrcvit.getRcvitst())) {
				rcvitCancelStatCnt++;
			} else {
				rcvitOtherStatCnt++;
			}
		}

		if(rcvitOtherStatCnt == 0) {
			WrcvhdDTO wrcvhdParam = om.convertValue(paramForStat, WrcvhdDTO.class);
			if(rcvitNewStatCnt == 0 && rcvitCancelStatCnt == wrcvitStatusList.size()) {
				documentHeaderUpdateCnt = inboundMapper.updateWrcvhdStatus(wrcvhdParam, Rcvdcst.ALL_CANCEL.getCode(), userInfo);
			} else if(rcvitNewStatCnt > 0) {
				documentHeaderUpdateCnt = inboundMapper.updateWrcvhdStatus(wrcvhdParam, Rcvdcst.PART_CANCEL.getCode(), userInfo);
			}
		}

		// ASN 상태 수정
		int updateWasnitCnt = inboundMapper.updateWasnitStatus(asnitList, Asnitst.PASS_INSPECTION.getCode(), true, userInfo); // asn item 대상 업데이트(Cancel 제외)
		if(updateWasnitCnt == 0) {
			throw new UpdateCheckedException();
		}

		List<String> asnitstList = new ArrayList<>();
//		asnitstList.add(Asnitst.PASS_INSPECTION.getCode());
		asnitstList.add(Asnitst.PROCEED.getCode());
		asnitstList.add(Asnitst.CANCEL.getCode());
		asnitstList.add(Asnitst.COMPLETE.getCode());

		WasnitSeacrhParamDTO paramForAsnitStat = WasnitSeacrhParamDTO.builder()
																								.eoasnky(asnitList.get(0).getEoasnky())
																								.warekey(asnitList.get(0).getWarekey())
																								.asnitstList(asnitstList)
																								.build();

//		int wasnitPassInspectionStatCnt = 0;
		int wasnitProceedStatCnt = 0;
		int wasnitCancelStatCnt = 0;
		int wasnitCompleteStatCnt = 0;
		List<WasnitDTO> wasnitStatusList = inboundMapper.selectWasnitKeyAndItem(paramForAsnitStat, userInfo); // 아이템의 리스트 상태에 따라서 헤더 상태를 업데이트 하기 위해 조회

		for(WasnitDTO wasnit : wasnitStatusList) { // 조건 처리를 위해 상태값 별 카운트
			if(Asnitst.PROCEED.getCode().equals(wasnit.getAsnitst())) {
				wasnitProceedStatCnt++;
			} else if(Asnitst.PASS_INSPECTION.getCode().equals(wasnit.getAsnitst())) {
//				wasnitPassInspectionStatCnt++;
			} else if(Asnitst.CANCEL.getCode().equals(wasnit.getAsnitst())) {
				wasnitCancelStatCnt++;
			} else if(Asnitst.COMPLETE.getCode().equals(wasnit.getAsnitst())) {
				wasnitCompleteStatCnt++;
			}
		}

		WasnhdDTO wasnhd = new WasnhdDTO();
		wasnhd.setEoasnky(paramForAsnitStat.getEoasnky());
		wasnhd.setWarekey(paramForAsnitStat.getWarekey());
		String asnstat = Asnstat.PROCEED.getCode(); // 혹시 몰라 Default 값으로 set
//			if(wasnitProceedStatCnt > 0) {
//				asnstat = Asnstat.PROCEED.getCode();
//			} else
		if(wasnitProceedStatCnt == 0 && wasnitCancelStatCnt == 0 && wasnitCompleteStatCnt == 0) {
			asnstat = Asnstat.PASS_INSPECTION.getCode();
		} else if(wasnitProceedStatCnt == 0 && wasnitCancelStatCnt > 0 && wasnitCompleteStatCnt == 0) {
			asnstat = Asnstat.PART_PASS_INSPECTION.getCode();
		}

		int updateWasnhdCnt = inboundMapper.updateWasnhdStatus(inboundMapper.selectWasnhdKey(wasnhd, userInfo), asnstat, userInfo); // asn 헤더 상태 업데이트
		if(updateWasnhdCnt == 0) {
			throw new UpdateCheckedException();
		}

		// 입고지시취소 API CALL
		apiConfig.requestToDC(saveList, ApiInfo.API_IM_WRCVIT_CANCEL, HttpMethod.POST);
//	}
		return taskUpdateCnt + documentHeaderUpdateCnt + documentItemUpdateCnt; // 검증용
	}

	// 입고완료처리
	public int saveInboundComplete(List<WrcvhdDTO> saveList) {
		if(CollectionUtils.isEmpty(saveList)) {
			throw new NoSaveDataException();
		}
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo(); // user data
		ObjectMapper om = new ObjectMapper();

		String rcvdcst = "";
		String asnstat = "";
		String strstat = "";

		// update count
		int taskUpdateCnt = 0; // WTAKIT
		int documentHeaderUpdateCnt = 0; // WRCVHD
		int documentItemUpdateCnt = 0; // WRCVIT
		int asnHeaderUpdateCnt = 0; // WASNHD
		int asnItemUpdateCnt = 0; // WASNIT
		int orderHeaderUpdateCnt = 0; // OSTRHD
		int orderItemUpdateCnt = 0; // OSTRIT

		for(WrcvhdDTO wrcvhd : saveList) {
			WrcvitSeacrhParamDTO wrcvitParam = WrcvitSeacrhParamDTO.builder().rcvdcky(wrcvhd.getRcvdcky()).warekey(wrcvhd.getWarekey()).build();
			List<String> rcvitstList = new ArrayList<>();
			rcvitstList.add(Rcvitst.PROCEED.getCode());
			rcvitstList.add(Rcvitst.RETURN.getCode());
			wrcvitParam.setRcvitstList(rcvitstList);
			List<WrcvitDTO> wrcvitList = inboundMapper.selectWrcvitStatusList(wrcvitParam, userInfo);

			List<WasnitDTO> tmpWasnitList = new ArrayList<>();
			List<OstritDTO> tmpOstritList = new ArrayList<>();
			for(WrcvitDTO wrcvit : wrcvitList) {
				int tmpTaskUpdateCnt = inventoryMapper.updateWtakitStatusToRcvdckyAndRcvdcit(om.convertValue(wrcvit, WtakitDTO.class), Tasksts.COMPLETE.getCode(), userInfo);
				wrcvit.setRchsqty(1);
				tmpWasnitList.add(om.convertValue(wrcvit, WasnitDTO.class));
				if(wrcvit.getCostrky() != null && wrcvit.getCostrit() != null) {
					OstritDTO ostrit = om.convertValue(wrcvit, OstritDTO.class);
					ostrit.setStrcqty(wrcvit.getRchsqty());
					tmpOstritList.add(ostrit);
				}
				if(tmpTaskUpdateCnt == 0) {
					throw new UpdateCheckedException();
				}
				taskUpdateCnt += tmpTaskUpdateCnt;
			}

			int tmpDocItemUpdateCnt = inboundMapper.updateWrcvitStatus(wrcvitList, Rcvitst.COMPLETE.getCode(), true, userInfo);
			if(tmpDocItemUpdateCnt == 0) {
				throw new UpdateCheckedException();
			}
			documentItemUpdateCnt += tmpDocItemUpdateCnt;

			//wasnit 총 아이템 개수 === wrcvit cmp 개수
			//맞으면 CMP 안맞으면 ING
			WasnitSeacrhParamDTO serachItemDTO = WasnitSeacrhParamDTO.builder()
																			.eoasnky(wrcvhd.getEoasnky())
																			.warekey(wrcvhd.getWarekey())
																			.build();

			List<WasnitDTO> asnitstAllList =  inboundMapper.selectWasnitKeyAndItemNotCancel(serachItemDTO, userInfo);

//			List<WrcvitDTO> wrcvitINGList = inboundMapper.selectWrcvitRVCMPList(wrcvitParam, userInfo);

			rcvitstList.clear();
			rcvitstList.add(Rcvitst.COMPLETE.getCode());
			wrcvitParam.setRcvitstList(rcvitstList);
			List<WrcvitDTO> wrcvitCMPList = inboundMapper.selectWrcvitRVCMPList(wrcvitParam, userInfo);

			if(asnitstAllList.size() == wrcvitCMPList.size()) {
				asnstat = Asnstat.COMPLETE.getCode();
				strstat = Strstat.COMPLETE.getCode();
				rcvdcst = Rcvdcst.COMPLETE.getCode();
			} else {
				asnstat = Asnstat.PROCEED.getCode();
				strstat = Strstat.PROCEED.getCode();
				rcvdcst = Rcvdcst.PROCEED.getCode();
			}

			int tmpDocHeaderUpdateCnt = inboundMapper.updateWrcvhdStatus(wrcvhd, rcvdcst, userInfo);
			if(tmpDocHeaderUpdateCnt == 0) {
				throw new UpdateCheckedException();
			}
			documentHeaderUpdateCnt += tmpDocHeaderUpdateCnt;

			int tmpAsnItemUpdateCnt = inboundMapper.updateWasnitStatus(tmpWasnitList, Asnitst.COMPLETE.getCode(), true, userInfo);
			if(tmpAsnItemUpdateCnt == 0) {
				throw new UpdateCheckedException();
			}
			asnItemUpdateCnt += tmpAsnItemUpdateCnt;

			int tmpAsnHeaderUpdateCnt = inboundMapper.updateWasnhdStatus(om.convertValue(tmpWasnitList.get(0), WasnhdDTO.class), asnstat, userInfo);
			if(tmpAsnHeaderUpdateCnt == 0) {
				throw new UpdateCheckedException();
			}
			asnHeaderUpdateCnt += tmpAsnHeaderUpdateCnt;

			if(!tmpOstritList.isEmpty()) {
				int tmpOrderItemUpdateCnt = inboundMapper.updateOstritStatus(tmpOstritList, Stritst.COMPLETE.getCode(), true, userInfo);
				if(tmpOrderItemUpdateCnt == 0) {
					throw new UpdateCheckedException();
				}
				orderItemUpdateCnt += tmpOrderItemUpdateCnt;

				int tmpOrderHeaderUpdateCnt = inboundMapper.updateOstrhdStatus(om.convertValue(tmpOstritList.get(0), OstrhdDTO.class), strstat, userInfo);
				if(tmpOrderHeaderUpdateCnt == 0) {
					throw new UpdateCheckedException();
				}
				orderHeaderUpdateCnt += tmpOrderHeaderUpdateCnt;
			}
		}
		// 입고완료 확정 api
		if(Asnstat.COMPLETE.getCode().equals(asnstat)) {
			apiConfig.requestToDC(saveList, ApiInfo.API_IM_WRCVIT_CMP, HttpMethod.POST);
		}
		return taskUpdateCnt + documentHeaderUpdateCnt + documentItemUpdateCnt + asnHeaderUpdateCnt + asnItemUpdateCnt + orderHeaderUpdateCnt + orderItemUpdateCnt; // 검증용
	}
}
