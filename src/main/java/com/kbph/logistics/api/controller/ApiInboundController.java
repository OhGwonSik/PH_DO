package com.kbph.logistics.api.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.api.constant.Constants;
import com.kbph.logistics.api.domain.ApiCommonDTO;
import com.kbph.logistics.api.domain.ExcelWasnifInfo;
import com.kbph.logistics.api.domain.InbEntranceCarDTO;
import com.kbph.logistics.api.domain.InbEntranceDongDTO;
import com.kbph.logistics.api.domain.InboundOperationDTO;
import com.kbph.logistics.api.domain.InboundTaskDTO;
import com.kbph.logistics.api.domain.InspectionDTO;
import com.kbph.logistics.api.service.ApiInboundService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.INBOUND_PREFIX)
public class ApiInboundController {
	private final ApiInboundService apiInboundService;
	
	//이송입고관리.xls => 정품입고관리.xls 	
	@PostMapping("/excel")
	public int saveConversionInfo(@RequestBody ApiCommonDTO<ExcelWasnifInfo> apiCommon){
		return apiInboundService.conversionWasnifInfo(apiCommon);
	}

	// 입차정보
	@PostMapping("/entrance/car")
	public ApiCommonDTO<InbEntranceCarDTO> saveEntranceCar(@RequestBody ApiCommonDTO<InbEntranceCarDTO> apiCommon){
		return apiInboundService.saveEntranceCar(apiCommon);
	}

	// 입동지시정보
	@PostMapping("/entrance/dong")
	public ApiCommonDTO<InbEntranceDongDTO> saveEntranceDong(@RequestBody ApiCommonDTO<InbEntranceDongDTO> apiCommon){
		return apiInboundService.saveEntranceDong(apiCommon);
	}

	// 검수완료실적
	@PatchMapping("/inspection/compl")
	public ApiCommonDTO<InspectionDTO> saveInspectionCompl(@RequestBody ApiCommonDTO<InspectionDTO> apiCommon){
		return apiInboundService.saveInspectionCompl(apiCommon);
	}

	// 입고조업시작
	@PatchMapping("/start")
	public ApiCommonDTO<InboundOperationDTO> saveInboundStart(@RequestBody ApiCommonDTO<InboundOperationDTO> apiCommon){
		return apiInboundService.saveInboundStart(apiCommon);
	}

	// 입고작업완료
	@PatchMapping("/task")
	public ApiCommonDTO<InboundTaskDTO> saveInboundTask(@RequestBody ApiCommonDTO<InboundTaskDTO> apiCommon){
		return apiInboundService.saveInboundTask(apiCommon);
	}

	// 입고조업완료
	@PatchMapping("/compl")
	public ApiCommonDTO<InboundOperationDTO> saveInboundCompl(@RequestBody ApiCommonDTO<InboundOperationDTO> apiCommon){
		return apiInboundService.saveInboundCompl(apiCommon);
	}

}
