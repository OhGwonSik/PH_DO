package com.kbph.logistics.md.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.common.domain.ComboDataDTO;
import com.kbph.logistics.common.util.RequestUtil;
import com.kbph.logistics.md.domain.MaremaDTO;
import com.kbph.logistics.md.domain.McodemDTO;
import com.kbph.logistics.md.domain.McusmaDTO;
import com.kbph.logistics.md.domain.MdesmaDTO;
import com.kbph.logistics.md.domain.MdocmaDTO;
import com.kbph.logistics.md.domain.MeqlocDTO;
import com.kbph.logistics.md.domain.MequmaDTO;
import com.kbph.logistics.md.domain.MgrpmaDTO;
import com.kbph.logistics.md.domain.MlocmaDTO;
import com.kbph.logistics.md.domain.MowrmaDTO;
import com.kbph.logistics.md.domain.MregmaDTO;
import com.kbph.logistics.md.domain.MrscmaDTO;
import com.kbph.logistics.md.domain.MskuwcDTO;
import com.kbph.logistics.md.domain.MvhcmaDTO;
import com.kbph.logistics.md.domain.MvowmaDTO;
import com.kbph.logistics.md.domain.MwarmaDTO;
import com.kbph.logistics.md.service.CodeService;
import com.kbph.logistics.md.service.DocumentService;
import com.kbph.logistics.md.service.OrganizationService;
import com.kbph.logistics.md.service.PartnerService;
import com.kbph.logistics.md.service.UnitsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommonController {
	// 공용 컨트롤러 (Select Box)
	private final OrganizationService organizationService; // 창고, 창고동, 베드, 설비
	private final PartnerService partnerService; // 차주, 차량, 목적지, 고객, 상세착지, 판매계약사
	private final UnitsService unitsService; // 제품, 제품그룹
	private final CodeService codeService; // 공통코드
	private final DocumentService documentService; // 문서타입

	// DOE001 : 창고
	// 창고 셀렉트박스 조회
	@GetMapping("/md/organization/warehouse")
	public List<ComboDataDTO> getWarehouseSelectBox(@ModelAttribute MwarmaDTO params) {
		return organizationService.getWarehouseSelectBox(params);
	}
	// DOE002 : 창고동(창고동)
	// 창고동 셀렉트박스 조회
	@GetMapping("/md/organization/area")
	public List<ComboDataDTO> getMaremaSelectBox(@ModelAttribute MaremaDTO params) {
		return organizationService.getAreaSelectBox(params);
	}

	// DOE003 : 베드
	// 베드 셀렉트박스 조회
	@GetMapping("/md/organization/location")
	public List<ComboDataDTO> getLocationSelectBox(@ModelAttribute MlocmaDTO params) {
		return organizationService.getLocationSelectBox(params);
	}

	// DOE004 : 설비
	// 설비 셀렉트박스 조회
	@GetMapping("/md/organization/equipment")
	public List<ComboDataDTO> getEquipmentSelectBox(@ModelAttribute MequmaDTO params) {
		return organizationService.getEquipmentSelectBox(params);
	}

	// DOE006 : 차주
	// 차주 셀렉트박스 조회
	@GetMapping("/md/partner/vehicle-owner")
	public List<ComboDataDTO> getVehicleOwnerSelectBox(@ModelAttribute MvowmaDTO params) {
		params.setDmwplnm(RequestUtil.extractSubDomain());
		return partnerService.getVehicleOwnerSelectBox(params);
	}

	// DOE007 : 차량
	// 차량 셀렉트박스 조회
	@GetMapping("/md/partner/vehicle")
	public List<ComboDataDTO> getVehicleSelectBox(@ModelAttribute MvhcmaDTO params) {
		params.setDmwplnm(RequestUtil.extractSubDomain());
		return partnerService.getVehicleSelectBox(params);
	}

	// DOE008 : 목적지(권역)
	// 목적지 셀렉트박스 조회
	@GetMapping("/md/partner/region")
	public List<ComboDataDTO> getRegionSelectBox(@ModelAttribute MregmaDTO params) {
		return partnerService.getRegionSelectBox(params);
	}

	// DOE009 : 고객
	// 고객 셀렉트박스 조회
	@GetMapping("/md/partner/customer")
	public List<ComboDataDTO> getCustomerSelectBox(@ModelAttribute McusmaDTO params) {
		return partnerService.getCustomerSelectBox(params);
	}

	// DOE010 : 상세착지
	// 상세착지 셀렉트박스 조회
	@GetMapping("/md/partner/destination")
	public List<ComboDataDTO> getDestinationSelectBox(@ModelAttribute MdesmaDTO params) {
		return partnerService.getDestinationSelectBox(params);
	}

	// DOE011 : 제품
	// 제품 셀렉트박스 조회
	@GetMapping("/md/units/sku")
	public List<ComboDataDTO> getSkuSelectBox(@ModelAttribute MskuwcDTO params) {
		return unitsService.getSkuSelectBox(params);
	}

	// DOE012 : 제품그룹
	// 제품그룹 셀렉트박스 조회
	@GetMapping("/md/units/sku-group")
	public List<ComboDataDTO> getSkuGroupSelectBox(@ModelAttribute MgrpmaDTO params) {
		return unitsService.getSkuGroupSelectBox(params);
	}

	// DOE013 : 판매계약사
	// 판매계약사 셀렉트박스 조회
	@GetMapping("/md/partner/owner")
	public List<ComboDataDTO> getOwnerSelectBox(@ModelAttribute MowrmaDTO params) {
		return partnerService.getOwnerSelectBox(params);
	}

	// DOE014 : 공통코드
	// 공통코드 셀렉트박스 조회
	@GetMapping("/md/code/common-code")
	public List<McodemDTO> getCommonCodeSelectBox(@ModelAttribute McodemDTO params) {
		return codeService.getCommonCodeSelectBox(params);
	}

	// DOE015 : 사유코드
	// 사유코드 셀렉트박스 조회
	@GetMapping("/md/code/reason-code")
	public List<MrscmaDTO> getReasonCodeSelectBox(@ModelAttribute MrscmaDTO params) {
		return codeService.getReasonCodeSelectBox(params);
	}

	// MDOCMA : 문서타입 마스터
	// 문서 유형 셀렉트박스 조회
	@GetMapping("/md/document/document-category")
	public List<MdocmaDTO> getDocumentCategorySelectBox(@ModelAttribute MdocmaDTO params) {
		return documentService.getDocumentCategorySelectBox(params);
	}

	// 문서 타입 셀렉트박스 조회
	@GetMapping("/md/document/document-type")
	public List<MdocmaDTO> getDocumentTypeSelectBox(@ModelAttribute MdocmaDTO params) {
		return documentService.getDocumentTypeSelectBox(params);
	}

	// 고객 - 목적지 관계리스트 조회
	@GetMapping("/md/partner/registration/relation")
	public List<MdesmaDTO> getRegistrationRelationList(@ModelAttribute MdesmaDTO params) {
		return partnerService.getRegistrationRelationList(params);
	}

	// 고객 - 목적지 - 상세착지 관계리스트 조회
	@GetMapping("/md/partner/destination/relation")
	public List<MdesmaDTO> getDestinationRelationList(@ModelAttribute MdesmaDTO params) {
		return partnerService.getDestinationRelationList(params);
	}

	// 창고 - 창고동 관계리스트 조회
	@GetMapping("/md/organization/area/relation")
	public List<MaremaDTO> getAreaRelationList(@ModelAttribute MaremaDTO params) {
		return organizationService.getAreaRelationList(params);
	}

	// 창고 - 창고동 - 베드 관계리스트 조회
	@GetMapping("/md/organization/location/relation")
	public List<MlocmaDTO> getLocationRelationList(@ModelAttribute MlocmaDTO params) {
		return organizationService.getLocationRelationList(params);
	}

	// 차주-차량 관계리스트 조회
	@GetMapping("/md/partner/vehicle/relation")
	public List<MrscmaDTO> getVehicleRelationList(@ModelAttribute MvhcmaDTO params) {
		params.setDmwplnm(RequestUtil.extractSubDomain());
		return partnerService.getVehicleRelationList(params);
	}

	// 사유코드 관계리스트 조회
	@GetMapping("/md/code/reason-code/relation")
	public List<MrscmaDTO> getReasonCodeRelationList(@ModelAttribute MrscmaDTO params) {
		return codeService.getReasonCodeRelationList(params);
	}

	// 설비 - 창고 - 창고동 - 베드 관계 리스트 조회
	@GetMapping("/md/organization/equip/relation")
	public List<MeqlocDTO> getEquipByLocForSelectBox(MlocmaDTO mlocmaDTO){
		return organizationService.getEquipByLocForSelectBox(mlocmaDTO);
	}
}