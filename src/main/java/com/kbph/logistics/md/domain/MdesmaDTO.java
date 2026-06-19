package com.kbph.logistics.md.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MdesmaDTO extends CommonColumnDTO {
	// MDESMA : 상세착지 마스터

	private String cdrcode; // concat 고객-상세착지-목적지
	// PK
	private String regikey; // VARCHAR(20) - 목적지키
	private String custkey; // VARCHAR(20) - 고객키
	private String destkey; // VARCHAR(20) - 상세착지키
	private String ownerky;

	// Columns
	private String denamlc; // VARCHAR(60) - 상세착지 명칭
	private String denamko; // VARCHAR(60) - 상세착지 명칭 한글
	private String denamen; // VARCHAR(60) - 상세착지 명칭 영어
	private String demannm; // VARCHAR(60) - 담당자 이름
	private String deposbx; // VARCHAR(10) - 지번
	private String desteln; // VARCHAR(20) - 전화번호
	private String deposcd; // VARCHAR(10) - 상세착지 우편번호
	private String desaddr; // VARCHAR(100) - 상세주소
	private String desmemo; // VARCHAR(100) - 상세착지 특이사항
	private String unavat1; // VARCHAR(10) - 진입불가 차량톤수1
	private String unavat2; // VARCHAR(10) - 진입불가 차량톤수2
	private String unavat3; // VARCHAR(10) - 진입불가 차량톤수3
	private String deuseyn; // VARCHAR(1) - 상세착지 사용여부

	// data
	private String oldRegikey; // 이전 목적지키
	private String oldCustkey; // 이전 고객키
	private String oldDestkey; // 이전 상세착지키

	private String cunamlc; // 고객명<커스텀>
	private String renamlc; // 상세착지 명칭<커스텀>

	// api data
	private String reuseyn;
}