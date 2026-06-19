package com.kbph.logistics.md.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MgrpmaDTO extends CommonColumnDTO {
	// MGRPMA : 제품그룹 마스터

	// PK
	private String skugrky; // VARCHAR(10) - 제품그룹키

	// Columns
	private String grpkynm; // VARCHAR(100) - 제품그룹키 명칭
	private int minleng; // INT(11) - 최소길이
	private int minwidt; // INT(11) - 최소폭
	private int maxleng; // INT(11) - 최대길이
	private int maxwidt; // INT(11) - 최대폭
	private int skugror; // INT(11) - 표시순서
	private String gruseyn; // VARCHAR(1) - 사용여부
	private String vhctype; //VARCHAR(10) - 차량종류
	private Integer szgubun; //INT(11) - 사이즈구분

	// data
	private String oldSkugrky; // 이전 제품그룹키
}