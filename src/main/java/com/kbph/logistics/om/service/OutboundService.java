package com.kbph.logistics.om.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbph.logistics.api.config.ApiConfig;
import com.kbph.logistics.api.enums.ApiInfo;
import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.domain.HeadItemGridListDTO;
import com.kbph.logistics.common.enums.Useyn;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.error.DeleteCheckedException;
import com.kbph.logistics.configuration.error.InsertCheckedException;
import com.kbph.logistics.configuration.error.NoSaveDataException;
import com.kbph.logistics.configuration.error.TaskProcessingException;
import com.kbph.logistics.configuration.error.UpdateCheckedException;
import com.kbph.logistics.md.domain.MeqlocDTO;
import com.kbph.logistics.md.domain.MlocmaDTO;
import com.kbph.logistics.md.mapper.OrganizationMapper;
import com.kbph.logistics.md.type.Aprovyn;
import com.kbph.logistics.md.type.Doccate;
import com.kbph.logistics.md.type.DoctypeInventory;
import com.kbph.logistics.md.type.DoctypeOutbound;
import com.kbph.logistics.md.type.DoctypeOutboundOrder;
import com.kbph.logistics.md.type.Oboitst;
import com.kbph.logistics.md.type.Obostat;
import com.kbph.logistics.md.type.Phymode;
import com.kbph.logistics.md.type.Phystat;
import com.kbph.logistics.md.type.Shpdcst;
import com.kbph.logistics.md.type.Shpitst;
import com.kbph.logistics.md.type.Tasksts;
import com.kbph.logistics.md.type.Tpistat;
import com.kbph.logistics.md.type.Tpnstat;
import com.kbph.logistics.md.type.Wplitst;
import com.kbph.logistics.md.type.Wplstat;
import com.kbph.logistics.om.domain.OoubhdDTO;
import com.kbph.logistics.om.domain.OoubitDTO;
import com.kbph.logistics.om.domain.OutboundOrderForTreeGridDTO;
import com.kbph.logistics.om.domain.TpwpwgDTO;
import com.kbph.logistics.om.domain.WplnhdDTO;
import com.kbph.logistics.om.domain.WplnitDTO;
import com.kbph.logistics.om.domain.WshphdDTO;
import com.kbph.logistics.om.domain.WshpitDTO;
import com.kbph.logistics.om.mapper.OutboundMapper;
import com.kbph.logistics.sm.domain.WphyitDTO;
import com.kbph.logistics.sm.domain.WstkkyDTO;
import com.kbph.logistics.sm.domain.WtakitDTO;
import com.kbph.logistics.sm.mapper.InventoryMapper;
import com.kbph.logistics.sy.domain.UserVO;
import com.kbph.logistics.tm.domain.TmDispatchDTO;
import com.kbph.logistics.tm.domain.TplnhdDTO;
import com.kbph.logistics.tm.domain.TplnitDTO;

