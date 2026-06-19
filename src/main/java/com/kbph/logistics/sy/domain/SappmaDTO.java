package com.kbph.logistics.sy.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SappmaDTO extends CommonColumnDTO {
	// SAPPMA : SY Application

	// PK
	private String applkey; // VARCHAR(10) - 어플리케이션키

	// Columns
	private String apnamen; // VARCHAR(10) - 어플리케이션명 영문
	private String apnamko; // VARCHAR(10) - 어플리케이션명
	private String appdesc; // VARCHAR(100) - 어플리케이션 설명

}