package com.kbph.logistics.md.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MvowmaDTO extends CommonColumnDTO {
	// MVOWMA : 차주 마스터

	// PK
	private String vownkey; // VARCHAR(20) - 차주키

	// Columns
	private String voregno; // VARCHAR(20) - 사업자등록번호
	private String vowaffi; // VARCHAR(20) - 소속(직접/위탁) 등
	private String vonamlc; // VARCHAR(20) - 차주명칭
	private String vowmemo; // VARCHAR(100) - 비고
	private String vowbank; // VARCHAR(20) - 차주은행
	private String vowacnm; // VARCHAR(20) - 계좌번호
	private String vowteln; // VARCHAR(20) - 전화번호
	private String vowaddr; // VARCHAR(100) - 주소
	private String vomannm; // VARCHAR(60) - 담당자
	private String vomantl; // VARCHAR(20) - 담당자 연락처
	private String vomanem; // VARCHAR(60) - 담당자 EMAIL
	private String dmwplnm; // VARCHAR(20) - 도메인 사업자명
	private String publcyn; // VARCHAR(1) - 공용여부
	private String vouseyn; // VARCHAR(1) - 사용여부
	private String sigupyn; // VARCHAR(1) - 차주등록여부

	// Data
	private String oldVownkey; // 이전 차주키
}