import groovy.lang.Tuple;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutboundService {
	private final OrganizationMapper organizationMapper;
	private final OutboundMapper outboundMapper;
	private final InventoryMapper inventoryMapper;
	private final ApiConfig apiConfig;

	public List<WstkkyDTO> getStockListForShipPlan(WstkkyDTO wstkkyDTO){
		return outboundMapper.selectStockListForShipPlan(wstkkyDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<WstkkyDTO> getTargetStockListForAddStock(WstkkyDTO wstkkyDTO){
		return outboundMapper.selectTargetStockListForAddStock(wstkkyDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<WstkkyDTO> getTargetStockListForChangeInvoice(WstkkyDTO wstkkyDTO){
		return outboundMapper.selectTargetStockListForChangeInvoice(wstkkyDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<WstkkyDTO> doe045StockList(WstkkyDTO wstkkyDTO){
		return outboundMapper.doe045StockList(wstkkyDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 출고계획 생성
	public int saveStandardOutboundPlanList(List<OutboundOrderForTreeGridDTO> outboundOrderList) {
		if(CollectionUtils.isEmpty(outboundOrderList)) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();
		int planHeadResult = 0;
		int planItemResult = 0;
		int stockResult = 0;

		for(OutboundOrderForTreeGridDTO outboundHeadOrderData : outboundOrderList) {
			String shpplky = outboundMapper.selectShpplky(userData);
			outboundHeadOrderData.setShpplky(shpplky);
			outboundHeadOrderData.setShpweig(outboundHeadOrderData.getShpweig());
			outboundHeadOrderData.setRemweig(outboundHeadOrderData.getShpweig());
			outboundHeadOrderData.setWplstat(Wplstat.NEW.getCode());

			planHeadResult = outboundMapper.saveDoe045Wplnhd(outboundHeadOrderData, userData);

			if(planHeadResult == 0) {
				throw new InsertCheckedException();
			}

			if(!CollectionUtils.isEmpty(outboundHeadOrderData.getChildren())) {
				for(OutboundOrderForTreeGridDTO outboundItemOrderData : outboundHeadOrderData.getChildren()) {
					WstkkyDTO wstkkyParam = new WstkkyDTO();
					wstkkyParam.setWarekey(outboundHeadOrderData.getWarekey());
					wstkkyParam.setSkumkey(outboundItemOrderData.getSkumkey());
					wstkkyParam.setStockky(outboundItemOrderData.getStockky());

					WstkkyDTO targetStock = outboundMapper.selectStockForShipPlan(wstkkyParam, userData);
					if(targetStock == null) {
						throw new InsertCheckedException();
					}

					if(!targetStock.getRegimod().equals(outboundItemOrderData.getRegimod())
						&& !targetStock.getDenamlc().equals(outboundItemOrderData.getDenamlc())
						&& !targetStock.getDemdadr().equals(outboundItemOrderData.getDemdadr())){ // 조회한 제품과 출고계획의 목적지, 상세착지명, 상세주소가 같지 않을 경우 예외 발생
						throw new InsertCheckedException();
					}

					outboundItemOrderData.setWarekey(outboundHeadOrderData.getWarekey());
					outboundItemOrderData.setDoccate(outboundHeadOrderData.getDoccate());
					outboundItemOrderData.setDoctype(outboundHeadOrderData.getDoctype());
					outboundItemOrderData.setShpplky(outboundHeadOrderData.getShpplky());
					outboundItemOrderData.setSkugrky(outboundHeadOrderData.getSkugrky());
					outboundItemOrderData.setShpplit(outboundMapper.selectShpplit(outboundHeadOrderData, userData));
					outboundItemOrderData.setChkdisp(Useyn.UNUSE.getString());
					outboundItemOrderData.setWplitst(Wplitst.NEW.getCode());

					planItemResult = outboundMapper.saveDoe045Wplnit(outboundItemOrderData, targetStock, userData);

					if(planItemResult == 0) {
						throw new InsertCheckedException();
					}

					stockResult = outboundMapper.updateStockSallqty(outboundItemOrderData, userData); // 할당수량 + 계획수량 변경

					if(stockResult == 0) {
						throw new UpdateCheckedException();
					}
				}
			}
		}

		return (planHeadResult + planItemResult + stockResult);
	}

	// 제품 지정출고
	public int saveDoe045(List<OutboundOrderForTreeGridDTO> outboundOrderList) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		for(OutboundOrderForTreeGridDTO outboundHeadOrderData : outboundOrderList) {
			String shpplky = outboundMapper.selectShpplky(userVO);
			outboundHeadOrderData.setShpplky(shpplky);
			outboundHeadOrderData.setShpweig(outboundHeadOrderData.getPqChildSum().getSkuweig());
			outboundHeadOrderData.setRemweig(outboundHeadOrderData.getPqChildSum().getSkuweig());
			outboundHeadOrderData.setWplstat(Wplstat.NEW.getCode());
			count = outboundMapper.saveDoe045Wplnhd(outboundHeadOrderData, userVO);

			if(count == 0) {
				throw new InsertCheckedException();
			}

			for(OutboundOrderForTreeGridDTO outboundItemOrderData : outboundHeadOrderData.getChildren()) {
				WstkkyDTO wstkkyParam = new WstkkyDTO();
				wstkkyParam.setWarekey(outboundHeadOrderData.getWarekey());
				wstkkyParam.setSkumkey(outboundItemOrderData.getSkumkey());
				wstkkyParam.setStockky(outboundItemOrderData.getStockky());

				WstkkyDTO targetStock = outboundMapper.selectStockForShipPlan(wstkkyParam, userVO);
				if(targetStock == null) {
					throw new InsertCheckedException();
				}

				if(!targetStock.getRegimod().equals(outboundItemOrderData.getRegimod())
						&& !targetStock.getDenamlc().equals(outboundItemOrderData.getDenamlc())
						&& !targetStock.getDemdadr().equals(outboundItemOrderData.getDemdadr())){ // 조회한 제품과 출고계획의 목적지, 상세착지명, 상세주소가 같지 않을 경우 예외 발생
						throw new InsertCheckedException();
				}

				outboundItemOrderData.setWarekey(outboundHeadOrderData.getWarekey());
				outboundItemOrderData.setDoccate(outboundHeadOrderData.getDoccate());
				outboundItemOrderData.setDoctype(outboundHeadOrderData.getDoctype());
				outboundItemOrderData.setShpplky(outboundHeadOrderData.getShpplky());
				outboundItemOrderData.setSkugrky(outboundHeadOrderData.getSkugrky());
				outboundItemOrderData.setChkdisp(Useyn.UNUSE.getString());
				outboundItemOrderData.setWplitst(Wplitst.NEW.getCode());
				outboundItemOrderData.setShpplit(outboundMapper.selectShpplit(outboundHeadOrderData, userVO));

				count = outboundMapper.saveDoe045Wplnit(outboundItemOrderData, targetStock, userVO);

				if(count == 0) {
					throw new InsertCheckedException();
				}
				//재고 -> 할당수량 카운트
				count = outboundMapper.updateStockSallqty(outboundItemOrderData, userVO);

				if(count == 0) {
					throw new UpdateCheckedException();
				}
			}
		}

		return count;
	}

	// 출고예정 헤더 리스트 조회
	public List<WplnhdDTO> getOutboundPlanHeaderList(WplnhdDTO wplnhdDTO){
		return outboundMapper.selectWplnhdList(wplnhdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 출고예정 아이템 리스트 조회
	public List<WplnitDTO> getOutboundPlanItemList(WplnitDTO wplnitDTO){
		return outboundMapper.selectWplnitList(wplnitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 배차된 출고예정 헤더 리스트 조회
	public List<WplnhdDTO> getDispatchedOutboundPlanHeaderList(WplnhdDTO wplnhdDTO){
		return outboundMapper.selectDispatchedWplnhdList(wplnhdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 배차된 출고예정 아이템 리스트 조회
	public List<TplnitDTO> getDispatchedOutboundPlanItemList(TplnhdDTO plnhdDTO){
		return outboundMapper.selectDispatchedWplnitList(plnhdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 출고예정확정(플래그 업데이트)
	public int setDispatchedOutboundPlanConfirm(List<WplnhdDTO> saveList){
		if(CollectionUtils.isEmpty(saveList)) {
			throw new NoSaveDataException();
		}

		//tpwpwg 업데이트 -> 출고예정확정 플래그 업데이트
		//wplnhd 업데이트 -> 상태값 ING로 변경
		//tplnit 조회 -> 해당 shpplky, shpplit 가져와야함
		//해당 배차계획, 출고계획, 배차사이즈에 잡힌 아이템들만 업데이트 해줘야함.
		//wplnit 업데이트 -> 상태값 ING로 변경

		int updateCnt = 0;

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		for(WplnhdDTO wplnhd : saveList) {
			TpwpwgDTO tpwpwg = new TpwpwgDTO();
			tpwpwg.setVhplnky(wplnhd.getVhplnky());
			tpwpwg.setShpplky(wplnhd.getShpplky());
			tpwpwg.setPlnsize(wplnhd.getPlnsize());
			//TPWPWG - 배차계획키, 출고계획번호, 배차사이즈 업데이트
			int tpwpwgUpdateCnt = outboundMapper.updateTpwpwgConfirmFlag(tpwpwg, Useyn.USE.getString(), userData);

			updateCnt = tpwpwgUpdateCnt;

			if(tpwpwgUpdateCnt == 0) {
				throw new UpdateCheckedException();
			}

			//SHPPLKY 값으로 헤더 조회
			WplnhdDTO targetWplnhd = outboundMapper.selectWplnhdListForVerification(wplnhd, userData);
			if(targetWplnhd == null) {
				throw new NoSaveDataException();
			}

			//WPLNHD 상태값이 NEW라면 ING로 변경
			if(targetWplnhd.getWplstat().equals(Wplstat.NEW.getCode())) {
				int wplnhdUpdateCnt = 0;
				wplnhdUpdateCnt = outboundMapper.updateWplnhdStatus(targetWplnhd, Wplstat.ING.getCode(), false, userData);
				updateCnt += wplnhdUpdateCnt;
				if(wplnhdUpdateCnt == 0 ) {
					throw new UpdateCheckedException();
				}
			}

			//배차계획번호, 출고계획번호, 배차사이즈, 창고로 tplnit 조회
			//tplnit에 있는 wplnit 데이터만 업데이트 해주기 위해서
			TplnhdDTO tplnitParam = new TplnhdDTO();
			tplnitParam.setShpplky(wplnhd.getShpplky());
			tplnitParam.setWarekey(wplnhd.getWarekey());
			tplnitParam.setVhplnky(wplnhd.getVhplnky());
			tplnitParam.setPlnsize(wplnhd.getPlnsize());
			List<TplnitDTO> tplnitItemList = outboundMapper.selectDispatchedWplnitList(tplnitParam, userData);

			//비어있는지 안비어있는지 체크
			//비어있는 오더들도 존재함.
			if(!tplnitItemList.isEmpty()) {
				for(TplnitDTO tplnitItem : tplnitItemList) {
					//tplnit 데이터에서 배차 취소가 아닌, wplnit 상태도 취소가 아닌 데이터만 업데이트
					//wplnit는 cancel이 아니지만 배차가 cancel인경우는 배차를 취소한 경우.(차주가 거절한 경우시 배차 전체 취소) 이럴 경우 헤더도 cancel이어야 하기 때문에 조회자체가 되면 안됐음.
					//wplnit는 cancel인데 배차가 cancel이 아니다? 데이터 꼬인거임.
					if(!tplnitItem.getTpistat().equals(Wplitst.CANCEL.getCode()) && !tplnitItem.getWplitst().equals(Wplitst.CANCEL.getCode())) {
						WplnitDTO updateParam = new WplnitDTO();
						updateParam.setShpplky(tplnitItem.getShpplky());
						updateParam.setShpplit(tplnitItem.getShpplit());

						int tmpWplnitUpdateCnt = 0;
						tmpWplnitUpdateCnt = outboundMapper.updateWplnitStatus(updateParam, Wplstat.ING.getCode(), userData);// 아이템 상태 업데이트

						updateCnt += tmpWplnitUpdateCnt;

						if(tmpWplnitUpdateCnt == 0) {
							throw new UpdateCheckedException();
						}
					}

					WstkkyDTO wstkky = new WstkkyDTO();
					wstkky.setWarekey(tplnitItem.getWarekey());
					wstkky.setStockky(tplnitItem.getStockky());
					wstkky.setSkumkey(tplnitItem.getSkumkey());

					int tmpWstkkyUpdateCnt = 0;
					tmpWstkkyUpdateCnt = outboundMapper.updateWstkkyAlloc(wstkky, true, true, true, userData);

					updateCnt += tmpWstkkyUpdateCnt;

					if(tmpWstkkyUpdateCnt == 0) {
						throw new UpdateCheckedException();
					}
				}
			}
		}

		return updateCnt; // 검증용
	}

	// 출고예정확정 취소
	public int cancelDispatchedOutboundPlan(List<WplnhdDTO> saveList){
		if(CollectionUtils.isEmpty(saveList)) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();
		int updateCnt = 0;
		int deleteCnt = 0;

		//출고계획이 여러개를 배차 하나로 내렸을 경우 예정데이터도 여러개. (즉, 배차랑 예정은 1대1이 아님)
		//체크박스 체크하면 같이 체크가 되므로 부분 취소는 불가
		//saveList는 헤드 체크 리스트 (WPLNHD 조회 리스트)
		//TPWPWG 삭제 (해당 배차계획번호, 출고예정번호, 배차사이즈 기준으로 삭제)
		//TPLNIT가 존재하면 TPLNIT 취소처리 (존재하는지 체크해야함)
		//취소처리는 배차계획번호, 출고계획번호, 배차사이즈 기준으로 취소처리
		//아이템이 전체 취소값이라면 TPLNHD 상태값도 취소로 변경 => 취소날짜도 들어가야함 (배차계획번호 기준)
		//TPLNIT가 존재하면 => 해당 출고계획번호, 출고아이템으로 WPLNIT 검색 => 존재하지않으면 x
		//WPLNIT 검색해서 해당 데이터들 취소처리
		//WPLNIT가 전체 취소라면 WPLNHD 또한 취소로 변경
		//할당 풀기

		//tpwpwg 삭제
		int tpwpwgDeleteCnt = 0;
		tpwpwgDeleteCnt = outboundMapper.deleteTpwpwgList(saveList, userData);
		deleteCnt += tpwpwgDeleteCnt;

		if(tpwpwgDeleteCnt == 0) {
			throw new DeleteCheckedException();
		}

		for(WplnhdDTO wplnhd : saveList) {
			TplnhdDTO selectParam = new TplnhdDTO();
			selectParam.setWarekey(wplnhd.getWarekey());
			selectParam.setVhplnky(wplnhd.getVhplnky());
			selectParam.setShpplky(wplnhd.getShpplky());
			selectParam.setPlnsize(wplnhd.getPlnsize());

			//tplnit 조회
			//존재하는지 여부 찾으려고 -> 아이템이 없는 경우가 있음
			//그럴 경우 업데이트 치면 에러남
			List<TplnitDTO> selectedTplnitList = outboundMapper.selectDispatchedWplnitList(selectParam, userData);

			if(!selectedTplnitList.isEmpty()) {
				for(TplnitDTO tplnit : selectedTplnitList) {
					// 배차계획 아이템 취소
					int tplnitUpdateCnt = 0;
					tplnitUpdateCnt = outboundMapper.updateTransportPlanItemToCancel(tplnit, userData);
					updateCnt += tplnitUpdateCnt;

					if(tplnitUpdateCnt == 0) {
						throw new UpdateCheckedException();
					}

					//출고계획아이템이 존재하는지는 따로 체크를 안함.
					//배차아이템에 있는 정보를 바탕으로 출고계획아이템 update 쳐주는 것.
					//출고계획아이템이 존재하지 않아서 update가 되지 않는다면 그자체가 오류라서 따로 체크를 안했으나 체크가 필요하다면 추가해서 확인 필요
					//출고계획아이템 취소 - 출고예정번호, 출고예정아이템번호
					int wplnitUpdateCnt = 0;
					WplnitDTO wplnitUpdateParam = new WplnitDTO();
					wplnitUpdateParam.setWarekey(tplnit.getWarekey());
					wplnitUpdateParam.setShpplky(tplnit.getShpplky());
					wplnitUpdateParam.setShpplit(tplnit.getShpplit());
					wplnitUpdateCnt = outboundMapper.updateShipPlanItemToCancel(wplnitUpdateParam, userData);
					updateCnt += wplnitUpdateCnt;

					if(wplnitUpdateCnt == 0) {
						throw new UpdateCheckedException();
					}

					//할당해제
					WstkkyDTO wstkky = new WstkkyDTO();
					wstkky.setWarekey(tplnit.getWarekey());
					wstkky.setStockky(tplnit.getStockky());
					wstkky.setSkumkey(tplnit.getSkumkey());

					int wstkkyUpdateCnt = 0;
					wstkkyUpdateCnt = outboundMapper.updateWstkkyAlloc(wstkky, false, false, false, userData); // 할당해제
					updateCnt += wstkkyUpdateCnt;

					if(wstkkyUpdateCnt == 0) {
						throw new UpdateCheckedException();
					}
				}
			}
		}

		Set<WplnhdDTO.VhplnkyHashSetWrapper> vhplnkySet = saveList.stream()
				.map(dto -> new WplnhdDTO.VhplnkyHashSetWrapper(dto.getVhplnky(), dto.getWarekey()))
				.collect(Collectors.toSet());

		for(WplnhdDTO.VhplnkyHashSetWrapper wplnhdItem : vhplnkySet) {
			int tplnhdUpdateCnt = 0;

			TplnitDTO itemParam = new TplnitDTO();
			itemParam.setVhplnky(wplnhdItem.getVhplnky());
			itemParam.setWarekey(wplnhdItem.getWarekey());
			//배차계획 헤더 취소
			tplnhdUpdateCnt = outboundMapper.updateTransportPlanHeadToCancel(itemParam, userData);
			updateCnt += tplnhdUpdateCnt;

			if(tplnhdUpdateCnt == 0) {
				throw new UpdateCheckedException();
			}
		}

		HashSet<WplnhdDTO> shpplkyList = new HashSet<>(saveList);

		for(WplnhdDTO item : shpplkyList) {
			WplnitDTO wplnit = new WplnitDTO();
			wplnit.setShpplky(item.getShpplky());
			wplnit.setWarekey(item.getWarekey());

			List<WplnitDTO> checkWplnitList = outboundMapper.selectWplnitListForVerification(wplnit, userData);

			WplnhdDTO updateParam = new WplnhdDTO();
			updateParam.setWarekey(item.getWarekey());
			updateParam.setShpplky(item.getShpplky());

			boolean isWplitstCancel = checkWplnitList.stream().allMatch(wplitst -> Wplitst.CANCEL.getCode().equals(wplitst.getWplitst()));

			if(isWplitstCancel) {
				int wplststCancelUpdateCnt = 0;

				updateParam.setWplstat(Wplstat.CANCEL.getCode());

				wplststCancelUpdateCnt = outboundMapper.updateShipPlanHeadToCancel(updateParam, userData);
				updateCnt += wplststCancelUpdateCnt;

				if(wplststCancelUpdateCnt == 0) {
					throw new UpdateCheckedException();
				}
			}
		}

		return (updateCnt + deleteCnt); // 검증용
	}

	// 모든 무게가 배차할당된 출고예정헤더 리스트 조회
	public List<WplnhdDTO> getFullDispatchedOutboundPlanHeaderList(WplnhdDTO wplnhdDTO){
		return outboundMapper.selectFullDispatchedOutboundPlanHeaderList(wplnhdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 배차완료된 출고계획 아이템 리스트 조회
	public List<TplnitDTO> getFullDispatchedOutboundPlanItemList(WplnhdDTO wplnhdDTO){
		return outboundMapper.selectFullDispatchedOutboundPlanItemList(wplnhdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 출고계획 업데이트
	public int saveOutboundPlanForDispatch(List<WplnhdDTO> saveList){
		if(CollectionUtils.isEmpty(saveList)) {
			throw new NoSaveDataException();
		}
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();

		for(WplnhdDTO saveData : saveList) {
			WplnhdDTO verifyingWplnhd = outboundMapper.selectWplnhdListForVerification(saveData, userInfo);

			if(verifyingWplnhd == null) {
				throw new UpdateCheckedException("출고예정 헤더를 찾을 수 없습니다.");
			}
			saveData.setUpdtchk(verifyingWplnhd.getUpdtchk()); // 조회 후 update check 넣어줌

			TpwpwgDTO tpwpwgParam = new TpwpwgDTO();
			tpwpwgParam.setShpplky(saveData.getShpplky());
			List<TpwpwgDTO> verifyingTpwpwg = outboundMapper.selectTpwpwgListForValidation(tpwpwgParam, userInfo);

			if(!verifyingTpwpwg.isEmpty()) { // 하나라도 배차에 배정 되어 있을 경우 계획일자와 시간은 수정 안되게
				saveData.setWpldate(null);
				saveData.setWpltime(null);
			}
		}

		return outboundMapper.updateWplnhdForDispatch(saveList, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 출고마감처리
	public int saveOutboundPlanClose(List<WplnhdDTO> saveList) {
		if(CollectionUtils.isEmpty(saveList)) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();
		int updateHeadCnt = 0;
		int updateItemCnt = 0;

		//sveList는 화면에서 보내는 check List

		// 헤더 update
		for(WplnhdDTO headData : saveList) {
			// 아이템 update
			WplnitDTO wplnitParam = new WplnitDTO();
			wplnitParam.setShpplky(headData.getShpplky());

			//wplnit 데이터 가져옴
			List<WplnitDTO> targetWplnitList = outboundMapper.selectWplnitListForVerification(wplnitParam, userData);

			for(WplnitDTO targetWplnit : targetWplnitList) {
				//배차아이템에서 해당 제품코드, 출고키, 출고아이템번호로 검색했을 때 데이터 있는지 확인
				TplnitDTO tplnit = new TplnitDTO();
				tplnit.setWarekey(targetWplnit.getWarekey());
				tplnit.setShpplky(targetWplnit.getShpplky());
				tplnit.setShpplit(targetWplnit.getShpplit());
				tplnit.setSkumkey(targetWplnit.getSkumkey());

				List<TplnitDTO> tplnitData = outboundMapper.selectTplnitListByDoe049(tplnit, userData);

				//배차된 아이템이 없다면 출고계획 아이템 업데이트
				//배차된 계획은 아이템 업데이트가 되면 안된다.
				if(tplnitData.isEmpty()) {
					//아이템 상태값 cmp로 업데이트
					updateItemCnt += outboundMapper.updateWplnitStatus(targetWplnit, Wplstat.CMP.getCode(), userData);

					if(updateItemCnt == 0) {
						throw new UpdateCheckedException("출고예정 아이템 업데이트 오류");
					}

					WstkkyDTO targetStock = inventoryMapper.hasStock(targetWplnit, userData); // 재고 검색

					if(targetStock == null) {
						throw new NoSaveDataException("재고 오류");
					}

					int wstkkyUpdateCnt = outboundMapper.updateWstkkyAlloc(targetStock, false, false, false, userData); // 할당해제
					if(wstkkyUpdateCnt == 0) {
						throw new UpdateCheckedException("할당해제 오류");
					}
				}
			}

			WplnhdDTO wplnhdParam = new WplnhdDTO();
			wplnhdParam.setShpplky(headData.getShpplky());
			//wplnhd 데이터 가져옴
			WplnhdDTO targetWplnhd = outboundMapper.selectWplnhdListForVerification(wplnhdParam, userData);
			if(targetWplnhd == null) {
				throw new UpdateCheckedException("출고예정 헤더 검색 오류");
			}
			targetWplnhd.setParsnnm(headData.getParsnnm()); // 사유내용 입력

			//wplnhd 상태값이 cmp, cancel이 아닌 데이터만 상태값을 cmp로 업데이트, 잔여중량 0으로 셋팅
			targetWplnhd.setRemweig(0);
			if(outboundMapper.updateWplnhdStatus(targetWplnhd, Wplstat.CMP.getCode(), true, userData) == 0) {
				throw new UpdateCheckedException("출고예정 헤더 업데이트 오류");
			}
			updateHeadCnt++;
		}
		return updateHeadCnt + updateItemCnt;
	}


	// 출고지시 대상 리스트 조회
	public List<OoubhdDTO> getDoe049HeadList(OoubhdDTO ooubhdDTO){
		ooubhdDTO.setTpnstat(Tpnstat.PLAN.getCode());
		ooubhdDTO.setAprovyn(Aprovyn.APPROVAL.getCode());

		return outboundMapper.getDoe049HeadList(ooubhdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 출고지시 대상 아이템 리스트 조회
	public List<OoubitDTO> getDoe049ItemList(OoubitDTO ooubitDTO){
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		ooubitDTO.setTpistat(Tpistat.PLAN.getCode());

		return outboundMapper.getDoe049ItemList(ooubitDTO, userVO);
	}

	// 출고 지시 등록
	public int saveDoe049(List<OoubhdDTO> ooubhdDTOList){
		if(CollectionUtils.isEmpty(ooubhdDTOList)) {
			throw new NoSaveDataException();
		}
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int insertCnt = 0;
		int updateCnt = 0;

		//ooubhdDTOList => 화면단 체크 리스트
		//체크한 데이터는 배차계획번호, 출고계획번호가 중복임.
		//중량테이블 데이터를 JOIN해서 가져온 결과값이므로 값이 여러개.
		//OOUBHD에 INSERT 되는 값은 딱 1개여야함

		//ooubhdDTOList에서 배차계획번호를 중심으로 중복값 걸러내야함. => 배차계획번호, 출고계획번호, 배차사이즈
		//걸러낸 값으로 insert 로직
		//OUTBOKY 채번
		//OOUBHD INSERT
		//TPWPWG 상태값 업데이트
		//TPLNIT 아이템이 존재하면 OOUBIT INSERT

		//OOUBHD INSERT
		//TPWPWG UPDATE
		//OOUBIT INSERT

		List<OoubhdDTO> ooubhdSet = new ArrayList<>();

		for(OoubhdDTO ooubhd : ooubhdDTOList) {
			if(ooubhdSet.stream().filter(s -> s.getVhplnky().equals(ooubhd.getVhplnky())).toList().isEmpty()) {
				ooubhdSet.add(ooubhd);
			}
		}

		for(OoubhdDTO ooubhd : ooubhdSet) {
			//outboky 채번
			String outboky = outboundMapper.selectOutboky(userVO);

			//ooubhd insert
			ooubhd.setOutboky(outboky);
			ooubhd.setDoccate(Doccate.OUTBOUND_ORDER.getCode());
			ooubhd.setDoctype(DoctypeOutboundOrder.GENERAL_SHIPMENT.getCode());
			ooubhd.setObostat(Obostat.NEW.getCode());

			int ooubhdInsertCnt = 0;
			ooubhdInsertCnt = outboundMapper.saveDoe049Ooubhd(ooubhd, userVO);
			insertCnt += ooubhdInsertCnt;

			if(ooubhdInsertCnt == 0) {
				throw new InsertCheckedException();
			}

			//tpwpwg update
			ooubhd.setOoucfyn(Useyn.USE.getString());
			int tpwpwgUpdateCnt = 0;
			tpwpwgUpdateCnt = outboundMapper.updateOoucfyn(ooubhd, userVO);
			updateCnt += tpwpwgUpdateCnt;

			if(tpwpwgUpdateCnt == 0) {
				throw new UpdateCheckedException();
			}

			OoubhdDTO ooubhdParamForTarget = new OoubhdDTO();
			ooubhdParamForTarget.setWarekey(ooubhd.getWarekey());
			ooubhdParamForTarget.setVhplnky(ooubhd.getVhplnky());
			List<OoubitDTO> tplnitItemList = outboundMapper.getDoe049ItemList(ooubhdParamForTarget, userVO);

			if(!tplnitItemList.isEmpty()) {
				for(OoubitDTO itemData : tplnitItemList) {
					itemData.setOboitst(Oboitst.NEW.getCode());
					itemData.setOutboky(outboky);
					itemData.setOutboit(outboundMapper.selectOutboit(ooubhd, userVO));
					itemData.setDoccate(ooubhd.getDoccate());
					itemData.setDoctype(ooubhd.getDoctype());

					WstkkyDTO stockParam = new WstkkyDTO();
					stockParam.setWarekey(itemData.getWarekey());
					stockParam.setSkumkey(itemData.getSkumkey());

					WstkkyDTO targetStock = outboundMapper.selectStockForShipPlan(stockParam, userVO);
					if(targetStock == null) {
						throw new NoSaveDataException("해당 재고가 존재하지 않습니다.");
					}

					MlocmaDTO mlocmaParam = new MlocmaDTO();
					mlocmaParam.setWarekey(targetStock.getWarekey());
					mlocmaParam.setAreakey(targetStock.getAreakey());
					mlocmaParam.setLocakey(targetStock.getLocakey());

					// 설비키 조회
					List<MeqlocDTO> targetLocAndEqu = outboundMapper.selectEquipListForTask(mlocmaParam, userVO);
					if(CollectionUtils.isEmpty(targetLocAndEqu)) {
						throw new NoSaveDataException("제품에 해당하는 지시설비 없음");
					}

					itemData.setEquipky(targetLocAndEqu.get(0).getEquipky()); // 설비키

					int ooubitInsertCnt = 0;
					ooubitInsertCnt = outboundMapper.saveDoe049Ooubit(itemData, targetStock, userVO);
					insertCnt += ooubitInsertCnt;

					if(ooubitInsertCnt == 0) {
						throw new InsertCheckedException("출고오더 아이템 생성 오류");
					}
				}
			}

			OoubhdDTO ooubhdParam = new OoubhdDTO();
			ooubhdParam.setOutboky(outboky);

			apiConfig.requestToDC(outboundMapper.selectOoubhdListForApi(ooubhdParam, userVO), ApiInfo.API_OM_WPLNHD, HttpMethod.POST);
		}

		return insertCnt + updateCnt;
	}

	// 출고지시 취소
	public int saveDoe050(List<OoubhdDTO> requestList){
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		for(OoubhdDTO ooubhdDTO : requestList) {
			ooubhdDTO.setOoucfyn(Useyn.UNUSE.getString()); //TPWPWG N처리
			count = outboundMapper.updateOoucfyn(ooubhdDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}

			//ooubhd 취소
			ooubhdDTO.setObostat(Obostat.CANCEL.getCode());
			count = outboundMapper.updateObostat(ooubhdDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}

			ooubhdDTO.setOboitst(Oboitst.NEW.getCode());
			List<OoubitDTO> ooubitDTONewList = outboundMapper.getDoe050OoubitList(ooubhdDTO, userVO);

			for(OoubitDTO ooubitDTO : ooubitDTONewList) {
				ooubitDTO.setOboitst(Oboitst.CANCEL.getCode());
				count = outboundMapper.updateOboitst(ooubitDTO, userVO);

				if(count == 0) {
					throw new UpdateCheckedException();
				}
			}
		}

		apiConfig.requestToDC(requestList, ApiInfo.API_OM_WPLNHD_CANCEL, HttpMethod.PATCH);

		return count;
	}
	public List<OoubhdDTO> getDoe050HeadList(OoubhdDTO ooubhdDTO){
		ooubhdDTO.setObostat(Obostat.NEW.getCode());
		ooubhdDTO.setEntdoyn(Useyn.UNUSE.getString());

		return outboundMapper.getDoe053HeadList(ooubhdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<OoubitDTO> getDoe050ItemList(OoubitDTO ooubitDTO){
		return outboundMapper.getDoe053ItemList(ooubitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<OoubhdDTO> getDoe051HeadList(OoubhdDTO ooubhdDTO){
		return outboundMapper.getDoe053HeadList(ooubhdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<OoubitDTO> getDoe051ItemList(OoubitDTO ooubitDTO){
		return outboundMapper.getDoe053ItemList(ooubitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 출고대상확정 페이지 헤더 조회
	public List<OoubhdDTO> getDoe053HeadList(OoubhdDTO ooubhdDTO){
		ooubhdDTO.setEntdoyn(Useyn.USE.getString());
		return outboundMapper.getDoe053HeadList(ooubhdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 출고대상확정 페이지 아이템 조회
	public List<OoubitDTO> getDoe053ItemList(OoubitDTO ooubitDTO){
		return outboundMapper.getDoe053ItemList(ooubitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 출고오더의 배차별 출고계획 리스트 조회
	public List<OoubhdDTO> getDoe053ModalHeadList(OoubhdDTO ooubhdDTO){
		return outboundMapper.selectDoe053ModalHeadList(ooubhdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 출고오더의 배차별 출고계획 아이템 리스트 조회
	public List<TplnitDTO> getDoe053ModalItemList(OoubhdDTO ooubhdDTO){
		return outboundMapper.selectDoe053ModalItemList(ooubhdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 출고대상 확정
	public int saveDoe053(List<OoubhdDTO> requestList) {
		if(CollectionUtils.isEmpty(requestList)) {
			throw new NoSaveDataException();
		}

		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;
		String doctype = "";

		List<OoubhdDTO> requestFilterList = requestList.stream().filter(e->Obostat.NEW.getCode().equals(e.getObostat())).toList();
		for(OoubhdDTO ooubhdDTO : requestFilterList) {
			// 대상 출고오더 헤더 재검색(검증용)
			OoubhdDTO targetOoubhd = outboundMapper.selectOoubhdForVerification(ooubhdDTO, userVO);
			if(targetOoubhd == null) {
				throw new NoSaveDataException("출고오더 오류");
			}

			// 대상 출고오더의 아이템 리스트
			List<OoubitDTO> doe053ItemList = outboundMapper.getDoe053ItemList(ooubhdDTO, userVO);

			if(CollectionUtils.isEmpty(doe053ItemList)) {
				throw new NoSaveDataException("출고오더 아이템 오류. 출고오더 대상이 없습니다.");
			}

			//wshphd, it insert (고객사명칭 + 목적지 + 상세착지명 + 상세착지주소 별)
			Map<Tuple<String>, List<OoubitDTO>> sameOwnerRegiDestList = doe053ItemList.stream().collect(Collectors.groupingBy( e ->
				new Tuple<String>(e.getCustnam(), e.getRegimod(), e.getDemodnm(), e.getDemdadr())
			));

			for(Map.Entry<Tuple<String>, List<OoubitDTO>> sameOwnerRegiDest : sameOwnerRegiDestList.entrySet()) {
				String shpdcky = outboundMapper.selectShpdcky(userVO);

				if("".equals(doctype) || !(DoctypeOutboundOrder.GENERAL_SHIPMENT.getCode().equals(doctype) || DoctypeOutboundOrder.URGENT_RESEND.getCode().equals(doctype) || DoctypeOutboundOrder.SCHEDULED_SHIPMENT.getCode().equals(doctype) || DoctypeOutboundOrder.OTHER_SHIPMENT.getCode().equals(doctype))) {
					doctype = ooubhdDTO.getDoctype();
				}
				ooubhdDTO.setShpdcky(shpdcky);
				ooubhdDTO.setShpdcst(Shpdcst.NEW.getCode());
				ooubhdDTO.setDoccate(Doccate.OUTBOUND.getCode());
				ooubhdDTO.setRegikey(sameOwnerRegiDest.getKey().get(1));
				ooubhdDTO.setDoctype(DoctypeOutbound.valueOf(DoctypeOutboundOrder.getDoctypeToCode(doctype).name()).getCode());
				count = outboundMapper.saveDoe049Wshphd(ooubhdDTO, userVO);

				if(count == 0) {
					throw new InsertCheckedException();
				}

				for(OoubitDTO ooubitDTO : sameOwnerRegiDest.getValue()) {
					//wshpit insert
					ooubitDTO.setShpdcky(shpdcky);
					ooubitDTO.setShpitst(Shpitst.NEW.getCode());
					ooubitDTO.setDoccate(Doccate.OUTBOUND.getCode());
					ooubitDTO.setDoctype(DoctypeOutbound.valueOf(DoctypeOutboundOrder.getDoctypeToCode(doctype).name()).getCode());
					ooubitDTO.setShpdcit(outboundMapper.selectShpdcit(shpdcky, userVO));

					WstkkyDTO targetStockParam = new WstkkyDTO();
					targetStockParam.setWarekey(ooubhdDTO.getWarekey());
					targetStockParam.setSkumkey(ooubitDTO.getSkumkey());
					WstkkyDTO targetStockForOoubit = inventoryMapper.hasStock(targetStockParam, userVO); // 검증용 재고 조회
					if(targetStockForOoubit == null) {
						throw new NoSaveDataException("출고문서 아이템 생성시 재고조회 오류");
					}
					count = outboundMapper.saveDoe049Wshpit(ooubitDTO, targetStockForOoubit, userVO);

					if(count == 0) {
						throw new InsertCheckedException();
					}
				}
			}

			//상차 포인트별 지시
			List<OoubitDTO> getOoubitShlockyGroup = outboundMapper.getOoubitShlockyGroup(ooubhdDTO, userVO);
			int tolayer = 1;
			for(OoubitDTO ooubitDTO : getOoubitShlockyGroup) {
				String taskoky = inventoryMapper.selectTaskoky(userVO);
				List<OoubitDTO> oouShlockyGroupList = outboundMapper.getOoubitShlockyGroupList(ooubitDTO, userVO);

				for(OoubitDTO oouShlockyGroupData : oouShlockyGroupList) {
					WstkkyDTO stockInfo = inventoryMapper.hasStock(oouShlockyGroupData, userVO);

					if(stockInfo == null) {
						throw new NoSaveDataException();
					}

					oouShlockyGroupData.setTaskoky(taskoky);
					oouShlockyGroupData.setTaskoit(inventoryMapper.selectTaskoit(taskoky, userVO));
					oouShlockyGroupData.setStockky(stockInfo.getStockky());
					oouShlockyGroupData.setLotnmky(stockInfo.getLotnmky());
					oouShlockyGroupData.setDoccate(Doccate.OUTBOUND.getCode());
					oouShlockyGroupData.setDoctype(DoctypeOutbound.valueOf(DoctypeOutboundOrder.getDoctypeToCode(doctype).name()).getCode());
					oouShlockyGroupData.setTasksts(Tasksts.NEW.getCode());
					oouShlockyGroupData.setTolayer(tolayer++);

					count = outboundMapper.saveDoe049Wtakit(oouShlockyGroupData, userVO);

					if(count == 0) {
						throw new InsertCheckedException();
					}
					//ooubit 진행중으로 상태값 변경
					oouShlockyGroupData.setOboitst(Oboitst.ING.getCode());
					count = outboundMapper.updateOboitst(oouShlockyGroupData, userVO);

					if(count == 0) {
						throw new UpdateCheckedException();
					}
				}
			}
		}

		//진행중으로 상태값 변경
		for(OoubhdDTO ooubhdDTO : requestList) {
			ooubhdDTO.setObostat(Obostat.ING.getCode());
			count = outboundMapper.updateDoe053Entdate(ooubhdDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}

		for(OoubhdDTO ooubhdDTO : requestList) {
			WtakitDTO taskParam = new WtakitDTO();
			taskParam.setWarekey(ooubhdDTO.getWarekey());
			taskParam.setOutboky(ooubhdDTO.getOutboky());

			List<WtakitDTO> taskListForOutbound = inventoryMapper.selectWtakitListForOutbound(taskParam, userVO);

			OoubhdDTO ooubhdVerif = outboundMapper.selectOoubhdForReselect(ooubhdDTO, userVO);
			if(ooubhdVerif == null) {
				throw new NoSaveDataException("출고오더 오류");
			}
			ooubhdVerif.setEquipky(taskListForOutbound.get(0).getEquipky());
			ooubhdVerif.setTpriloc(taskListForOutbound.get(0).getTolocky());
			ooubhdVerif.setSpriloc(taskListForOutbound.get(0).getTolocky());

			List<OoubhdDTO> wrapForApiOoubhd = new ArrayList<>();
			wrapForApiOoubhd.add(ooubhdVerif);

			//DO -> DC API(출고예정오더 수정)
			apiConfig.requestToDC(wrapForApiOoubhd, ApiInfo.API_OM_WPLNHD, HttpMethod.PATCH);
			//DO -> DC API(출고선별지시)
			apiConfig.requestToDC(taskListForOutbound, ApiInfo.API_OM_WTAKIT, HttpMethod.POST);
		}

		return count;
	}

	public int saveDoe053AddStock(HeadItemGridListDTO<OoubhdDTO, WstkkyDTO> headItemGridList) {
		if(headItemGridList == null || CollectionUtils.isEmpty(headItemGridList.getHeadList()) || CollectionUtils.isEmpty(headItemGridList.getItemList())) {
			throw new NoSaveDataException("출고대상 추가 데이터 오류");
		} // head = 오더, item = 재고
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		OoubhdDTO targetOrder = headItemGridList.getHeadList().get(0); // 1개만 존재

		// 재고 데이터 재조회(validation)
		List<WstkkyDTO> targetStockList = outboundMapper.selectTargetStockListForValidation(headItemGridList.getItemList(), targetOrder, userVO);
		if(targetStockList.isEmpty()) {
			throw new NoSaveDataException("재고 데이터 오류");
		}
		int allocStockCnt = 0; // 할당 카운트
		int insertWplnitCnt = 0; // 출고계획 카운트
		int insertTplnitCnt = 0; // 출고계획 카운트
		int insertOoubitCnt = 0; // 출고오더 카운트

		// 출고계획헤더 조회
		WplnhdDTO wplnhdParam = new WplnhdDTO();
		wplnhdParam.setWarekey(targetOrder.getWarekey());
		wplnhdParam.setShpplky(targetOrder.getShpplky());;
		WplnhdDTO targetWplnhd = outboundMapper.selectWplnhdListForVerification(wplnhdParam, userVO);
		if(targetWplnhd == null) {
			throw new NoSaveDataException("출고계획 데이터 오류");
		}

		for(WstkkyDTO targetStock : targetStockList) {
			if(inventoryMapper.updateStockAllocate(targetStock, userVO) == 0) {
				throw new UpdateCheckedException("재고 할당 오류");
			}
			allocStockCnt++;

			MlocmaDTO mlocmaParam = new MlocmaDTO();
			mlocmaParam.setWarekey(targetStock.getWarekey());
			mlocmaParam.setAreakey(targetStock.getAreakey());
			mlocmaParam.setLocakey(targetStock.getLocakey());
			// 설비키 조회
			List<MeqlocDTO> targetLocAndEqu = outboundMapper.selectEquipListForTask(mlocmaParam, userVO);
			if(CollectionUtils.isEmpty(targetLocAndEqu)) {
				throw new NoSaveDataException("해당하는 지시설비 없음");
			}
			targetStock.setEquipky(targetLocAndEqu.get(0).getEquipky());

			if(!targetWplnhd.getOwnerky().equals(targetStock.getOwnerky()) && !targetWplnhd.getRegikey().equals(targetStock.getRegimod()) && !targetWplnhd.getDenamlc().equals(targetStock.getRemodnm()) && !targetWplnhd.getDesaddr().equals(targetStock.getDemdadr())) {
				throw new NoSaveDataException("출고계획의 정보와 일치하지 않는 재고입니다.");
			}
		}

		// 출고계획아이템추가
		insertWplnitCnt = outboundMapper.insertWplnitForAddStock(targetWplnhd, targetStockList, userVO);
		if(insertWplnitCnt == 0) {
			throw new InsertCheckedException("출고계획 아이템 삽입 오류");
		}

		// 배차계획 헤더 조회
		TplnhdDTO tplnhdParam = new TplnhdDTO();
		tplnhdParam.setWarekey(targetOrder.getWarekey());
		tplnhdParam.setVhplnky(targetOrder.getVhplnky());
		TplnhdDTO targetTplnhd = outboundMapper.selectTplnhdForVerification(tplnhdParam, userVO);
		if(targetTplnhd == null) {
			throw new NoSaveDataException("배차계획 데이터 오류");
		}

		targetTplnhd.setShpplky(targetOrder.getShpplky());
		targetTplnhd.setPlnsize(targetOrder.getPlnsize());
		// 배차계획아이템추가
		insertTplnitCnt = outboundMapper.insertTplnitForAddStock(targetTplnhd, targetStockList, userVO);
		if(insertTplnitCnt == 0) {
			throw new InsertCheckedException("배차계획 아이템 삽입 오류");
		}

		// 출고오더 헤더
		OoubhdDTO reSelectTargetOrder = outboundMapper.selectOoubhdForVerification(targetOrder, userVO);
		if(reSelectTargetOrder == null) {
			throw new NoSaveDataException("출고오더 데이터 오류");
		}

		reSelectTargetOrder.setVhplnky(targetOrder.getVhplnky());
		reSelectTargetOrder.setShpplky(targetOrder.getShpplky());
		reSelectTargetOrder.setPlnsize(targetOrder.getPlnsize());
		// 출고오더아이템추가
		insertOoubitCnt = outboundMapper.insertOoubitForAddStock(reSelectTargetOrder, targetStockList, userVO);
		if(insertOoubitCnt == 0) {
			throw new InsertCheckedException("출고오더 아이템 삽입 오류");
		}

		return allocStockCnt + insertWplnitCnt + insertTplnitCnt + insertOoubitCnt;
	}

	public List<OoubhdDTO> getDoe054HeadList(OoubhdDTO ooubhdDTO){
		ooubhdDTO.setObostat(Obostat.NEW.getCode());
		return outboundMapper.getDoe054HeadList(ooubhdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<OoubitDTO> getDoe054ItemList(OoubitDTO ooubitDTO){
		return outboundMapper.getDoe053ItemList(ooubitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public int saveDoe054(List<OoubhdDTO> requestList) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		for(OoubhdDTO ooubhdDTO : requestList) {
			count = outboundMapper.updateEntdate(ooubhdDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}

		return count;
	}

	public List<WtakitDTO> getDoe056HeadList(WtakitDTO wtakitDTO){
		List<String> taskstsList = new ArrayList<>();
		taskstsList.add(Tasksts.NEW.getCode());
		taskstsList.add(Tasksts.OPERATION_START.getCode());
		taskstsList.add(Tasksts.TASK_COMPLETE.getCode());
		wtakitDTO.setTaskstsList(taskstsList);
		wtakitDTO.setDoccate(Doccate.OUTBOUND.getCode());

		return outboundMapper.getDoe056HeadList(wtakitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<WtakitDTO> getDoe056ItemList(WtakitDTO wtakitDTO){
		wtakitDTO.setOboitst(Oboitst.ING.getCode());

		return outboundMapper.getDoe056WtakitList(wtakitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public int saveDoe056StartOperValidation(WtakitDTO requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		List<String> taskstsList = new ArrayList<>();
		List<WtakitDTO> getAllWtakitList = inventoryMapper.getDoe036WtakitList(requestDTO, userVO);

		taskstsList.add(Tasksts.OPERATION_START.getCode());
		requestDTO.setTaskstsList(taskstsList);
		List<WtakitDTO> getOperStartWtakitList = inventoryMapper.getDoe036WtakitTaskstsList(requestDTO, userVO);
		//이미 조업시작
		if(getAllWtakitList.size() == getOperStartWtakitList.size()) {
			throw new TaskProcessingException("ms.operation.startCompleted");
		}
		//작업중
		taskstsList.clear();
		taskstsList.add(Tasksts.TASK_COMPLETE.getCode());
		requestDTO.setTaskstsList(taskstsList);
		List<WtakitDTO> getTaskCmpWtakitList = inventoryMapper.getDoe036WtakitTaskstsList(requestDTO, userVO);

		if(!getTaskCmpWtakitList.isEmpty()) {
			throw new TaskProcessingException("ms.operation.taskStart");
		}
		return count;
	}

	public int saveDoe056StartOper(WtakitDTO requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		requestDTO.setTasksts(Tasksts.OPERATION_START.getCode());
		count = inventoryMapper.updateDoe036WtakitOperStart(requestDTO, userVO);

		if(count == 0) {
			throw new UpdateCheckedException();
		}

		//shpdcky, it 추출
		List<WtakitDTO> wtakitShpdckyAnditList = outboundMapper.getDoe056WtakitShpdckyAnditList(requestDTO, userVO);


		for(WtakitDTO wtakitDTO : wtakitShpdckyAnditList) {
			//wshphd, it 상태값 변경 (ING)
			wtakitDTO.setShpitst(Shpitst.ING.getCode());
			count = outboundMapper.updateShpitst(wtakitDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}

			List<WshpitDTO> doe056TotalList = outboundMapper.getDoe056WshpitStatList(wtakitDTO, userVO);

			wtakitDTO.setShpitst(Shpitst.ING.getCode());
			List<WshpitDTO> doe056IngList = outboundMapper.getDoe056WshpitStatList(wtakitDTO, userVO);

			if(doe056TotalList.size() == doe056IngList.size()) {
				wtakitDTO.setShpdcst(Shpdcst.ING.getCode());
				count = outboundMapper.updateShpdcst(wtakitDTO, userVO);

				if(count == 0) {
					throw new UpdateCheckedException();
				}
			}
		}
		return count;
	}

	public int saveDoe056CmpTaskValidation(WtakitDTO requestDTO){
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 1;

		//아이템 갯수 == 조업시작
		WtakitDTO wtakitParam = new WtakitDTO();
		wtakitParam.setWarekey(requestDTO.getWarekey());
		wtakitParam.setTaskoky(requestDTO.getTaskoky());

		List<WtakitDTO> getTotalItemList = inventoryMapper.getDoe036WtakitList(wtakitParam, userVO);

		List<String> taskstsList = new ArrayList<>();
		taskstsList.add(Tasksts.OPERATION_START.getCode());
		taskstsList.add(Tasksts.TASK_COMPLETE.getCode());
		wtakitParam.setTaskstsList(taskstsList);
		List<WtakitDTO> getOperStartItemList = inventoryMapper.getDoe036WtakitTaskstsList(wtakitParam, userVO);

		taskstsList.clear();
		taskstsList.add(Tasksts.TASK_COMPLETE.getCode());
		wtakitParam.setTaskstsList(taskstsList);

		List<WtakitDTO> getTASKCMPItemList = inventoryMapper.getDoe036WtakitTaskstsList(wtakitParam, userVO);

		if(getTotalItemList.size() != getOperStartItemList.size()) {
			throw new TaskProcessingException("ms.operation.startFirst");
		}

		if(getTotalItemList.size() == getTASKCMPItemList.size()){
			throw new TaskProcessingException("ms.operation.taskCmp");
		}

		return count;
	}

	//작업완료
	public int saveDoe056CmpTask(@RequestBody GridDTO<WtakitDTO> requestDTO){
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		for(WtakitDTO wtakitDTO : requestDTO.getUpdateList()) {
			//wstkky update
			WstkkyDTO getStockInfo = inventoryMapper.hasStock(wtakitDTO, userVO);

			if(getStockInfo == null) {
				throw new NoSaveDataException();
			}

			count = outboundMapper.deleteDoe056Stock(wtakitDTO, userVO);

			if(count == 0) {
				throw new DeleteCheckedException();
			}

			//상차포인트(차량단) getStlayer
			int stlayer = outboundMapper.getDoe056UllockyStlayer(wtakitDTO, userVO);
			wtakitDTO.setTolayer(stlayer + 1);

			//wshpit 출고완료 시간, 출고완료수량 업뎃
			count = outboundMapper.updateDoe056ShpDate(wtakitDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}

			//wtakit 상태값 update
			wtakitDTO.setTasksts(Tasksts.TASK_COMPLETE.getCode());
			count = inventoryMapper.updateDoe036Wtakit(wtakitDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}
		return count;
	}

	public int saveDoe056CmpOperValidation(WtakitDTO requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 1;

		List<String> taskstsList = new ArrayList<>();
		List<WtakitDTO> getTotalItemList = inventoryMapper.getDoe036WtakitList(requestDTO, userVO);

		taskstsList.add(Tasksts.OPERATION_START.getCode());
		requestDTO.setTaskstsList(taskstsList);
		List<WtakitDTO> getOperStartList = inventoryMapper.getDoe036WtakitTaskstsList(requestDTO, userVO);

		//아이템 갯수 == 조업시작 갯수 (조업시작 상태)
		if(getTotalItemList.size() == getOperStartList.size()) {
			throw new TaskProcessingException("ms.operation.completeFirst");
		}

		taskstsList.clear();
		taskstsList.add(Tasksts.TASK_COMPLETE.getCode());
		requestDTO.setTaskstsList(taskstsList);
		List<WtakitDTO> getTaskCmpList = inventoryMapper.getDoe036WtakitTaskstsList(requestDTO, userVO);

		//아이템 갯수 1= 작업완료 갯수(전체 작업완료가 되지않앗다)
		if(getTotalItemList.size() != getTaskCmpList.size()) {
			throw new TaskProcessingException("ms.operation.notAllCompleted");
		}

		return count;
	}

	//조업완료
	public int saveDoe056CmpOper(WtakitDTO requestDTO) {
		int count = 0;
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		ObjectMapper om = new ObjectMapper(); // 변환 util
		List<WstkkyDTO> fromLocationStock = new ArrayList<>();

		//wakit 상태 update
		requestDTO.setTasksts(Tasksts.OPERATION_COMPLETE.getCode());
		count = inventoryMapper.updateDoe036WtakitOperCmp(requestDTO, userVO);

		if(count == 0) {
			throw new UpdateCheckedException();
		}

		requestDTO.setOboitst(Oboitst.ING.getCode());

		List<WtakitDTO> outTaskItemList = outboundMapper.getDoe056WtakitList(requestDTO, userVO);

		for(WtakitDTO outTaskItemData : outTaskItemList) {
			if (fromLocationStock.stream().noneMatch(dto ->
					dto.getFrareky().equals(outTaskItemData.getFrareky()) &&
					dto.getFrlocky().equals(outTaskItemData.getFrlocky()))) {
				fromLocationStock.add(om.convertValue(outTaskItemData, WstkkyDTO.class));
			}
		}

		//from 단 sort
		for(WstkkyDTO wstkkyDTO : fromLocationStock) {
			int fromLayerCount = 1;
			List<WstkkyDTO> fromLocakeyStockList = inventoryMapper.getFromLocationStockInfo(wstkkyDTO, userVO);

			for(WstkkyDTO fromLocakeyStock : fromLocakeyStockList) {
				fromLocakeyStock.setLolayer(fromLayerCount++);
				count = inventoryMapper.updateStockLayerSort(fromLocakeyStock, userVO);

				if(count == 0) {
					throw new UpdateCheckedException();
				}
			}
		}

		return count;
	}

	public List<WshphdDTO> getDoe057HeadList(WshphdDTO wshphdDTO){
		if(wshphdDTO.getShpdcst() != null && wshphdDTO.getShpdcst().equals(Shpdcst.CMP.getCode())) {
			wshphdDTO.setTasksts(Tasksts.SHPOUT.getCode());
		}
		return outboundMapper.getDoe057HeadList(wshphdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<WshpitDTO> getDoe057ItemList(WshpitDTO wshpitDTO){
		return outboundMapper.getDoe057ItemList(wshpitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<WshpitDTO> getDoe057TotalList(WshphdDTO wshphdDTO){
		return outboundMapper.getDoe057TotalList(wshphdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 출고완료확정
	public int saveDoe057(List<WshphdDTO> requestDTO) {
		if(CollectionUtils.isEmpty(requestDTO)) {
			throw new NoSaveDataException();
		}

		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		List<OoubhdDTO> ooubhdListForApi = new ArrayList<>();
		WtakitDTO wtakitDTO = new WtakitDTO();
		OoubhdDTO ooubhdDTO = new OoubhdDTO();
		int count = 0;

		for(WshphdDTO wshphdDTO : requestDTO) {
			// 출고문서 헤더 재조회
			WshphdDTO targetWshphd = outboundMapper.selectWshphdListForVerification(wshphdDTO, userVO);
			if(targetWshphd == null) {
				throw new NoSaveDataException("출고문서헤더 조회 오류");
			}

			// ING가 아니면
			if(!Shpdcst.ING.getCode().equals(targetWshphd.getShpdcst())) {
				throw new UpdateCheckedException("진행중인 문서만 완료처리 할 수 있습니다.");
			}

			 // wshpit 조회
			List<WshpitDTO> targetWshpitList = outboundMapper.selectWshpitListForVerification(wshphdDTO, userVO);
			if(targetWshpitList.isEmpty()) {
				throw new NoSaveDataException("출고문서아이템 조회 오류");
			}

			//wshphd, it 상태 완료
			wtakitDTO.setWarekey(targetWshphd.getWarekey());
			wtakitDTO.setShpdcky(targetWshphd.getShpdcky());

			List<WtakitDTO> taskList = inventoryMapper.selectWtakitListForValidation(wtakitDTO, userVO);
			List<WtakitDTO> noEndTaskList = taskList.stream().filter(
																							e -> Tasksts.NEW.getCode().equals(e.getTasksts())
																							|| Tasksts.OPERATION_START.getCode().equals(e.getTasksts())
																							|| Tasksts.TASK_COMPLETE.getCode().equals(e.getTasksts()))
																					.toList();
			if(!noEndTaskList.isEmpty()) {
				throw new UpdateCheckedException("조업이 완료되지 않은 출고문서 입니다.\n" + "출고문서 번호 : " + wshphdDTO.getShpdcky());
			}

			// 출고문서 아이템 완료
			wtakitDTO.setShpitst(Shpitst.CMP.getCode());
			count = outboundMapper.updateShpitstCMP(wtakitDTO, userVO);
			if(count == 0) {
				throw new UpdateCheckedException("출고문서아이템 상태 변경 오류");
			}

			// 출고문서 완료
			wtakitDTO.setShpdcst(Shpdcst.CMP.getCode());
			count = outboundMapper.updateShpdcstCMP(wtakitDTO, userVO);
			if(count == 0) {
				throw new UpdateCheckedException("출고문서헤더 업데이트 오류");
			}

			// TPWPWG 출고완료확정 Y UPDATE
			wshphdDTO.setShpcfyn(Useyn.USE.getString());
			count = outboundMapper.updateTpwpwgShpcfyn(wshphdDTO, userVO);
			if(count == 0) {
				throw new UpdateCheckedException("배차별 출고 완료확정처리 오류");
			}

			// SHPPLKY로 헤더 조회 후 잔여중량이 0고 예정확정여부 지시여부 완료확정여부 ALL Y인지 확인 후
			WshphdDTO getShipmentCompletionStatus = outboundMapper.checkShipmentCompletionStatus(wshphdDTO, userVO);

			if(getShipmentCompletionStatus != null) {
				//wplnhd, it 상태 완료
				wshphdDTO.setWplstat(Wplstat.CMP.getCode());
				count = outboundMapper.updateWplnhdStat(wshphdDTO, userVO);
				if(count == 0) {
					throw new UpdateCheckedException("출고계획헤더 상태변경 오류");
				}

				wshphdDTO.setWplitst(Wplitst.CMP.getCode());
				count = outboundMapper.updateWplnitStat(wshphdDTO, userVO);
				if(count == 0) {
					throw new UpdateCheckedException("출고계획아이템 상태변경 오류");
				}
			}

			// 배차 아이템 송장발행완료 처리
			count = outboundMapper.updateTransportPlanItemStatus(targetWshpitList, Tpistat.INVCMP.getCode(), userVO);

			// 배차 아이템이 전부 송장발행완료이면
			Integer tplnitCountValidationListCount = outboundMapper.selectTplnitCountValidationList(wshphdDTO, userVO);

			if(tplnitCountValidationListCount == null || tplnitCountValidationListCount == 0) {
				count = outboundMapper.updateTransportPlanHeaderStatus(wshphdDTO, Tpnstat.INVCMP.getCode(), userVO);// 배차 헤더 송장발행완료 처리
			}

			// 작업 출고완료 처리
			for(WtakitDTO targetTask : taskList) {
				count = inventoryMapper.updateWtakitStatusToShpdckyAndShpdcit(targetTask, Tasksts.SHPOUT.getCode(), userVO);
				if(count == 0) {
					throw new UpdateCheckedException("작업 출고완료 처리 오류");
				}
			}

			//ooubhd, it 상태 완료
			count = outboundMapper.updateOboitstCmpToWshpit(targetWshpitList, Oboitst.CMP.getCode(), userVO);
			if(count == 0) {
				throw new UpdateCheckedException("출고오더아이템 상태 업데이트 오류");
			}

//			// ooubit 조회 후 조건부 헤더 완료로 변경.
			ooubhdDTO.setWarekey(wshphdDTO.getWarekey());
			ooubhdDTO.setOutboky(wshphdDTO.getOutboky());
			Integer ooubitCountValidationList = outboundMapper.selectOoubitCountValidationList(ooubhdDTO, userVO);

			if(ooubitCountValidationList == null || ooubitCountValidationList == 0) {
				ooubhdDTO.setObostat(Obostat.CMP.getCode());
				count = outboundMapper.updateObostatCMP(ooubhdDTO, userVO);
				if(count == 0) {
					throw new UpdateCheckedException("출고오더헤더 상태 업데이트 오류");
				}

				ooubhdListForApi.add(ooubhdDTO);
			}
		}

		if(!ooubhdListForApi.isEmpty()) {
			apiConfig.requestToDC(ooubhdListForApi, ApiInfo.API_OM_WSHPIT, HttpMethod.POST);
		}

		return count;
	}

	// `
	public int changeInvoice(GridDTO<WshpitDTO> saveList) {
		if(saveList == null || saveList.getAddList() == null || saveList.getDeleteList() == null) {
			throw new NoSaveDataException();
		}
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();
		List<WphyitDTO> insertPhyStockList = new ArrayList<>(); // for api 재고생성
		List<WphyitDTO> deletePhyStockList = new ArrayList<>(); // for api 재고삭제
		List<WstkkyDTO> updatePhyStockList = new ArrayList<>(); // for api 재고업데이트
		//-- 검증용 변수 --//
		int invoiceInsertCnt = 0; // 송장아이템 추가
		int invoiceDeleteCnt = 0; // 송장 아이템 삭제
		int tplnitInsertCnt = 0; // 배차 아이템 추가
		int tplnitDeleteCnt = 0; // 배차 아이템 삭제
		int alloccateCnt = 0; // 할당
		int reStockCnt = 0; // 재고생성
		int delStockCnt = 0; // 재고삭제
		int insertWphyitCnt = 0; // 실사생성
		int delSkuMasterCnt = 0; // 제품 마스터 삭제
		int insertSkuMasterCnt = 0; // 제품 마스터 생성

		// 송장 추가내용
		for(int idx=0; idx<saveList.getAddList().size(); idx++) {
			WshpitDTO addWshpit = saveList.getAddList().get(idx);
			WshphdDTO insertWshphdParam = new WshphdDTO();
			insertWshphdParam.setWarekey(addWshpit.getWarekey());
			insertWshphdParam.setShpdcky(addWshpit.getShpdcky());
			WshphdDTO insertTargetWshphd = outboundMapper.selectWshphdListForVerification(insertWshphdParam, userData);
			if(insertTargetWshphd == null) {
				throw new NoSaveDataException("출고문서 헤더 조회 오류");
			}
			addWshpit.setVhplnky(insertTargetWshphd.getVhplnky()); // 배차키

			WstkkyDTO addTargetStock = new WstkkyDTO();
			addTargetStock.setWarekey(addWshpit.getWarekey());
			addTargetStock.setSkumkey(addWshpit.getSkumkey());
			List<WstkkyDTO> addStockList = outboundMapper.selectStockListForAddStockToInvoice(addTargetStock, userData); // 추가할 재고 조회
			if(CollectionUtils.isEmpty(addStockList)) {
				throw new NoSaveDataException("재고 조회 오류");
			}

			for(WstkkyDTO targetStock : addStockList) {
				List<WstkkyDTO> updateStockList = outboundMapper.selectStockListForUpdate(targetStock, userData); // 업데이트 할 재고 조회

				// 재고할당
				int tmpAllocateCnt = inventoryMapper.updateStockAllocate(targetStock, userData); // 재고 할당
				if(tmpAllocateCnt == 0) {
					throw new UpdateCheckedException("재고 할당 오류");
				}
				alloccateCnt += tmpAllocateCnt;

				// 송장 아이템 추가
				addWshpit.setDoccate(Doccate.OUTBOUND.getCode());
				addWshpit.setDoctype(DoctypeOutbound.GENERAL_SHIPMENT.getCode());

				int tmpInvoiceInsertCnt = outboundMapper.insertWshpitForChangeInvoice(addWshpit, targetStock, userData); // 출고문서 아이템 삽입
				if(tmpInvoiceInsertCnt == 0) {
					throw new InsertCheckedException("출고문서 아이템 추가오류");
				}
				invoiceInsertCnt += tmpInvoiceInsertCnt;

				// 배차 추가
				int tmpTplnitInsertCnt = outboundMapper.insertTplnitForChangeInvoice(addWshpit, targetStock, userData); // 배차 아이템 삽입
				if(tmpTplnitInsertCnt == 0) {
					throw new InsertCheckedException("배차 아이템 추가오류");
				}
				invoiceInsertCnt += tmpTplnitInsertCnt;

				// 재고 삭제 실사 문서 생성 및 지시
				WphyitDTO wphyitParamForAddShip = new WphyitDTO(); // 조회용
				String physokyForAddShip = inventoryMapper.selectPhysoky(targetStock, userData); // 실사키 채번
				String phygrkyForAddShip = inventoryMapper.selectPhygrky(targetStock, userData); // 그룹키 채번
				wphyitParamForAddShip.setWarekey(targetStock.getWarekey());
				wphyitParamForAddShip.setPhysoky(physokyForAddShip);
				wphyitParamForAddShip.setPhygrky(phygrkyForAddShip);
				wphyitParamForAddShip.setWarekey(targetStock.getWarekey());
				wphyitParamForAddShip.setSkumkey(targetStock.getSkumkey());
				wphyitParamForAddShip.setPhymode(Phymode.BEFORE.getCode());
				wphyitParamForAddShip.setPhystat(Phystat.WORKING.getCode());
				wphyitParamForAddShip.setDoccate(Doccate.STOCK.getCode());
				wphyitParamForAddShip.setDoctype(DoctypeInventory.PHYSICAL_STOCK.getCode());
				wphyitParamForAddShip.setParsncd("ADDSHIP");
				wphyitParamForAddShip.setPhysqty(1);

				int tmpInsertBeforeWphyit = inventoryMapper.insertWphyitForInvoiceChange(wphyitParamForAddShip, userData); // 실사 BEFORE
				if(tmpInsertBeforeWphyit == 0) {
					throw new InsertCheckedException("before 실사문서 생성오류");
				}
				insertWphyitCnt += tmpInsertBeforeWphyit;

				wphyitParamForAddShip.setPhymode(Phymode.AFTER.getCode());
				wphyitParamForAddShip.setRmstkyn(Useyn.USE.getString());
				int tmpInsertAfterWphyit = inventoryMapper.insertWphyitForInvoiceChange(wphyitParamForAddShip, userData); // 실사 AFTER
				if(tmpInsertAfterWphyit == 0) {
					throw new InsertCheckedException("after 실사문서 생성오류");
				}
				insertWphyitCnt += tmpInsertAfterWphyit;

				// 재고삭제
				int tmpDelStockCnt = inventoryMapper.deleteWstkkyForChangeInvoice(addTargetStock, userData); // 재고 삭제
				if(tmpDelStockCnt == 0) {
					throw new UpdateCheckedException("재고 삭제 오류");
				}
				delStockCnt += tmpDelStockCnt;

//				// 제품마스터 삭제
//				int tmpDelSkuMasterCnt = inventoryMapper.deleteMskuwcForPhysical(wphyitParamForAddShip, userData); // 제품 마스터데이터 삭제
//				if(tmpDelSkuMasterCnt == 0) {
//					throw new DeleteCheckedException("제품 삭제 오류");
//				}
//				delSkuMasterCnt += tmpDelSkuMasterCnt;

				// 재고 삭제 실사 문서 완료
				wphyitParamForAddShip.setCompqty(1);
				wphyitParamForAddShip.setParsncd("ADDSHIP"); // 임시

				// before update
				wphyitParamForAddShip.setPhymode(Phymode.BEFORE.getCode());
				List<WphyitDTO> phyBeforeItemList = inventoryMapper.getDoe039ItemList(wphyitParamForAddShip, userData); // Before 실사문서 재조회
				wphyitParamForAddShip.setPhystat(Phystat.CMP.getCode());
				for(WphyitDTO phyItem : phyBeforeItemList) {
					phyItem.setPhystat(Phystat.CMP.getCode());
					tmpInsertBeforeWphyit = inventoryMapper.updateDoe040Wphyit(phyItem, userData); // Before 실사문서 업데이트

					if(tmpInsertBeforeWphyit == 0) {
						throw new UpdateCheckedException("before 실사문서 업데이트 오류");
					}
				}

				// after update
				wphyitParamForAddShip.setPhymode(Phymode.AFTER.getCode());
				wphyitParamForAddShip.setPhystat(Phystat.WORKING.getCode());
				List<WphyitDTO> phyAfterItemList = inventoryMapper.getDoe039ItemList(wphyitParamForAddShip, userData); // after 실사문서 재조회
				wphyitParamForAddShip.setPhystat(Phystat.CMP.getCode());
				for(WphyitDTO phyItem : phyAfterItemList) {
					phyItem.setPhystat(Phystat.CMP.getCode());
					tmpInsertAfterWphyit = inventoryMapper.updateDoe040Wphyit(phyItem, userData); // after 실사문서 업데이트

					if(tmpInsertAfterWphyit == 0) {
						throw new UpdateCheckedException("after 실사문서 업데이트 오류");
					}
					insertWphyitCnt += tmpInsertAfterWphyit;
				}
				updateStockList.forEach(e->e.setLolayer(e.getLolayer()-1));

				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for(WstkkyDTO updateStock : updateStockList) {
					int tmpUpdateStockCnt = inventoryMapper.updateLayerSort(updateStock, userData);
					if(tmpUpdateStockCnt == 0) {
						throw new UpdateCheckedException("재고 단수 정렬 오류");
					}
					updateStock.setPhydttm(dateFormat.format(new Date()));
					updateStock.setRmstkyn(Useyn.UNUSE.getString());
					updateStock.setRetunyn(Useyn.UNUSE.getString());
					updateStock.setStlayer(updateStock.getLolayer());
					updatePhyStockList.add(updateStock);
				}

				List<WphyitDTO> phyAfterItemListForApi = inventoryMapper.getDoe039ItemList(wphyitParamForAddShip, userData); // 재조회
				phyAfterItemListForApi.forEach(e -> {
					e.setPhydttm(dateFormat.format(new Date()));
					e.setRmstkyn(Useyn.USE.getString());
					e.setRetunyn(Useyn.UNUSE.getString());
					deletePhyStockList.add(e);
				});
			}
		}

		// 송장 삭제내용
		for(int idx=0; idx<saveList.getDeleteList().size(); idx++) {
			// 출고 재고 생성
			WshpitDTO deleteWshpit = saveList.getDeleteList().get(idx);
			WshpitDTO targetDeleteWshpit = outboundMapper.selectWshpitListForVerificationItem(deleteWshpit, userData);
			if(targetDeleteWshpit == null) {
				throw new NoSaveDataException("출고아이템 없음");
			}

			WshphdDTO deleteWshphdParam = new WshphdDTO();
			deleteWshphdParam.setWarekey(targetDeleteWshpit.getWarekey());
			deleteWshphdParam.setShpdcky(targetDeleteWshpit.getShpdcky());
			WshphdDTO deleteTargetWshphd = outboundMapper.selectWshphdListForVerification(deleteWshphdParam, userData);
			if(deleteTargetWshphd == null) {
				throw new NoSaveDataException("출고문서 헤더 조회 오류");
			}
			targetDeleteWshpit.setVhplnky(deleteTargetWshphd.getVhplnky()); // 배차키

			MlocmaDTO templocationParam = new MlocmaDTO();
			templocationParam.setWarekey(targetDeleteWshpit.getWarekey());
			templocationParam.setLoctype("TMP"); // 임시

			List<MlocmaDTO> tempLocationList = organizationMapper.selectMlocmaList(templocationParam, userData); // 임시베드 검색
			if(tempLocationList.isEmpty()) { // 임시베드 없으면.
				throw new NoSaveDataException("임시베드가 존재하지 않습니다.");
			}

			MlocmaDTO targetTempLoc = tempLocationList.get(0);
			Integer maxlayer = outboundMapper.selectMaxLayerOfLocation(targetTempLoc, userData);
			if(maxlayer == null) {
				maxlayer = 0;
			}

			targetDeleteWshpit.setFrareky(targetTempLoc.getAreakey());
			targetDeleteWshpit.setFrlocky(targetTempLoc.getLocakey());
			targetDeleteWshpit.setFrlayer(maxlayer + 1);

			int tmpReStockCnt = inventoryMapper.insertWstkkyForChangeInvoice(targetDeleteWshpit, userData);
			if(tmpReStockCnt == 0) {
				throw new InsertCheckedException("재고 생성 오류");
			}
			reStockCnt += tmpReStockCnt;

			// 재고생성 실사 문서 생성 및 지시
			WstkkyDTO wstkkyParamForDeleteShip = new WstkkyDTO();
			wstkkyParamForDeleteShip.setWarekey(targetDeleteWshpit.getWarekey());
			wstkkyParamForDeleteShip.setSkumkey(targetDeleteWshpit.getSkumkey());
			wstkkyParamForDeleteShip.setAreakey(targetTempLoc.getAreakey());
			wstkkyParamForDeleteShip.setLocakey(targetTempLoc.getLocakey());
			wstkkyParamForDeleteShip.setLolayer(maxlayer + 1);

			WstkkyDTO targetStcokForDeleteShip = outboundMapper.selectStockForShipPlan(wstkkyParamForDeleteShip, userData);
			if(targetStcokForDeleteShip == null) {
				throw new NoSaveDataException("출고아이템 없음");
			}

			WphyitDTO wphyitParamForDeleteShip = new WphyitDTO();
			String physokyForDeleteShip = inventoryMapper.selectPhysoky(targetStcokForDeleteShip, userData); // 실사키 채번
			String phygrkyForDeleteShip = inventoryMapper.selectPhysoky(targetStcokForDeleteShip, userData); // 실사 그룹키 채번
			wphyitParamForDeleteShip.setWarekey(targetStcokForDeleteShip.getWarekey());
			wphyitParamForDeleteShip.setPhysoky(physokyForDeleteShip);
			wphyitParamForDeleteShip.setPhygrky(phygrkyForDeleteShip);
			wphyitParamForDeleteShip.setSkumkey(targetStcokForDeleteShip.getSkumkey());
			wphyitParamForDeleteShip.setOwnerky(targetStcokForDeleteShip.getOwnerky());
			wphyitParamForDeleteShip.setSkugrky(targetStcokForDeleteShip.getSkugrky());
			wphyitParamForDeleteShip.setSkudesc(targetStcokForDeleteShip.getSkudesc());
			wphyitParamForDeleteShip.setAddcoky(targetStcokForDeleteShip.getAddcoky());
			wphyitParamForDeleteShip.setSkustat(targetStcokForDeleteShip.getStkstat());
			wphyitParamForDeleteShip.setItemcod(targetStcokForDeleteShip.getItemcod());
			wphyitParamForDeleteShip.setSkuweig(targetStcokForDeleteShip.getSkuweig());
			wphyitParamForDeleteShip.setSkuwidt(targetStcokForDeleteShip.getSkuwidt());
			wphyitParamForDeleteShip.setSkuleng(targetStcokForDeleteShip.getSkuleng());
			wphyitParamForDeleteShip.setSkuthic(targetStcokForDeleteShip.getSkuthic());
			wphyitParamForDeleteShip.setSuomkey(targetStcokForDeleteShip.getSuomkey());
			wphyitParamForDeleteShip.setPhymode(Phymode.BEFORE.getCode());
			wphyitParamForDeleteShip.setPhystat(Phystat.WORKING.getCode());
			wphyitParamForDeleteShip.setDoccate(Doccate.STOCK.getCode());
			wphyitParamForDeleteShip.setDoctype(DoctypeInventory.PHYSICAL_STOCK.getCode());
			wphyitParamForDeleteShip.setSystqty(0);
			wphyitParamForDeleteShip.setPhysqty(1);

			int tmpInsertBeforeWphyit = inventoryMapper.insertWphyitForInvoiceChange(wphyitParamForDeleteShip, userData); // 실사 BEFORE
			if(tmpInsertBeforeWphyit == 0) {
				throw new InsertCheckedException("before 실사문서 생성오류");
			}
			insertWphyitCnt += tmpInsertBeforeWphyit;

			wphyitParamForDeleteShip.setPhymode(Phymode.AFTER.getCode());
			wphyitParamForDeleteShip.setSystqty(1);
			int tmpInsertAfterWphyit = inventoryMapper.insertWphyitForInvoiceChange(wphyitParamForDeleteShip, userData); // 실사 AFTER
			if(tmpInsertAfterWphyit == 0) {
				throw new InsertCheckedException("after 실사문서 생성오류");
			}
			insertWphyitCnt += tmpInsertAfterWphyit;

			// 제품마스터 생성
			int tmpInsertSkuMasterCnt = inventoryMapper.insertMskuwcForPhysical(wphyitParamForDeleteShip, userData);
			if(tmpInsertSkuMasterCnt == 0) {
				throw new InsertCheckedException("제품 마스터 생성 오류");
			}
			insertSkuMasterCnt += tmpInsertSkuMasterCnt;

			// 재고 생성 실사 문서 완료
			wphyitParamForDeleteShip.setCompqty(1);
			wphyitParamForDeleteShip.setParsncd("CANCELSHIP"); // 임시

			// before update
			wphyitParamForDeleteShip.setPhymode(Phymode.BEFORE.getCode());
			List<WphyitDTO> phyBeforeItemList = inventoryMapper.getDoe039ItemList(wphyitParamForDeleteShip, userData); // before 실사 재조회
			wphyitParamForDeleteShip.setPhystat(Phystat.CMP.getCode());
			for(WphyitDTO phyItem : phyBeforeItemList) {
				phyItem.setPhystat(Phystat.CMP.getCode());
				tmpInsertBeforeWphyit = inventoryMapper.updateDoe040Wphyit(phyItem, userData);

				if(tmpInsertBeforeWphyit == 0) {
					throw new UpdateCheckedException("before 실사문서 업데이트 오류");
				}
			}

			// after update
			wphyitParamForDeleteShip.setPhystat(Phystat.WORKING.getCode());
			wphyitParamForDeleteShip.setPhymode(Phymode.AFTER.getCode());
			List<WphyitDTO> phyAfterItemList = inventoryMapper.getDoe039ItemList(wphyitParamForDeleteShip, userData); // after 실사 재조회
			wphyitParamForDeleteShip.setPhystat(Phystat.CMP.getCode());
			for(WphyitDTO phyItem : phyAfterItemList) {
				phyItem.setPhystat(Phystat.CMP.getCode());
				tmpInsertAfterWphyit = inventoryMapper.updateDoe040Wphyit(phyItem, userData);

				if(tmpInsertAfterWphyit == 0) {
					throw new UpdateCheckedException("after 실사문서 업데이트 오류");
				}
			}

			List<WphyitDTO> phyAfterItemListForApi = inventoryMapper.getDoe039ItemList(wphyitParamForDeleteShip, userData); // after 재조회

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			phyAfterItemListForApi.forEach(e ->{
				e.setPhydttm(dateFormat.format(new Date()));
				insertPhyStockList.add(e);
			});

			// 송장 업데이트(삭제)
			int tmpInvoiceDeleteCnt = outboundMapper.deleteWshpitForChangeInvoice(targetDeleteWshpit, userData);
			if(tmpInvoiceDeleteCnt == 0) {
				throw new UpdateCheckedException("송장 삭제 오류");
			}

			// 배차삭제
			int tmpTplnitDeleteCnt = outboundMapper.deleteTplnitForChangeInvoice(targetDeleteWshpit, userData);

			tplnitDeleteCnt += tmpTplnitDeleteCnt;
		}

		if(!insertPhyStockList.isEmpty()) {
			apiConfig.requestToDC(insertPhyStockList, ApiInfo.API_SM_WPHYIT_CREATE, HttpMethod.POST); // 재고생성 insert 실사문서
		}
		if(!updatePhyStockList.isEmpty()) {
			apiConfig.requestToDC(updatePhyStockList, ApiInfo.API_SM_WPHYIT, HttpMethod.PATCH); // 재고수정 단수 update 실사
		}
		if(!deletePhyStockList.isEmpty()) {
			apiConfig.requestToDC(deletePhyStockList, ApiInfo.API_SM_WPHYIT, HttpMethod.PATCH); // 재고삭제 delete 실사문서
		}


		return (invoiceInsertCnt + invoiceDeleteCnt + alloccateCnt + reStockCnt + delStockCnt + insertWphyitCnt + delSkuMasterCnt + insertSkuMasterCnt + tplnitInsertCnt + tplnitDeleteCnt);
	}

	public List<TmDispatchDTO> getFilterLocationList(TmDispatchDTO dispatch) {
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();
		List<TmDispatchDTO> tplnitList = outboundMapper.selectTplnitListValidation(dispatch, userData);

		if(!tplnitList.isEmpty()) {
			dispatch.setStockkyList(tplnitList);
			return outboundMapper.selectOmFilterLocationList(dispatch, userData);
		} else {
			return outboundMapper.selectOmLocationList(dispatch, userData);
		}
	}

	// 운송상태 확인용
	public boolean checkTransportPlanStat(WshphdDTO wshphdDTO) {
		List<TplnhdDTO> targetTmList = outboundMapper.selectTransportPlanStat(wshphdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
		List<TplnhdDTO> invcmpTmList = targetTmList.stream().filter(e -> Tpnstat.INVCMP.getCode().equals(e.getTpnstat())).toList();

		return targetTmList.size() == invcmpTmList.size();
	}
}
