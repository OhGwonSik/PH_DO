package com.kbph.logistics.md.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class McusmaDTO extends CommonColumnDTO {
	// MCUSMA : 고객 마스터

	// PK
	private String custkey; // VARCHAR(20) - 고객키

	// Columns
	private String curegno; // VARCHAR(20) - 사업자등록번호
	private String cunamlc; // VARCHAR(60) - 고객명칭
	private String cunamko; // VARCHAR(60) - 고객명칭 한글
	private String cunamen; // VARCHAR(60) - 고객명칭 영어
	private String cusaddr; // VARCHAR(100) - 고객 주소
	private String custeln; // VARCHAR(20) - 전화번호
	private String cuposbx; // VARCHAR(10) - 지번
	private String cuposcd; // VARCHAR(10) - 우편번호
	private String curepnm; // VARCHAR(60) - 대표자 이름
	private String cureptl; // VARCHAR(20) - 대표자 전화번호
	private String curepem; // VARCHAR(60) - 대표자 이메일
	private String cumannm; // VARCHAR(60) - 담당자 이름
	private String cumantl; // VARCHAR(20) - 담당자 전화번호
	private String cumanem; // VARCHAR(60) - 담당자 이메일
	private String cusbank; // VARCHAR(20) - 고객 은행
	private String cusacnm; // VARCHAR(20) - 고객 계좌번호
	private String surchyn; // VARCHAR(1) - 추가금청구대상여부
	private String cudelyn; // VARCHAR(1) - 고객 사용여부

	// data
	private String oldCustkey; // 이전 고객키
}