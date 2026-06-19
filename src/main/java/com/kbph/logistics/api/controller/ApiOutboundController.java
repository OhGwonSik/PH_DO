package com.kbph.logistics.api.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.api.constant.Constants;
import com.kbph.logistics.api.domain.ApiCommonDTO;
import com.kbph.logistics.api.domain.OubEntranceCarDTO;
import com.kbph.logistics.api.domain.OubEntranceDongDTO;
import com.kbph.logistics.api.domain.OutboundOperationDTO;
import com.kbph.logistics.api.domain.OutboundTaskDTO;
import com.kbph.logistics.api.service.ApiOutboundService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.OUTBOUND_PREFIX)
public class ApiOutboundController {
	private final ApiOutboundService apiOutboundService;

	// 입차정보
	@PostMapping("/entrance/car")
	public ApiCommonDTO<OubEntranceCarDTO> saveEntranceCar(@RequestBody ApiCommonDTO<OubEntranceCarDTO> apiCommon){
		return apiOutboundService.saveEntranceCar(apiCommon);
	}

	// 입동정보
	@PostMapping("/entrance/dong")
	public ApiCommonDTO<OubEntranceDongDTO> saveEntranceDong(@RequestBody ApiCommonDTO<OubEntranceDongDTO> apiCommon){
		return apiOutboundService.saveEntranceDong(apiCommon);
	}

	// 출고조업시작
	@PatchMapping("/start")
	public ApiCommonDTO<OutboundOperationDTO> saveOutboundStart(@RequestBody ApiCommonDTO<OutboundOperationDTO> apiCommon){
		return apiOutboundService.saveOutboundStart(apiCommon);
	}

	// 출고작업완료
	@PatchMapping("/task")
	public ApiCommonDTO<OutboundTaskDTO> saveOutboundTask(@RequestBody ApiCommonDTO<OutboundTaskDTO> apiCommon){
		return apiOutboundService.saveOutboundTask(apiCommon);
	}

	// 출고작업리스트갱신
	@PostMapping("/task/update")
	public ApiCommonDTO<OutboundTaskDTO> updateOutboundTask(@RequestBody ApiCommonDTO<OutboundTaskDTO> apiCommon){
		return apiOutboundService.updateOutboundTask(apiCommon);
	}

	// 출고조업완료
	@PatchMapping("/compl")
	public ApiCommonDTO<OutboundOperationDTO> saveOutboundCompl(@RequestBody ApiCommonDTO<OutboundOperationDTO> apiCommon){
		return apiOutboundService.saveOutboundCompl(apiCommon);
	}
}
