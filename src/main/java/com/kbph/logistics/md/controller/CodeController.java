package com.kbph.logistics.md.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.domain.InitDataDTO;
import com.kbph.logistics.md.domain.McodemDTO;
import com.kbph.logistics.md.domain.MdocmaDTO;
import com.kbph.logistics.md.domain.MrscmaDTO;
import com.kbph.logistics.md.service.CodeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CodeController {
	private final CodeService codeService;

	/*************** DOE014 : 공통코드 등록 ***************/
	// 공통코드 리스트 조회
	@GetMapping("/md/doe014/grid")
	public List<McodemDTO> getMcodemList(@ModelAttribute McodemDTO params) {
		return codeService.getMcodemList(params);
	}

	// 공통코드 데이터 저장
	@PostMapping("/md/doe014/grid")
	public int saveDoe014(@RequestBody GridDTO<McodemDTO> saveList) {
		return codeService.saveDoe014(saveList);
	}

	/*************** DOE015 : 사유코드 등록 ***************/
	// 사유코드 리스트 조회
	@GetMapping("/md/doe015/grid")
	public List<MrscmaDTO> getMrscmaList(@ModelAttribute MrscmaDTO params) {
		return codeService.getMrscmaList(params);
	}

	// 사유코드 데이터 저장
	@PostMapping("/md/doe015/grid")
	public int saveDoe015(@RequestBody GridDTO<MrscmaDTO> saveList) {
		return codeService.saveDoe015(saveList);
	}

	// doe015 초기화
	@GetMapping("/md/doe015/init")
	public InitDataDTO getDoe015InitData(@ModelAttribute MdocmaDTO params) {
		return codeService.getDoe015InitData(params);
	}
}
