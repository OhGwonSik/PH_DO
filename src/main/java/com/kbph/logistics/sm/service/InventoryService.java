package com.kbph.logistics.sm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbph.logistics.api.config.ApiConfig;
import com.kbph.logistics.api.enums.ApiInfo;
import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.domain.InitDataDTO;
import com.kbph.logistics.common.enums.Useyn;
import com.kbph.logistics.common.util.DateUtil;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.error.DeleteCheckedException;
import com.kbph.logistics.configuration.error.InsertCheckedException;
import com.kbph.logistics.configuration.error.NoSaveDataException;
import com.kbph.logistics.configuration.error.TaskProcessingException;
import com.kbph.logistics.configuration.error.UpdateCheckedException;
import com.kbph.logistics.md.domain.MaremaDTO;
import com.kbph.logistics.md.domain.MdesmaDTO;
import com.kbph.logistics.md.domain.MlocmaDTO;
import com.kbph.logistics.md.domain.MrscmaDTO;
import com.kbph.logistics.md.domain.MskuwcDTO;
import com.kbph.logistics.md.mapper.CodeMapper;
import com.kbph.logistics.md.mapper.OrganizationMapper;
import com.kbph.logistics.md.type.Adjdcst;
import com.kbph.logistics.md.type.Adjitst;
import com.kbph.logistics.md.type.Adjmode;
import com.kbph.logistics.md.type.DoccateInventory;
import com.kbph.logistics.md.type.DoctypeInventory;
import com.kbph.logistics.md.type.Phymode;
import com.kbph.logistics.md.type.Phystat;
import com.kbph.logistics.md.type.Rcvitst;
import com.kbph.logistics.md.type.Tasksts;
import com.kbph.logistics.sm.domain.WadjitDTO;
import com.kbph.logistics.sm.domain.WphyitDTO;
import com.kbph.logistics.sm.domain.WstkkyDTO;
import com.kbph.logistics.sm.domain.WtakitDTO;
import com.kbph.logistics.sm.mapper.InventoryMapper;
import com.kbph.logistics.sy.domain.UserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
	private final InventoryMapper inventoryMapper;
	private final OrganizationMapper organizationMapper;
	private final CodeMapper codeMapper;
	private final ApiConfig apiConfig;

	public InitDataDTO inventoryInit(MaremaDTO maremaDTO, MlocmaDTO mlocmaDTO) {
		InitDataDTO initDataDTO = new InitDataDTO();
		Map<String,Object> initMap = new HashMap<>();
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();

		initMap.put("areakeyList", organizationMapper.selectMaremaSelectBox(maremaDTO, userVO));
		initMap.put("locakeyList", organizationMapper.selectWareAreaLocRelations(mlocmaDTO, userVO));

		initDataDTO.setItem(initMap);
		return initDataDTO;
	}
	public InitDataDTO doe035Init(MrscmaDTO mrscmaDTO) {
		InitDataDTO initDataDTO = new InitDataDTO();
		Map<String,Object> initMap = new HashMap<>();
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();

		initMap.put("parsncdList", codeMapper.selectMrscmaSelectBox(mrscmaDTO, userVO));

		initDataDTO.setItem(initMap);
		return initDataDTO;
	}

	public WstkkyDTO getDoe035Layer(WstkkyDTO wstkkyDTO) {
		return inventoryMapper.getDoe035Layer(wstkkyDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<WstkkyDTO> getWstkkyList(WstkkyDTO wstkkyDTO){
		return inventoryMapper.getWstkkyList(wstkkyDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<WstkkyDTO> getDoe035WstkkyList(WstkkyDTO wstkkyDTO){
		return inventoryMapper.getDoe035WstkkyList(wstkkyDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public int saveDoe035Validation(List<WstkkyDTO> requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 1;

		for(WstkkyDTO wstkkyDTO : requestDTO) {
			// 재고 검증 - updtchk
			int checkStockTran = inventoryMapper.checkDoe035StockVail(wstkkyDTO, userVO);

			if(checkStockTran == 0) {
				throw new TaskProcessingException("ms.updateErr");
			}
			//재고 위 제품이 있는지
			int checkAboveStockExist = inventoryMapper.checkDoe035AboveStockExist(wstkkyDTO, userVO);

			if(checkAboveStockExist != 0) {
				// 선별지시 대상의 위에 있는 제품 선별작업지시 유무 확인
				int checkStockAboveTaskExist = inventoryMapper.checkDoe035AboveTaskExist(wstkkyDTO, userVO);

				if(checkStockAboveTaskExist == 0) {
					// 출고예정확정이 되었는지 확인 후 주석 제거예정
//					int stockAboveOutExist = inventoryMapper.checkDoe035StockAboveOutExist(wstkkyDTO, userVO);
//
//					if(stockAboveOutExist != 0) {
//						return count;
//					}

					throw new TaskProcessingException("ms.stockAboveTarget");
				}
			}
		}

		return count;
	}

	public int saveDoe035(GridDTO<WstkkyDTO> requestDTO) {

		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		Map<String, List<WstkkyDTO>> sameEquipkyList = requestDTO.getUpdateList().stream().collect(Collectors.groupingBy(WstkkyDTO :: getEquipky));

		for(Map.Entry<String, List<WstkkyDTO>> sameEquipky : sameEquipkyList.entrySet()) {
			String taskoky = inventoryMapper.selectTaskoky(userVO);
			Map<String, Integer> sameToLockyCnt = new HashMap<>();
			for(WstkkyDTO wstkkyDTO : sameEquipky.getValue()) {
				wstkkyDTO.setTaskoky(taskoky);
				wstkkyDTO.setTaskoit(inventoryMapper.selectTaskoit(taskoky, userVO));
				wstkkyDTO.setTasksts(Tasksts.NEW.getCode());
				wstkkyDTO.setFordqty(wstkkyDTO.getStotqty());
				wstkkyDTO.setDoccate(DoccateInventory.MOVEMENT.getCode());
				wstkkyDTO.setDoctype(DoctypeInventory.SELECTION.getCode());

				String tolocky = wstkkyDTO.getTolocky();

				if(sameToLockyCnt.containsKey(tolocky)) {
					sameToLockyCnt.put(tolocky, sameToLockyCnt.get(tolocky) + 1);
				}else {
					sameToLockyCnt.put(tolocky, 1);
				}

				wstkkyDTO.setTolayer(sameToLockyCnt.get(tolocky));

				//api용 from set
				wstkkyDTO.setFrareky(wstkkyDTO.getAreakey());
				wstkkyDTO.setFrlocky(wstkkyDTO.getLocakey());
				wstkkyDTO.setFrlayer(wstkkyDTO.getLolayer());
				count = inventoryMapper.saveDoe035List(wstkkyDTO, userVO);

				if(count == 0) {
					throw new InsertCheckedException();
				}

				//할당수량으로 옮기는 작업 -> 변경 시 TASKOKY , TASKOIT 기입
				count = inventoryMapper.updateTaskToSallqty(wstkkyDTO, userVO);

				if(count == 0) {
					throw new UpdateCheckedException();
				}
			}
		}
		//운영 -> 관제 선별지시 API
		apiConfig.requestToDC(requestDTO.getUpdateList(), ApiInfo.API_SM_WTAKIT, HttpMethod.POST);

		return count;
	}

	// doedy035 선별지시
	public List<WstkkyDTO> getDoeDy035WstkkyList(WstkkyDTO wstkkyDTO){
		return inventoryMapper.getDoeDy035WstkkyList(wstkkyDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<WtakitDTO> getDoe036HeadList(WtakitDTO wtakitDTO){
		List<String> taskstsList = new ArrayList<>();
		taskstsList.add(Tasksts.NEW.getCode());
		taskstsList.add(Tasksts.OPERATION_START.getCode());
		taskstsList.add(Tasksts.TASK_COMPLETE.getCode());
		wtakitDTO.setTaskstsList(taskstsList);
		wtakitDTO.setDoctype(DoctypeInventory.SELECTION.getCode());
		return inventoryMapper.getDoe036HeadList(wtakitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<WtakitDTO> getDoe036ItemList(WtakitDTO wtakitDTO){
		return inventoryMapper.getDoe036ItemList(wtakitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// doe036 모달
	public List<WtakitDTO> getDoe036ModalList(WtakitDTO wtakitDTO){
		return inventoryMapper.getDoe036StockInfos(wtakitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public int saveDoe036StartOperValidation(WtakitDTO requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		List<String> taskstsList = new ArrayList<>();
		List<WtakitDTO> getAllWtakitList = inventoryMapper.getDoe036WtakitList(requestDTO, userVO);

		taskstsList.add(Tasksts.OPERATION_START.getCode());
		requestDTO.setTaskstsList(taskstsList);
//		requestDTO.setTasksts(Tasksts.OPERATION_START.getCode());
		List<WtakitDTO> getOperStartWtakitList = inventoryMapper.getDoe036WtakitTaskstsList(requestDTO, userVO);
		//이미 조업시작
		if(getAllWtakitList.size() == getOperStartWtakitList.size()) {
			throw new TaskProcessingException("ms.operation.startCompleted");
		}
		//작업중
		taskstsList.clear();
		taskstsList.add(Tasksts.TASK_COMPLETE.getCode());
		requestDTO.setTaskstsList(taskstsList);
//		requestDTO.setTasksts(Tasksts.TASK_COMPLETE.getCode());
		List<WtakitDTO> getTaskCmpWtakitList = inventoryMapper.getDoe036WtakitTaskstsList(requestDTO, userVO);

		if(!getTaskCmpWtakitList.isEmpty()) {
			throw new TaskProcessingException("ms.operation.taskStart");
		}
		return count;
	}

	public int saveDoe036StartOper(WtakitDTO requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		List<WtakitDTO> getSelectionBeforeLocaList = inventoryMapper.getSelectionBeforeLocaList(requestDTO, userVO);

		for(WtakitDTO SelectionBeforeLocaData : getSelectionBeforeLocaList) {
			count = inventoryMapper.updateSelectionBeforeLoca(SelectionBeforeLocaData, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}

		requestDTO.setTasksts(Tasksts.OPERATION_START.getCode());
		count = inventoryMapper.updateDoe036WtakitOperStart(requestDTO, userVO);

		if(count == 0) {
			throw new UpdateCheckedException();
		}
		return count;
	}

	public int saveDoe036CmpTaskValidation(WtakitDTO requestDTO){
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 1;

		//아이템 갯수 == 조업시작
		WtakitDTO wtakitDTO = new WtakitDTO();
		wtakitDTO.setWarekey(requestDTO.getWarekey());
		wtakitDTO.setTaskoky(requestDTO.getTaskoky());

		List<WtakitDTO> getTotalItemList = inventoryMapper.getDoe036WtakitList(wtakitDTO, userVO);

		List<String> taskstsList = new ArrayList<>();
		taskstsList.add(Tasksts.OPERATION_START.getCode());
		taskstsList.add(Tasksts.TASK_COMPLETE.getCode());
		wtakitDTO.setTaskstsList(taskstsList);
		List<WtakitDTO> getOperStartItemList = inventoryMapper.getDoe036WtakitTaskstsList(wtakitDTO, userVO);

		taskstsList.clear();
		taskstsList.add(Tasksts.TASK_COMPLETE.getCode());
		wtakitDTO.setTaskstsList(taskstsList);
		List<WtakitDTO> getTASKCMPItemList = inventoryMapper.getDoe036WtakitTaskstsList(wtakitDTO, userVO);

		if(getTotalItemList.size() != getOperStartItemList.size()) {
			throw new TaskProcessingException("ms.operation.startFirst");
		}

		if(getTotalItemList.size() == getTASKCMPItemList.size()){
			throw new TaskProcessingException("ms.operation.taskCmp");
		}

		return count;
	}

	//작업완료
	public int saveDoe036CmpTask(GridDTO<WtakitDTO> requestDTO){
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;
		int layer = 0;

		for(WtakitDTO wtakitDTO : requestDTO.getUpdateList()) {
			//wstkky update
			WstkkyDTO getStockInfo = inventoryMapper.hasStock(wtakitDTO, userVO);

			if(getStockInfo == null) {
				throw new NoSaveDataException();
			}

			if(layer == 0) {
				getStockInfo.setAreakey(wtakitDTO.getToareky());
				getStockInfo.setLocakey(wtakitDTO.getTolocky());
				layer = inventoryMapper.getDoe035Layer(getStockInfo, userVO).getLolayer();
			}
			wtakitDTO.setTmpupck(getStockInfo.getUpdtchk());
			wtakitDTO.setTolayer(++layer);
			count = inventoryMapper.updateDoe037Wstkky(wtakitDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
			//wtakit 상태값 update / 실제 절대 단으로 업데이트
			wtakitDTO.setTasksts(Tasksts.TASK_COMPLETE.getCode());
			count = inventoryMapper.updateDoe036Wtakit(wtakitDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}
		return count;
	}

	public int saveDoe036CmpOperValidation(WtakitDTO requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 1;

		List<String> taskstsList = new ArrayList<>();

		List<WtakitDTO> getTotalItemList = inventoryMapper.getDoe036WtakitList(requestDTO, userVO);

//		requestDTO.setTasksts(Tasksts.OPERATION_START.getCode());
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
//		requestDTO.setTasksts(Tasksts.TASK_COMPLETE.getCode());
		List<WtakitDTO> getTaskCmpList = inventoryMapper.getDoe036WtakitTaskstsList(requestDTO, userVO);

		//아이템 갯수 1= 작업완료 갯수(전체 작업완료가 되지않앗다)
		if(getTotalItemList.size() != getTaskCmpList.size()) {
			throw new TaskProcessingException("ms.operation.notAllCompleted");
		}

		return count;
	}

	//조업완료
	public int saveDoe036CmpOper(WtakitDTO requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;
		List<WstkkyDTO> fromLocationStock = new ArrayList<>();
		ObjectMapper om = new ObjectMapper(); // 변환 util

		//선별완료확정 자동화
		requestDTO.setTasksts(Tasksts.COMPLETE.getCode());
		count = inventoryMapper.updateDoe036WtakitOperCmp(requestDTO, userVO);

		if(count == 0) {
			throw new UpdateCheckedException();
		}

		List<WtakitDTO> selectionItemList = inventoryMapper.getDoe036ItemList(requestDTO, userVO);

		for(WtakitDTO selectionItemData : selectionItemList) {

			if (fromLocationStock.stream().noneMatch(dto ->
						dto.getFrareky().equals(selectionItemData.getFrareky()) &&
						dto.getFrlocky().equals(selectionItemData.getFrlocky()))) {
				fromLocationStock.add(om.convertValue(selectionItemData, WstkkyDTO.class));
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


	public List<WtakitDTO> getDoe037WtakitList(WtakitDTO wtakitDTO){
		return inventoryMapper.getDoe037WtakitList(wtakitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	//선별 취소 - 선별 조업시작 전
	public int saveDoe037Cancel(GridDTO<WtakitDTO> requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;
		List<WtakitDTO> apiTaskList = new ArrayList<>();
		for(WtakitDTO wtakitDTO : requestDTO.getUpdateList()) {
			wtakitDTO.setTasksts(Tasksts.NEW.getCode());
			int statusCnt = inventoryMapper.getDoe037StatusCnt(wtakitDTO, userVO);

			// 1이면 NEW , 다른 값이면 다른 상태값
			if(statusCnt != 1) {
				throw new TaskProcessingException("ms.cancelBeforeStart");
			}

			count = inventoryMapper.updateDoe037CancelWstkky(wtakitDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}

			wtakitDTO.setTasksts(Tasksts.CANCEL.getCode());
			count = inventoryMapper.saveDoe037Cancel(wtakitDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}

			// taskoky 중복여부 확인
			boolean isDuplicateTaskoky = apiTaskList.stream()
											.anyMatch(dto -> dto.getTaskoky().equals(wtakitDTO.getTaskoky()));

			if(!isDuplicateTaskoky) {
				apiTaskList.add(wtakitDTO);
			}
		}

		//DO -> DC 선별취소 API CALL
		apiConfig.requestToDC(apiTaskList, ApiInfo.API_SM_WTAKIT_CANCEL, HttpMethod.PATCH);

		return count;
	}

	//선별 확정
	public int saveDoe037(GridDTO<WtakitDTO> requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		for(WtakitDTO wtakitDTO : requestDTO.getUpdateList()) {
			//재고유무 확인
			WstkkyDTO hasStock = inventoryMapper.hasStockToLoc(wtakitDTO, userVO);

			if(hasStock == null) {
				throw new NoSaveDataException();
			}

			//작업테이블 update
			wtakitDTO.setTasksts(Tasksts.COMPLETE.getCode());
			count = inventoryMapper.updateDoe037Wtakit(wtakitDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}
		return count;
	}

	public int saveDoe038(GridDTO<WstkkyDTO> requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;
		String physoky = "";

		for(WstkkyDTO wstkkyDTO : requestDTO.getUpdateList()) {
			//재고 유무 확인
			WstkkyDTO hasStock = inventoryMapper.hasStock(wstkkyDTO, userVO);
			if(hasStock == null) {
				throw new NoSaveDataException();
			}
			//WPHYIT에 insert
			if(physoky.isEmpty()) {
				physoky = inventoryMapper.selectPhysoky(wstkkyDTO, userVO);
			}
			wstkkyDTO.setPhysoky(physoky);
			wstkkyDTO.setPhysoit(inventoryMapper.selectPhysoit(wstkkyDTO, userVO));
			wstkkyDTO.setDoccate(DoccateInventory.STOCK.getCode());
			wstkkyDTO.setDoctype(DoctypeInventory.PHYSICAL_STOCK.getCode());
			wstkkyDTO.setPhystat(Phystat.READY.getCode());
			wstkkyDTO.setPhymode(Phymode.BEFORE.getCode());
			wstkkyDTO.setPhygrky(inventoryMapper.selectPhygrky(wstkkyDTO, userVO));
			count = inventoryMapper.saveDoe038List(wstkkyDTO, userVO);

			if(count == 0) {
				throw new InsertCheckedException();
			}
			//총재고수량 -> 할당수량으로 옮겨두기 및 참고실사문서번호 update
			count = inventoryMapper.updatePhyToSallqty(wstkkyDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}
		return count;
	}

	public List<WphyitDTO> getDoe039WphyitList(WphyitDTO wphyitDTO){
		List<String> phystatList = new ArrayList<>();
		phystatList.add(Phystat.READY.getCode());
		wphyitDTO.setPhystatList(phystatList);
		return inventoryMapper.getDoe039HeadList(wphyitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<WphyitDTO> getDoe039ItemList(WphyitDTO wphyitDTO){
		List<String> phystatList = new ArrayList<>();
		phystatList.add(Phystat.READY.getCode());
		wphyitDTO.setPhystatList(phystatList);
		return inventoryMapper.getDoe039ItemStatusList(wphyitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public int saveDoe039(GridDTO<WphyitDTO> requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;
		for(WphyitDTO wphyitDTO : requestDTO.getUpdateList()) {

			//입고문서번호가 있는 경우 -> 기초재고인 경우 입고문서가 없음.
			//입고문서번호가 없는 경우는 반품여부 체크 못함 -> 벨리데이션
			if(Useyn.USE.getString().equals(wphyitDTO.getRetunyn())
					&& !StringUtils.hasText(wphyitDTO.getRcvdcky())
					) {
				throw new TaskProcessingException(wphyitDTO.getSkumkey() + "ms.noInboundDataForPhysical");
			}

			//실사수량 AFTER 넣어줄때  BEFORE 바꿔주기
			wphyitDTO.setPhysoit(inventoryMapper.selectPhysoit(wphyitDTO, userVO));
			wphyitDTO.setPhystat(Phystat.READY.getCode());
			wphyitDTO.setPhymode(Phymode.AFTER.getCode());
			count = inventoryMapper.saveDoe039List(wphyitDTO, userVO);

			if(count == 0) {
				throw new InsertCheckedException();
			}

			//상태값 UPDATE -> READY => WORKING
			List<WphyitDTO> beforeAfterList = inventoryMapper.getBeforeAfterData(wphyitDTO, userVO);

			if(beforeAfterList.isEmpty()) {
				throw new NoSaveDataException();
			}

			for(WphyitDTO beforeAfterDataDTO : beforeAfterList){
				beforeAfterDataDTO.setPhystat(Phystat.WORKING.getCode());
				count = inventoryMapper.updatePhystat(beforeAfterDataDTO, userVO);

				if(count == 0) {
					throw new UpdateCheckedException();
				}
			}
		}
		return count;
	}

	public List<WphyitDTO> getDoe040HeadList(WphyitDTO wphyitDTO){
		List<String> phystatList = new ArrayList<>();
		phystatList.add(Phystat.WORKING.getCode());
		phystatList.add(Phystat.CMP.getCode());
		wphyitDTO.setPhystatList(phystatList);
		return inventoryMapper.getDoe039HeadList(wphyitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<WphyitDTO> getDoe040ItemList(WphyitDTO wphyitDTO){
		List<String> phystatList = new ArrayList<>();
		phystatList.add(Phystat.WORKING.getCode());
		phystatList.add(Phystat.CMP.getCode());
		wphyitDTO.setPhystatList(phystatList);
		return inventoryMapper.getDoe039ItemStatusList(wphyitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<WstkkyDTO> samePlaceCheck(GridDTO<WphyitDTO> requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();

		List<WstkkyDTO> samePlaceStockList = new ArrayList<>();

		for(WphyitDTO wphyitDTO : requestDTO.getUpdateList()) {
			wphyitDTO.setPhystat(Phystat.WORKING.getCode());
			wphyitDTO.setPhymode(Phymode.AFTER.getCode());
			List<WphyitDTO> phyItemList = inventoryMapper.getDoe039ItemList(wphyitDTO, userVO);

			for(WphyitDTO phyItem : phyItemList) {
				WstkkyDTO samePlaceStock = inventoryMapper.hasPlaceStock(phyItem, userVO);
				//옮길 위치에 재고가 존재하는데 그 재고가 자기 자신일 경우
				if(samePlaceStock != null) {
					boolean isSameStock = phyItem.getStockky().equals(samePlaceStock.getStockky()) &&
							phyItem.getAreakey().equals(samePlaceStock.getAreakey()) &&
							phyItem.getLocakey().equals(samePlaceStock.getLocakey()) &&
							phyItem.getStlayer().equals(samePlaceStock.getLolayer());

					if(!isSameStock) {
						samePlaceStockList.add(samePlaceStock);
					}
				}
			}
		}
		return samePlaceStockList;
	}

	// 재고실사 확정
	public int saveDoe040(GridDTO<WphyitDTO> requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;
		String taskoky = "";
		ObjectMapper om = new ObjectMapper(); // 변환 util
		List<WstkkyDTO> fromLocationStock = new ArrayList<>();
		List<WstkkyDTO> toLocationStock = new ArrayList<>();

		//실사된 from to 베드 리스트 - api용
		List<WstkkyDTO> apiPhyList = new ArrayList<>();

		for(WphyitDTO phyHeadData : requestDTO.getUpdateList()) {
			phyHeadData.setPhystat(Phystat.WORKING.getCode());
			phyHeadData.setPhymode(Phymode.AFTER.getCode());
			List<WphyitDTO> phyItemList = inventoryMapper.getDoe039ItemList(phyHeadData, userVO);

			for(WphyitDTO wphyitDTO : phyItemList) {

				if("Y".equals(wphyitDTO.getRmstkyn()) || "Y".equals(wphyitDTO.getRetunyn())) {
					//제품 삭제 로직
					count = inventoryMapper.deleteWstkkyForPhysical(wphyitDTO, userVO);

					if(count == 0) {
						throw new DeleteCheckedException();
					}

					count = inventoryMapper.deleteMskuwcForPhysical(wphyitDTO, userVO);

					if(count == 0) {
						throw new DeleteCheckedException();
					}

					if("Y".equals(wphyitDTO.getRetunyn())) {
						wphyitDTO.setRcvitst(Rcvitst.RETURN.getCode());
						count = inventoryMapper.updateWrcvitForPhysical(wphyitDTO, userVO);

						if(count == 0) {
							throw new UpdateCheckedException();
						}
					}

					//삭제 제품 윗단 정렬(마지막에 정렬하는데 정렬할 필요가 있나?) 필요 없을듯
					WphyitDTO stkLocationAndLayerInfo = inventoryMapper.getStkLocationAndLayer(wphyitDTO, userVO);

					int layer = stkLocationAndLayerInfo.getStlayer();
					List<WstkkyDTO> samePlaceStockList = inventoryMapper.getSamePlaceStock(stkLocationAndLayerInfo, userVO);

					for(WstkkyDTO wstkkyDTO : samePlaceStockList) {
						wstkkyDTO.setLolayer(layer++);
						count = inventoryMapper.updateLayerSort(wstkkyDTO, userVO);

						if(count == 0) {
							throw new UpdateCheckedException();
						}
					}

					if (fromLocationStock.stream().noneMatch(dto ->
					dto.getAreakey().equals(stkLocationAndLayerInfo.getAreakey()) &&
					dto.getLocakey().equals(stkLocationAndLayerInfo.getLocakey()))) {
						fromLocationStock.add(om.convertValue(stkLocationAndLayerInfo, WstkkyDTO.class));
					}

					wphyitDTO.setPhydttm(DateUtil.getDateTime());
					apiPhyList.add(om.convertValue(wphyitDTO, WstkkyDTO.class));
				}else {
					//before 데이터를 못가져와서 재고와 실사테이블 조인
					wphyitDTO.setPhymode(Phymode.BEFORE.getCode());
					WstkkyDTO hasStock = inventoryMapper.doe040HasStock(wphyitDTO, userVO);

					if(hasStock == null) {
						throw new NoSaveDataException();
					}

					if (fromLocationStock.stream().noneMatch(dto ->
								dto.getAreakey().equals(hasStock.getAreakey()) &&
								dto.getLocakey().equals(hasStock.getLocakey()))) {
						fromLocationStock.add(hasStock);
					}

					//이동할 위치에 재고가 존재하냐
					WstkkyDTO samePlaceStock = inventoryMapper.hasPlaceStock(wphyitDTO, userVO);

					//옮길 위치에 재고가 존재하는데 그 재고가 자기 자신일 경우
					boolean isSameStock = samePlaceStock != null &&
							wphyitDTO.getStockky().equals(samePlaceStock.getStockky()) &&
							wphyitDTO.getAreakey().equals(samePlaceStock.getAreakey()) &&
							wphyitDTO.getLocakey().equals(samePlaceStock.getLocakey()) &&
							wphyitDTO.getStlayer().equals(samePlaceStock.getLolayer());


					//옮길 위치에 재고가 존재하는데 그 재고가 자기 자신일 경우 && 옮길 위치에 재고가 존재 할 경우
					if(!isSameStock && samePlaceStock != null) {
						List<WstkkyDTO> samePlaceStockList = inventoryMapper.getSamePlaceStock(wphyitDTO, userVO);
						for(WstkkyDTO wstkkyDTO : samePlaceStockList) {
							//단위치 이동 시 wtakit에도 기록
							if(taskoky.isEmpty()) {
								taskoky = inventoryMapper.selectTaskoky(userVO);
							}
							//wtakit insert
							wstkkyDTO.setTaskoky(taskoky);
							wstkkyDTO.setTaskoit(inventoryMapper.selectTaskoit(taskoky, userVO));
							wstkkyDTO.setToareky(wstkkyDTO.getAreakey());
							wstkkyDTO.setTolocky(wstkkyDTO.getLocakey());
							wstkkyDTO.setTolayer(isSameStock ? wstkkyDTO.getLolayer() : wstkkyDTO.getLolayer() + 1);
							wstkkyDTO.setDoccate(DoccateInventory.STOCK.getCode());
							wstkkyDTO.setDoctype(DoctypeInventory.PHYSICAL_STOCK.getCode());
							wstkkyDTO.setTasksts(Tasksts.NEW.getCode());
							wstkkyDTO.setParsncd(phyHeadData.getParsncd());
							wstkkyDTO.setParsnnm(phyHeadData.getParsnnm());

							count = inventoryMapper.saveDoe035List(wstkkyDTO, userVO);

							if(count == 0) {
								throw new InsertCheckedException();
							}

							//단 + 1
							count = inventoryMapper.updateSamePlaceStock(wstkkyDTO, userVO);

							if(count == 0) {
								throw new UpdateCheckedException();
							}

							//wtakit 완료
							wstkkyDTO.setTasksts(Tasksts.COMPLETE.getCode());
							count = inventoryMapper.updateDoe040Wtakit(wstkkyDTO, userVO);

							if(count == 0) {
								throw new UpdateCheckedException();
							}
						}
					}
					WstkkyDTO afterStockInfo = om.convertValue(wphyitDTO, WstkkyDTO.class);

					if (toLocationStock.stream().noneMatch(dto ->
								dto.getAreakey().equals(afterStockInfo.getAreakey()) &&
								dto.getLocakey().equals(afterStockInfo.getLocakey()))
						&&fromLocationStock.stream().noneMatch(dto ->
								dto.getAreakey().equals(afterStockInfo.getAreakey()) &&
								dto.getLocakey().equals(afterStockInfo.getLocakey()))						) {
						toLocationStock.add(afterStockInfo);
					}

					//재고 위치 업데이트
					wphyitDTO.setTmpupck(hasStock.getUpdtchk());
					count = inventoryMapper.updateDoe040Wstkky(wphyitDTO, userVO);

					if(count == 0) {
						throw new UpdateCheckedException();
					}

					//mskuwc에 있는 값(폭,두꼐,길이 등) 수정
					MskuwcDTO mskuwcDTO = inventoryMapper.getSkuInfo(wphyitDTO, userVO);

					if(mskuwcDTO == null) {
						throw new NoSaveDataException();
					}

					wphyitDTO.setTmpupck(mskuwcDTO.getUpdtchk());
					count = inventoryMapper.updateDoe040Mskuwc(wphyitDTO, userVO);

					if(count == 0) {
						throw new UpdateCheckedException();
					}
				}
				//wphyit 테이블 완료 처리
				wphyitDTO.setParsncd(phyHeadData.getParsncd());
				wphyitDTO.setParsnnm(phyHeadData.getParsnnm());
				wphyitDTO.setPhystat(Phystat.CMP.getCode());
				count = inventoryMapper.updateDoe040Wphyit(wphyitDTO, userVO);

				if(count == 0) {
					throw new UpdateCheckedException();
				}
			}

			//from 단 sort
			for(WstkkyDTO wstkkyDTO : fromLocationStock) {
				int fromLayerCount = 1;
				List<WstkkyDTO> fromLocakeyStockList = inventoryMapper.getLocationStockInfo(wstkkyDTO, userVO);

				for(WstkkyDTO fromLocakeyStock : fromLocakeyStockList) {
					fromLocakeyStock.setStlayer(fromLayerCount);
					fromLocakeyStock.setLolayer(fromLayerCount++);
					count = inventoryMapper.updateStockLayerSort(fromLocakeyStock, userVO);

					if(count == 0) {
						throw new UpdateCheckedException();
					}

					apiPhyList.add(fromLocakeyStock);
				}
			}

			//to 단 sort
			for(WstkkyDTO wstkkyDTO : toLocationStock) {
				int toLayerCount = 1;
				List<WstkkyDTO> toLocakeyStockList = inventoryMapper.getLocationStockInfo(wstkkyDTO, userVO);

				for(WstkkyDTO toLocakeyStock : toLocakeyStockList) {
					toLocakeyStock.setStlayer(toLayerCount);
					toLocakeyStock.setLolayer(toLayerCount++);
					count = inventoryMapper.updateStockLayerSort(toLocakeyStock, userVO);

					if(count == 0) {
						throw new UpdateCheckedException();
					}
					apiPhyList.add(toLocakeyStock);
				}
			}

			//DO -> DC 실사정보 API CALL
			apiConfig.requestToDC(apiPhyList, ApiInfo.API_SM_WPHYIT, HttpMethod.PATCH);
		}
		return count;
	}

	public int saveDoe041(GridDTO<WstkkyDTO> requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;
		String adjsoky = "";
		for(WstkkyDTO wstkkyDTO : requestDTO.getUpdateList()) {
			//재고 유무 확인 및 before 데이터
			WstkkyDTO hasStock = inventoryMapper.hasStock(wstkkyDTO, userVO);

			if(hasStock == null) {
				throw new NoSaveDataException();
			}

			if(adjsoky.isEmpty()) {
				adjsoky = inventoryMapper.selectAdjsoky(userVO);
			}
			String adjgrky = inventoryMapper.selectAdjgrky(userVO);

			//insert before Stock
			hasStock.setAdjsoky(adjsoky);
			hasStock.setAdjsoit(inventoryMapper.selectAdjsoit(hasStock, userVO));
			hasStock.setAdjgrky(adjgrky);
			hasStock.setDoccate(DoccateInventory.STOCK.getCode());
			hasStock.setDoctype(DoctypeInventory.STATUS_CHANGE.getCode());
			hasStock.setAdjdcst(Adjdcst.NEW.getCode());
			hasStock.setAdjitst(Adjitst.NEW.getCode());
			hasStock.setAdjmode(Adjmode.BEFORE.getCode());
			hasStock.setParsncd(wstkkyDTO.getParsncd());
			hasStock.setParsnnm(wstkkyDTO.getParsnnm());

			count = inventoryMapper.insertWadjit(hasStock, userVO);

			if(count == 0) {
				throw new InsertCheckedException();
			}
			//insert after Stock
			wstkkyDTO.setAdjsoky(adjsoky);
			wstkkyDTO.setAdjsoit(inventoryMapper.selectAdjsoit(wstkkyDTO, userVO));
			wstkkyDTO.setAdjgrky(adjgrky);
			wstkkyDTO.setDoccate(DoccateInventory.STOCK.getCode());
			wstkkyDTO.setDoctype(DoctypeInventory.STATUS_CHANGE.getCode());
			wstkkyDTO.setAdjdcst(Adjdcst.NEW.getCode());
			wstkkyDTO.setAdjitst(Adjitst.NEW.getCode());
			wstkkyDTO.setAdjmode(Adjmode.AFTER.getCode());

			count = inventoryMapper.insertWadjit(wstkkyDTO, userVO);

			if(count == 0) {
				throw new InsertCheckedException();
			}
			//update wstkky -> 재고 속성 및 상태, 조정문서번호, 아이템
			count = inventoryMapper.updateDoe041Wstkky(wstkkyDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
			//update mskuwc -> 재고 속성 및 상태
			MskuwcDTO mskuwcDTO = inventoryMapper.getSkuInfo(hasStock, userVO);

			if(mskuwcDTO == null) {
				throw new NoSaveDataException();
			}
			wstkkyDTO.setTmpupck(mskuwcDTO.getUpdtchk());
			wstkkyDTO.setOldowky(hasStock.getOwnerky());
			count = inventoryMapper.updateDoe040Mskuwc(wstkkyDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}

			//update wadjit -> 상태완료
			wstkkyDTO.setAdjdcst(Adjdcst.CMP.getCode());
			wstkkyDTO.setAdjitst(Adjitst.CMP.getCode());
			count = inventoryMapper.updateDoe041Adjistat(wstkkyDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}
		WadjitDTO paramDTO = new WadjitDTO();
		paramDTO.setWarekey(requestDTO.getUpdateList().get(0).getWarekey());
		paramDTO.setAdjsoky(adjsoky);

		List<WadjitDTO> wadjitApiList = inventoryMapper.getWadjitApi(paramDTO, userVO);
		//DO -> DC 조정정보 API CALL
		apiConfig.requestToDC(wadjitApiList, ApiInfo.API_SM_WADJIT, HttpMethod.PATCH);

		return count;
	}

	public List<WstkkyDTO> getDoe042List(WstkkyDTO wstkkyDTO){
		return inventoryMapper.getDoe042List(wstkkyDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public int saveDoe042(GridDTO<WstkkyDTO> requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;
		String adjsoky = "";

		for(WstkkyDTO wstkkyDTO : requestDTO.getUpdateList()) {
			//재고 유무 확인
			WstkkyDTO hasStock = inventoryMapper.hasStock(wstkkyDTO, userVO);

			if(hasStock == null) {
				throw new NoSaveDataException();
			}

			if(adjsoky.isEmpty()) {
				adjsoky = inventoryMapper.selectAdjsoky(userVO);
			}
			String adjgrky = inventoryMapper.selectAdjgrky(userVO);

			//BEFORE 데이터 insert
			hasStock.setAdjsoky(adjsoky);
			hasStock.setAdjsoit(inventoryMapper.selectAdjsoit(hasStock, userVO));
			hasStock.setAdjgrky(adjgrky);
			hasStock.setDoccate(DoccateInventory.STOCK.getCode());
			hasStock.setDoctype(DoctypeInventory.BLOCK.getCode());
			hasStock.setAdjdcst(Adjdcst.NEW.getCode());
			hasStock.setAdjitst(Adjitst.NEW.getCode());
			hasStock.setAdjmode(Adjmode.BEFORE.getCode());
			hasStock.setParsncd(wstkkyDTO.getInprscd());
			hasStock.setAdjsrmk(wstkkyDTO.getInprsnm());

			count = inventoryMapper.insertWadjit(hasStock, userVO);

			if(count == 0) {
				throw new InsertCheckedException();
			}

			//AFTER 데이터 insert
			wstkkyDTO.setAdjsoky(adjsoky);
			wstkkyDTO.setAdjsoit(inventoryMapper.selectAdjsoit(wstkkyDTO, userVO));
			wstkkyDTO.setAdjgrky(adjgrky);
			wstkkyDTO.setDoccate(DoccateInventory.STOCK.getCode());
			wstkkyDTO.setDoctype(DoctypeInventory.BLOCK.getCode());
			wstkkyDTO.setAdjdcst(Adjdcst.NEW.getCode());
			wstkkyDTO.setAdjitst(Adjitst.NEW.getCode());
			wstkkyDTO.setSbloqty(wstkkyDTO.getStotqty());
			wstkkyDTO.setAdjmode(Adjmode.AFTER.getCode());
			wstkkyDTO.setParsncd(wstkkyDTO.getInprscd());
			wstkkyDTO.setAdjsrmk(wstkkyDTO.getInprsnm());
			count = inventoryMapper.insertWadjit(wstkkyDTO, userVO);

			if(count == 0) {
				throw new InsertCheckedException();
			}

			//재고 업데이트
			count = inventoryMapper.updateDoe042Wstkky(wstkkyDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}

			//WADJIT 상태값 업데이트
			wstkkyDTO.setAdjdcst(Adjdcst.CMP.getCode());
			wstkkyDTO.setAdjitst(Adjitst.CMP.getCode());
			count = inventoryMapper.updateDoe041Adjistat(wstkkyDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}

		WadjitDTO paramDTO = new WadjitDTO();
		paramDTO.setWarekey(requestDTO.getUpdateList().get(0).getWarekey());
		paramDTO.setAdjsoky(adjsoky);

		List<WadjitDTO> wadjitApiList = inventoryMapper.getWadjitApi(paramDTO, userVO);

		//DO -> DC 블락정보 API CALL
		apiConfig.requestToDC(wadjitApiList, ApiInfo.API_SM_BLOCK, HttpMethod.PATCH);

		return count;
	}

	public List<WstkkyDTO> getDoe043List(WstkkyDTO wstkkyDTO){
		return inventoryMapper.getDoe043List(wstkkyDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public int saveDoe043(GridDTO<WstkkyDTO> requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;
		String adjsoky = "";

		for(WstkkyDTO wstkkyDTO : requestDTO.getUpdateList()) {
			//재고 유무 확인
			WstkkyDTO hasStock = inventoryMapper.hasStock(wstkkyDTO, userVO);

			if(hasStock == null) {
				throw new NoSaveDataException();
			}

			if(adjsoky.isEmpty()) {
				adjsoky = inventoryMapper.selectAdjsoky(userVO);
			}
			String adjgrky = inventoryMapper.selectAdjgrky(userVO);

			//BEFORE 데이터 insert
			hasStock.setAdjsoky(adjsoky);
			hasStock.setAdjsoit(inventoryMapper.selectAdjsoit(hasStock, userVO));
			hasStock.setAdjgrky(adjgrky);
			hasStock.setDoccate(DoccateInventory.STOCK.getCode());
			hasStock.setDoctype(DoctypeInventory.UNBLOCK.getCode());
			hasStock.setAdjdcst(Adjdcst.NEW.getCode());
			hasStock.setAdjitst(Adjitst.NEW.getCode());
			hasStock.setAdjmode(Adjmode.BEFORE.getCode());
			hasStock.setParsncd(wstkkyDTO.getInprscd());
			hasStock.setAdjsrmk(wstkkyDTO.getInprsnm());

			count = inventoryMapper.insertWadjit(hasStock, userVO);

			if(count == 0) {
				throw new InsertCheckedException();
			}
			//AFTER 데이터 insert
			int sbloqty = wstkkyDTO.getSbloqty() - wstkkyDTO.getStotqty();

			wstkkyDTO.setAdjsoky(adjsoky);
			wstkkyDTO.setAdjsoit(inventoryMapper.selectAdjsoit(wstkkyDTO, userVO));
			wstkkyDTO.setAdjgrky(adjgrky);
			wstkkyDTO.setDoccate(DoccateInventory.STOCK.getCode());
			wstkkyDTO.setDoctype(DoctypeInventory.UNBLOCK.getCode());
			wstkkyDTO.setAdjdcst(Adjdcst.NEW.getCode());
			wstkkyDTO.setAdjitst(Adjitst.NEW.getCode());
			wstkkyDTO.setSbloqty(sbloqty);
			wstkkyDTO.setAdjmode(Adjmode.AFTER.getCode());
			wstkkyDTO.setParsncd(wstkkyDTO.getInprscd());
			wstkkyDTO.setAdjsrmk(wstkkyDTO.getInprsnm());

			count = inventoryMapper.insertWadjit(wstkkyDTO, userVO);

			if(count == 0) {
				throw new InsertCheckedException();
			}

			//재고 업데이트
			count = inventoryMapper.updateDoe042Wstkky(wstkkyDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}

			//WADJIT 상태값 업데이트
			wstkkyDTO.setAdjdcst(Adjdcst.CMP.getCode());
			wstkkyDTO.setAdjitst(Adjitst.CMP.getCode());
			count = inventoryMapper.updateDoe041Adjistat(wstkkyDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}
		WadjitDTO paramDTO = new WadjitDTO();
		paramDTO.setWarekey(requestDTO.getUpdateList().get(0).getWarekey());
		paramDTO.setAdjsoky(adjsoky);

		List<WadjitDTO> wadjitApiList = inventoryMapper.getWadjitApi(paramDTO, userVO);

		//DO -> DC 블락해제정보 API CALL
		apiConfig.requestToDC(wadjitApiList, ApiInfo.API_SM_UNBLOCK, HttpMethod.PATCH);

		return count;
	}

	public List<WstkkyDTO> getDoe089List(WstkkyDTO wstkkyDTO){
		return inventoryMapper.getDoe089List(wstkkyDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	//doe041: 재고조정 (모달) 상세착지 selectbox 가져오기
	public List<MdesmaDTO> getDoe041Denamlc(MdesmaDTO mdesmaDTO){
		return inventoryMapper.getDoe041Denamlc(mdesmaDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	//doe041: 재고조정 (변경모달) 업데이트
	public int updateDoe041ModalUpdate(WstkkyDTO wstkkyDTO) {
		UserVO user = SecurityUtils.getCustomUserDetails().getUserInfo();
		int updateCnt = 0;
		String adjsoky = "";
		int count = 0;

		for(WstkkyDTO wstkky : wstkkyDTO.getCheckList()) {
			wstkky.setDemdman(wstkkyDTO.getDemdman());
	        wstkky.setDemdadr(wstkkyDTO.getDemdadr());
	        wstkky.setDemdtln(wstkkyDTO.getDemdtln());
	        wstkky.setDempocd(wstkkyDTO.getDempocd());
	        wstkky.setDemodnm(wstkkyDTO.getDemodnm());
	        wstkky.setDestmod(wstkkyDTO.getDestmod());
	        wstkky.setRegimod(wstkkyDTO.getRegimod());
	        wstkky.setDemodyn(Useyn.USE.getString());

	        WstkkyDTO hasStock = inventoryMapper.hasStock(wstkky, user);

			if(adjsoky.isEmpty()) {
				adjsoky = inventoryMapper.selectAdjsoky(user);
			}
			String adjgrky = inventoryMapper.selectAdjgrky(user);

			hasStock.setAdjsoky(adjsoky);
			hasStock.setAdjsoit(inventoryMapper.selectAdjsoit(hasStock, user));
			hasStock.setAdjgrky(adjgrky);
			hasStock.setDoccate(DoccateInventory.STOCK.getCode());
			hasStock.setDoctype(DoctypeInventory.STATUS_CHANGE.getCode());
			hasStock.setAdjdcst(Adjdcst.CMP.getCode());
			hasStock.setAdjitst(Adjitst.CMP.getCode());
			hasStock.setAdjmode(Adjmode.BEFORE.getCode());

			count = inventoryMapper.insertWadjit(hasStock, user);

			if(count == 0) {
				throw new InsertCheckedException();
			}

			wstkky.setAdjsoky(adjsoky);
			wstkky.setAdjsoit(inventoryMapper.selectAdjsoit(hasStock, user));
			wstkky.setAdjgrky(adjgrky);
			wstkky.setDoccate(DoccateInventory.STOCK.getCode());
			wstkky.setDoctype(DoctypeInventory.STATUS_CHANGE.getCode());
			wstkky.setAdjdcst(Adjdcst.CMP.getCode());
			wstkky.setAdjitst(Adjitst.CMP.getCode());
			wstkky.setAdjmode(Adjmode.AFTER.getCode());

			count = inventoryMapper.insertWadjit(wstkky, user);

			if(count == 0) {
				throw new InsertCheckedException();
			}

			updateCnt += inventoryMapper.updateDoe041ModalUpdate(wstkky, user);

			if(updateCnt == 0) {
				throw new UpdateCheckedException();
			}
		}

		//DO -> DC 조정정보 API CALL
		apiConfig.requestToDC(wstkkyDTO.getCheckList(), ApiInfo.API_SM_WADJIT, HttpMethod.PATCH);

		return updateCnt;
	}
}