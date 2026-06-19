package com.kbph.logistics.md.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.domain.InitDataDTO;
import com.kbph.logistics.common.util.RequestUtil;
import com.kbph.logistics.md.domain.McusmaDTO;
import com.kbph.logistics.md.domain.MdesmaDTO;
import com.kbph.logistics.md.domain.MowrmaDTO;
import com.kbph.logistics.md.domain.MregmaDTO;
import com.kbph.logistics.md.domain.MvhcmaDTO;
import com.kbph.logistics.md.domain.MvowmaDTO;
import com.kbph.logistics.md.service.PartnerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PartnerController {

	private final PartnerService partnerService;

	/*************** DOE006 : 차주 등록 ***************/
	// 차주 리스트 조회
	@GetMapping("/md/doe006/grid")
	public List<MvowmaDTO> getMvowmaList(@ModelAttribute MvowmaDTO params) {
		params.setDmwplnm(RequestUtil.extractSubDomain());
		return partnerService.getMvowmaList(params);
	}

	// 차주 데이터 저장
	@PostMapping("/md/doe006/grid")
	public int saveDoe006(@RequestBody GridDTO<MvowmaDTO> saveList) {
		return partnerService.saveDoe006(saveList, RequestUtil.extractSubDomain());
	}

	/*************** DOE007 : 차량 등록 ***************/
	// 차량 리스트 조회
	@GetMapping("/md/doe007/grid")
	public List<MvhcmaDTO> getMvhcmaList(@ModelAttribute MvhcmaDTO params){
		params.setDmwplnm(RequestUtil.extractSubDomain());
		return partnerService.getMvhcmaList(params);
	}

	// 차량 데이터 저장
	@PostMapping("/md/doe007/grid")
	public int saveDoe007(@RequestBody GridDTO<MvhcmaDTO> saveList) {
		return partnerService.saveDoe007(saveList, RequestUtil.extractSubDomain());
	}

	// doe007 초기화
	@GetMapping("/md/doe007/init")
	public InitDataDTO getDoe007InitData() {
		Map<String, Object> item = new HashMap<>();
		item.put("subDomain", RequestUtil.extractSubDomain());
		return partnerService.getDoe007InitData(item);
	}

	/*************** DOE008 : 목적지 등록 ***************/
	// 목적지 리스트 조회
	@GetMapping("/md/doe008/grid")
	public List<MregmaDTO> getMregmaList(@ModelAttribute MregmaDTO params) {
		return partnerService.getMregmaList(params);
	}

	// 목적지 데이터 저장
	@PostMapping("/md/doe008/grid")
	public int saveDoe008(@RequestBody GridDTO<MregmaDTO> saveList) {
		return partnerService.saveDoe008(saveList);
	}

	/*************** DOE009 : 고객 등록 ***************/
	// 고객 리스트 조회
	@GetMapping("/md/doe009/grid")
	public List<McusmaDTO> getMcusmaList(@ModelAttribute McusmaDTO params) {
		return partnerService.getMcusmaList(params);
	}

	// 고객 데이터 저장
	@PostMapping("/md/doe009/grid")
	public int saveDoe009(@RequestBody GridDTO<McusmaDTO> saveList) {
		return partnerService.saveDoe009(saveList);
	}

	/*************** DOE010 : 상세착지 등록 ***************/
	// 상세착지 리스트 조회
	@GetMapping("/md/doe010/grid")
	public List<MdesmaDTO> getMdesmaList(@ModelAttribute MdesmaDTO params) {
		return partnerService.getMdesmaList(params);
	}

	// 상세착지 정보 저장
	@PostMapping("/md/doe010/grid")
	public int saveDoe010(@RequestBody GridDTO<MdesmaDTO> saveList) {
		return partnerService.saveDoe010(saveList);
	}

	/*************** DOE013 : 판매계약사 등록 (보류) ***************/
	// 판매계약사 리스트 조회
	@GetMapping("/md/doe013/grid")
	public List<MowrmaDTO> getMowrmaList(@ModelAttribute MowrmaDTO params) {
		return partnerService.getMowrmaList(params);
	}

	// 판매계약사 정보 저장
	@PostMapping("/md/doe013/grid")
	public int saveDoe013(@RequestBody GridDTO<MowrmaDTO> saveList) {
		return partnerService.saveDoe013(saveList);
	}

}