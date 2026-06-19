package com.kbph.logistics.md.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.domain.InitDataDTO;
import com.kbph.logistics.md.domain.MaremaDTO;
import com.kbph.logistics.md.domain.MeqlocDTO;
import com.kbph.logistics.md.domain.MequmaDTO;
import com.kbph.logistics.md.domain.MlocmaDTO;
import com.kbph.logistics.md.domain.MwarmaDTO;
import com.kbph.logistics.md.service.OrganizationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class OrganizationController {

	private final OrganizationService organizationService;

	/*************** DOE001 : 창고 등록 ***************/
	// 창고 리스트 조회 (grid 데이터)
	@GetMapping("/md/doe001/grid")
	public List<MwarmaDTO> getMwarmaList(@ModelAttribute MwarmaDTO params) {
		return organizationService.getMwarmaList(params);
	}

	// 창고 데이터 저장 (grid 데이터)
	@PostMapping("/md/doe001/grid")
	public int saveMdo02(@RequestBody GridDTO<MwarmaDTO> saveList) {
		return organizationService.saveDoe001(saveList);
	}

	// doe001 초기화
	/*@GetMapping("/md/doe001/init")
	public InitDataDTO getDoe001InitData() {
		return organizationService.getDoe001InitData();
	}*/

	/*************** DOE002 : 창고동 등록 ***************/
	// 창고동 리스트 조회 (grid 데이터)
	@GetMapping("/md/doe002/grid")
	public List<MaremaDTO> getMaremaList(@ModelAttribute MaremaDTO params) {
		return organizationService.getMaremaList(params);
	}

	// 창고동 데이터 저장 (grid 데이터)
	@PostMapping("/md/doe002/grid")
	public int saveMdo03(@RequestBody GridDTO<MaremaDTO> saveList) {
		return organizationService.saveDoe002(saveList);
	}

	/*************** DOE003 : 베드 등록 ***************/
	// 베드 리스트 조회 (grid용 데이터)
	@GetMapping("/md/doe003/grid")
	public List<MlocmaDTO> getMlocmaList(@ModelAttribute MlocmaDTO params) {
		return organizationService.selectMlocmaList(params);
	}

	// 베드 데이터 저장
	@PostMapping("/md/doe003/grid")
	public int saveDoe003(@RequestBody GridDTO<MlocmaDTO> saveList) {
		return organizationService.saveDoe003(saveList);
	}

	// doe003 초기화
	@GetMapping("/md/doe003/init")
	public InitDataDTO getDoe003InitData() {
		return organizationService.getDoe003InitData();
	}

	// 베드 관계 리스트 조회 (창고-창고동)
	@GetMapping("/md/wa-relations")
	public List<MlocmaDTO> getWareAreaRelations(@ModelAttribute MlocmaDTO params) {
		return organizationService.getWareAreaRelations(params);
	}

	// 베드 관계 리스트 조회 (창고-베드)
	@GetMapping("/md/wal-relations")
	public List<MlocmaDTO> getWareAreaLocRelations(@ModelAttribute MlocmaDTO params) {
		return organizationService.getWareAreaLocRelations(params);
	}

	/*************** DOE004 : 설비 등록 ***************/
	// 설비 리스트 조회 (grid용 데이터)
	@GetMapping("/md/doe004/grid")
	public List<MequmaDTO> getMequmaList(@ModelAttribute MequmaDTO params) {
		return organizationService.getMequmaList(params);
	}

	// 설비 데이터 저장
	@PostMapping("/md/doe004/grid")
	public int saveDoe004(@RequestBody GridDTO<MequmaDTO> saveList) {
		return organizationService.saveDoe004(saveList);
	}

	// doe004 초기화
	@GetMapping("/md/doe004/init")
	public InitDataDTO getDoe004InitData() {
		return organizationService.getDoe004InitData();
	}

	// 설비 관계 리스트 조회 (창고-설비)
	@GetMapping("/md/wae-relations")
	public List<MequmaDTO> selectMcodemSelectBox(MequmaDTO params) {
		return organizationService.getWareAreaEquipRelations(params);
	}

	/*************** DOE005 : 설비-베드 매핑 ***************/
	// 설비-베드 매핑 리스트 조회
	@GetMapping("/md/doe005/grid")
	public List<MlocmaDTO> getMappingList(@ModelAttribute MlocmaDTO params){
		return organizationService.getMappingList(params);
	}

	// 설비-베드 매핑 데이터 저장
	@PostMapping("/md/doe005/grid")
	public int saveDoe005(@RequestBody GridDTO<MeqlocDTO> saveList) {
		return organizationService.saveDoe005(saveList);
	}

	// doe005 초기화
	@GetMapping("/md/doe005/init")
	public InitDataDTO getDoe005InitData() {
		return organizationService.getDoe005InitData();
	}
}