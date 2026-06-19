package com.kbph.logistics.api.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.api.constant.Constants;
import com.kbph.logistics.api.domain.ApiCommonDTO;
import com.kbph.logistics.api.domain.SelectionOperationCancelDTO;
import com.kbph.logistics.api.domain.SelectionOperationComplDTO;
import com.kbph.logistics.api.domain.SelectionOperationDTO;
import com.kbph.logistics.api.domain.SelectionTaskDTO;
import com.kbph.logistics.api.domain.WstkkyUploadDTO;
import com.kbph.logistics.api.service.ApiInventoryService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.INVENTORY_PREFIX)
public class ApiInventoryController {

	private final ApiInventoryService apiInventoryService;

	//재고엑셀업로드 정보 수정
	@PatchMapping("/upload/wstkky/excel")
	public int updateWstkkyUploadInfo(@RequestBody ApiCommonDTO<WstkkyUploadDTO> apiCommon){
		return apiInventoryService.updateWstkkyUploadInfo(apiCommon);
	}


	//선별지시 취소
	@DeleteMapping("/wtakit/cancel")
	public ApiCommonDTO<SelectionOperationCancelDTO> saveSelectionCancel(@RequestBody ApiCommonDTO<SelectionOperationCancelDTO> apiCommon){
		return apiInventoryService.saveSelectionCancel(apiCommon);
	}

	// 조업 시작
	@PatchMapping("/selection/start")
	public ApiCommonDTO<SelectionOperationDTO> saveSelectionStart(@RequestBody ApiCommonDTO<SelectionOperationDTO> apiCommon){
		return apiInventoryService.saveSelectionStart(apiCommon);
	}

	// 작업 완료
	@PatchMapping("/selection/task")
	public ApiCommonDTO<SelectionTaskDTO> saveSelectionTask(@RequestBody ApiCommonDTO<SelectionTaskDTO> apiCommon){
		return apiInventoryService.saveSelectionTask(apiCommon);
	}

	// 조업 완료
	@PatchMapping("/selection/compl")
	public ApiCommonDTO<SelectionOperationComplDTO> saveSelectionCompl(@RequestBody ApiCommonDTO<SelectionOperationComplDTO> apiCommon){
		return apiInventoryService.saveSelectionCompl(apiCommon);
	}
}