package com.kbph.logistics.md.domain;

import com.kbph.logistics.common.domain.ComboDataDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MrscmaDTO extends ComboDataDTO {
	// MRSCMA : 사유코드 마스터

	// PK
	private String warekey; // VARCHAR(20) - 창고키
	private String doccate; // VARCHAR(10) - 문서유형
	private String doctype; // VARCHAR(10) - 문서타입
	private String rsncode; // VARCHAR(10) - 사유코드

	// Columns
	private String rsncdnm; // VARCHAR(10) - 사유코드명
	private int rsncdod; // INT(11) - 사유코드 표시순서
	private String rscate1; // VARCHAR(10) - 사유분류1
	private String rscate2; // VARCHAR(10) - 사유분류2
	private String rscate3; // VARCHAR(10) - 사유분류3
	private String rsattr1; // VARCHAR(10) - 사유속성1
	private String rsattr2; // VARCHAR(10) - 사유속성2
	private String rsattr3; // VARCHAR(10) - 사유속성3
	private String rsuseyn; // VARCHAR(1) - 사유코드 사용여부

	// Data
	private String oldWarekey; // 이전 창고키
	private String oldDoccate; // 이전 문서유형
	private String oldDoctype; // 이전 문서타입
	private String oldRsncode; // 이전 사유코드

	private String docctnm; // 문서유형 명칭 <커스텀>
	private String doctynm; // 문서타입 명칭 <커스텀>

	// PQGrid recIndx;
	private String rowkey;

	// Common columns
	private String credate; // VARCHAR(8) create date
	private String cretime; // VARCHAR(6) create time
	private String creuser; // VARCHAR(60) create user
	private String lmodate; // VARCHAR(8) last modify date
	private String lmotime; // VARCHAR(6) last modify time
	private String lmouser; // VARCHAR(60) last modify user
	private Integer updtchk; // INT(11) Update check
}
