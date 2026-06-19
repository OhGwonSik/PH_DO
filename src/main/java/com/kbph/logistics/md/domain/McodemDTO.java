package com.kbph.logistics.md.domain;

import java.util.List;

import com.kbph.logistics.common.domain.ComboDataDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class McodemDTO extends ComboDataDTO {
	// MCODEM : 공통코드 마스터

	// PK
	private String comcdky; // VARCHAR(10) - 공통코드키
	private String comcdvl; // VARCHAR(10) - 공통코드값

	// Columns
	private String comkytx; // VARCHAR(100) - 공통코드키 명칭
	private String comcdtx; // VARCHAR(100) - 공통코드값 명칭
	private int comcdor; // INT - 공통코드 표시순서
	private String comcdsy; // VARCHAR(1) - Code 시스템 전용
	private String commemo; // VARCHAR(100) - 비고
	private String cdcate1; // VARCHAR(50) - 코드분류1
	private String cdcate2; // VARCHAR(50) - 코드분류2
	private String cdcate3; // VARCHAR(50) - 코드분류3
	private String cdattr1; // VARCHAR(50) - 코드속성1
	private String cdattr2; // VARCHAR(50) - 코드속성2
	private String cdattr3; // VARCHAR(50) - 코드속성3
	private String couseyn; // VARCHAR(1) - 공통코드 사용여부

	// Data
	private String oldComcdky; // 이전 공통코드키
	private String oldComcdvl; // 이전 공통코드값

	private String actyn;
	private String cdky;
	private String cdvl;
	private String cdtx;

	private List<String> comcdkys; // Common code Keys
	private List<String> comcdvls; // Common code values
	private List<String> cdcate2s;
	private String mdp3Comcdvl;

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
