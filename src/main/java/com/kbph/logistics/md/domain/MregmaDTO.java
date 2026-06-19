package com.kbph.logistics.md.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MregmaDTO extends CommonColumnDTO {
	// MREGMA : 목적지(권역) 마스터

	// PK
	private String regikey; // VARCHAR(20) - 목적지코드

	// Columns
	private String renamlc; // VARCHAR(60) - 목적지명
	private String regaddr; // VARCHAR(100) - 목적지 주소
	private String regmemo; // VARCHAR(100) - 목적지 특이사항
	private String reuseyn; // VARCHAR(1) - 사용여부

	// data
	private String oldRegikey; // 이전 목적지키
}