package com.kbph.logistics.md.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.domain.InitDataDTO;
import com.kbph.logistics.md.domain.MgrpmaDTO;
import com.kbph.logistics.md.domain.MskuwcDTO;
import com.kbph.logistics.md.service.UnitsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UnitsController {
	private final UnitsService unitsService;

	/*************** DOE011 : 제품 등록 ***************/
	// 제품 리스트 조회
	@GetMapping("/md/doe011/grid")
	public List<MskuwcDTO> getMskuwcList(@ModelAttribute MskuwcDTO params) {
		return unitsService.getMskuwcList(params);
	}

	// 제품 데이터 저장
	@PostMapping("/md/doe011/grid")
	public int saveDoe011(@RequestBody GridDTO<MskuwcDTO> saveList) {
		return unitsService.saveDoe011(saveList);
	}

	// doe011 초기화
	@GetMapping("/md/doe011/init")
	public InitDataDTO getDoe011InitData() {
		return unitsService.getDoe011InitData();
	}

	/*************** DOE012 : 제품그룹정보 등록 ***************/
	// 제품그룹 리스트 조회
	@GetMapping("/md/doe012/grid")
	public List<MgrpmaDTO> getMgrpmaList(@ModelAttribute MgrpmaDTO params) {
		return unitsService.getMgrpmaList(params);
	}

	// 제품그룹 데이터 저장
	@PostMapping("/md/doe012/grid")
	public int saveDoe012(@RequestBody GridDTO<MgrpmaDTO> saveList) {
		return unitsService.saveDoe012(saveList);
	}
}
