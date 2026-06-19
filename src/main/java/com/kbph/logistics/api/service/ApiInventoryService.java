package com.kbph.logistics.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.kbph.logistics.api.config.ApiConfig;
import com.kbph.logistics.api.constant.Constants;
import com.kbph.logistics.api.domain.ApiCommonDTO;
import com.kbph.logistics.api.domain.SelectionOperationCancelDTO;
import com.kbph.logistics.api.domain.SelectionOperationComplDTO;
import com.kbph.logistics.api.domain.SelectionOperationDTO;
import com.kbph.logistics.api.domain.SelectionTaskDTO;
import com.kbph.logistics.api.domain.WstkkyUploadDTO;
import com.kbph.logistics.api.enums.ApiInfo;
import com.kbph.logistics.api.mapper.ApiInventoryMapper;
import com.kbph.logistics.configuration.error.DuplicateLayerException;
import com.kbph.logistics.configuration.error.TaskProcessingException;
import com.kbph.logistics.configuration.error.UpdateCheckedException;
import com.kbph.logistics.md.type.Tasksts;
import com.kbph.logistics.sm.domain.WstkkyDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiInventoryService {

	private final ApiInventoryMapper apiInventoryMapper;
	private final ApiConfig apiConfig;

	public int updateWstkkyUploadInfo(ApiCommonDTO<WstkkyUploadDTO> apiCommon){

	    WstkkyUploadDTO params = apiCommon.getApiData().get(0);

	    apiInventoryMapper.spUpdWstkkyExcel(params, apiCommon);

	    Integer dataCnt = params.getOresult();

	    if(dataCnt == null) {
	    	return 0;
	    }

		//프로시저 call 결과값(재고 업로드 개수) == 플래그가 U인 개수(관제로 인터페이스할 데이터)
		List<WstkkyDTO> apiStockList = apiInventoryMapper.getApiExcelUploadUpdateList(apiCommon);

		// 프로시저 update 개수가 없고, 플래그 U가 있을 경우 대비
		if(dataCnt != 0 && dataCnt != apiStockList.size()) {
			throw new TaskProcessingException("ms.stockUpdateMismatch");
		}

		if(!apiStockList.isEmpty()) {
			//관제 인터페이스
			apiConfig.requestToDC(apiStockList, ApiInfo.API_SM_WPHYIT_CREATE, HttpMethod.POST);
			// 플래그 U에서 Y로 업데이트
			int count = apiInventoryMapper.updateStockFlag(apiCommon);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}
		return dataCnt;
	}

	// 선별지시 취소 - 선별 조업시작 전
	public ApiCommonDTO<SelectionOperationCancelDTO> saveSelectionCancel(ApiCommonDTO<SelectionOperationCancelDTO> apiCommon){
		apiCommon.setUseract(Constants.API_USERACT);
		int count = 0;

		for(SelectionOperationCancelDTO selectionOperationCancelDTO : apiCommon.getApiData()) {

			// 조업 시작 전인지, 취소상태인지 검증
			int notNewStatCnt = apiInventoryMapper.checkSelectionCancelVail(selectionOperationCancelDTO, apiCommon);

			if(notNewStatCnt != 0) {
				throw new TaskProcessingException("ms.cancelBeforeStart");
			}

			// 재고 작업수량 0 update
			count = apiInventoryMapper.updateApiSelectionCancelForWstkky(selectionOperationCancelDTO, apiCommon);

			if(count == 0) {
				throw new UpdateCheckedException();
			}

			// 작업테이블 CANCEL
			count = apiInventoryMapper.updateSelectionStateCancel(selectionOperationCancelDTO, apiCommon);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}
		return apiCommon;
	}

	// 조업 시작
	public ApiCommonDTO<SelectionOperationDTO> saveSelectionStart(ApiCommonDTO<SelectionOperationDTO> apiCommon){
		apiCommon.setUseract(Constants.API_USERACT);
		List<SelectionOperationDTO> selectionBeforeLocaList = new ArrayList<>();
		int count = 0;
		//조업시작 벨리데이션
		for(SelectionOperationDTO selectionOperationDTO : apiCommon.getApiData()) {
			int taskAllList = apiInventoryMapper.checkSelectionStart(selectionOperationDTO, apiCommon);

			selectionOperationDTO.setTasksts(Tasksts.OPERATION_START.getCode());
			int operStrList = apiInventoryMapper.checkSelectionStart(selectionOperationDTO, apiCommon);

			selectionOperationDTO.setTasksts(Tasksts.TASK_COMPLETE.getCode());
			int taskCmpList = apiInventoryMapper.checkSelectionStart(selectionOperationDTO, apiCommon);

			selectionOperationDTO.setTasksts(Tasksts.COMPLETE.getCode());
			int operCmpList = apiInventoryMapper.checkSelectionStart(selectionOperationDTO, apiCommon);

			if(taskAllList == operStrList) {
				throw new TaskProcessingException("ms.operation.startCompleted");
			}

			if(taskAllList == taskCmpList) {
				throw new TaskProcessingException("ms.operation.taskCmp");
			}else if(taskCmpList > 0) {
				throw new TaskProcessingException("ms.operation.taskStart");
			}

			if(taskAllList == operCmpList) {
				throw new TaskProcessingException("ms.operation.allCompleted");
			}
		}

		// 작업 상태값 update
		count = apiInventoryMapper.updateSelectionStart(apiCommon);

		if(count == 0) {
			throw new UpdateCheckedException();
		}

		for(SelectionOperationDTO selectionOperationDTO : apiCommon.getApiData()) {
			//조업시작 시점 제품의 LOCAKEY, LOLAYER 조회
			selectionBeforeLocaList.clear();
			selectionBeforeLocaList = apiInventoryMapper.getApiSelectionBeforeLocaList(selectionOperationDTO, apiCommon);

			for(SelectionOperationDTO selectionBeforeLocaData : selectionBeforeLocaList) {
				count = apiInventoryMapper.updateApiSelectionBeforeLoca(selectionBeforeLocaData, apiCommon);

				if(count == 0) {
					throw new UpdateCheckedException();
				}
			}

			//DO -> DC 조업시작시 from베드 , from단 동기화  API CALL
			apiConfig.requestToDC(selectionBeforeLocaList, ApiInfo.API_SM_WTAKIT_UPDATE, HttpMethod.PATCH);
		}

		return apiCommon;
	}

	// 작업 완료
	public ApiCommonDTO<SelectionTaskDTO> saveSelectionTask(ApiCommonDTO<SelectionTaskDTO> apiCommon){
		apiCommon.setUseract(Constants.API_USERACT);

		int count = 0;

		//작업 완료 벨리데이션
		for(SelectionTaskDTO selectionTaskDTO : apiCommon.getApiData()) {
			int asnAndTaskAllList = apiInventoryMapper.checkSelectionStart(selectionTaskDTO, apiCommon);

			selectionTaskDTO.setTasksts(Tasksts.NEW.getCode());
			int newList = apiInventoryMapper.checkSelectionStart(selectionTaskDTO, apiCommon);

			selectionTaskDTO.setTasksts(Tasksts.TASK_COMPLETE.getCode());
			int taskCmpList = apiInventoryMapper.checkSelectionStart(selectionTaskDTO, apiCommon);

			selectionTaskDTO.setTasksts(Tasksts.COMPLETE.getCode());
			int operCmpList = apiInventoryMapper.checkSelectionStart(selectionTaskDTO, apiCommon);


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

		//단 체크
		for(SelectionTaskDTO selectionTaskDTO : apiCommon.getApiData()) {
			WstkkyDTO sameLocAndLayer = apiInventoryMapper.isSameLocAndLayerApi(selectionTaskDTO, apiCommon);

			if(sameLocAndLayer != null) {
				throw new DuplicateLayerException();
			}
		}

		//관제에서 실제 작업한 단의 정보를 보낸다는 가정
		count = apiInventoryMapper.updateApiWstkky(apiCommon);

		if(count == 0) {
			throw new UpdateCheckedException();
		}

		//작업완료 상태 update 및 실제 작업한 위치 update
		count = apiInventoryMapper.updateSelectionTask(apiCommon);

		if(count == 0) {
			throw new UpdateCheckedException();
		}
		return apiCommon;
	}

	// 조업 완료
	public ApiCommonDTO<SelectionOperationComplDTO> saveSelectionCompl(ApiCommonDTO<SelectionOperationComplDTO> apiCommon){
		apiCommon.setUseract(Constants.API_USERACT);
		int count = 0;
		//sort 된 from 베드 리스트 - api용
		List<WstkkyDTO> apiPhyList = new ArrayList<>();
		List<WstkkyDTO> fromLocationStock = new ArrayList<>();

		//조업완료 벨리데이션
		for(SelectionOperationComplDTO selectionOperationComplDTO : apiCommon.getApiData()) {
			int taskAllList = apiInventoryMapper.checkSelectionStart(selectionOperationComplDTO, apiCommon);

			selectionOperationComplDTO.setTasksts(Tasksts.NEW.getCode());
			int newList = apiInventoryMapper.checkSelectionStart(selectionOperationComplDTO, apiCommon);

			selectionOperationComplDTO.setTasksts(Tasksts.OPERATION_START.getCode());
			int operStrList = apiInventoryMapper.checkSelectionStart(selectionOperationComplDTO, apiCommon);

			selectionOperationComplDTO.setTasksts(Tasksts.TASK_COMPLETE.getCode());
			int taskCmpList = apiInventoryMapper.checkSelectionStart(selectionOperationComplDTO, apiCommon);

			selectionOperationComplDTO.setTasksts(Tasksts.COMPLETE.getCode());
			int operCmpList = apiInventoryMapper.checkSelectionStart(selectionOperationComplDTO, apiCommon);

			if(taskAllList == newList) {
				throw new TaskProcessingException("ms.operation.startFirst");
			}

			if(taskAllList == operStrList) {
				throw new TaskProcessingException("ms.operation.completeFirst");
			}

			if(taskAllList == operCmpList) {
				throw new TaskProcessingException("ms.operation.allCompleted");
			}

			if(taskCmpList > 0 && taskAllList != taskCmpList) {
				throw new TaskProcessingException("ms.operation.notAllCompleted");
			}
		}

		for(SelectionOperationComplDTO selectionOperationComplDTO : apiCommon.getApiData()) {
			//from loc set
			WstkkyDTO getFromLocStock = apiInventoryMapper.getFromLocStock(selectionOperationComplDTO, apiCommon);

			if (fromLocationStock.stream().noneMatch(dto ->
			dto.getAreakey().equals(getFromLocStock.getAreakey()) &&
			dto.getLocakey().equals(getFromLocStock.getLocakey()))) {
				fromLocationStock.add(getFromLocStock);
			}
		}

		//단수 from 쉬프트 추가하기
		//from 단 sort
		for(WstkkyDTO wstkkyDTO : fromLocationStock) {
			int fromLayerCount = 1;
			List<WstkkyDTO> fromLocakeyStockList = apiInventoryMapper.getApiLocationStockInfo(wstkkyDTO, apiCommon);

			for(WstkkyDTO fromLocakeyStock : fromLocakeyStockList) {
				fromLocakeyStock.setLolayer(fromLayerCount++);
				count = apiInventoryMapper.updateApiStockLayerSort(fromLocakeyStock, apiCommon);

				if(count == 0) {
					throw new UpdateCheckedException();
				}

				apiPhyList.add(fromLocakeyStock);

			}
		}
		//DO -> DC 실사정보(단수 sort 정보) API CALL
		apiConfig.requestToDC(apiPhyList, ApiInfo.API_SM_WPHYIT, HttpMethod.PATCH);


		// 조업 완료 -> 선별완료 확정
		count = apiInventoryMapper.updateSelectionCompl(apiCommon);

		if(count == 0) {
			throw new UpdateCheckedException();
		}

		//조업 완료 시 최종 위치 변경
		count = apiInventoryMapper.updateApiWstkky(apiCommon);

		if(count == 0) {
			throw new UpdateCheckedException();
		}

		return apiCommon;
	}

}
