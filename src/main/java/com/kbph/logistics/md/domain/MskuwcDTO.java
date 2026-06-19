package com.kbph.logistics.md.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class MskuwcDTO extends CommonColumnDTO {
	// MSKUWC : 제품 마스터

	// PK
	private String warekey; // VARCHAR(20) - 창고키
	private String ownerky; // VARCHAR(20) - 판매계약사키
	private String skumkey; // VARCHAR(50) - 제품번호
	private String skugrky; // VARCHAR(10) - 제품그룹키
	private String addcoky; // VARCHAR(10) - 할증코드

	// Columns
	private String skudesc; // VARCHAR(100) - 제품명
	private String itemcod; // VARCHAR(20) - 품목코드
	private int skuweig; // DECIMAL(10,3) - 중량
	private double skuwidt; // INT(11) - 폭
	private int skuleng; // INT(11) - 길이
	private double skuthic; // INT(11) - 두께
	private String suomkey; // VARCHAR(10) - Default unit of measure
	private String skustat; // VARCHAR(20) - 제품상태

	// data
	private String oldWarekey; // 이전 창고키
	private String oldOwnerky; // 이전 판매계약사키
	private String oldSkumkey; // 이전 제품번호
	private String oldSkugrky; // 이전 제품그룹키
	private String oldAddcoky; // 이전 할증코드
	private String comcdtx;

	private String whnamlc; // 창고명 <커스텀>
	private String ownamlc; // 판매계약사명 <커스텀>
	private String grpkynm; // 제품그룹키 명 <커스텀>
	private String addsiky; // 할증기준코드 <커스텀>
	private List<String> skumkeyList;
	private String addsinm; // 할증기준명칭
	private String skugrnm;
}