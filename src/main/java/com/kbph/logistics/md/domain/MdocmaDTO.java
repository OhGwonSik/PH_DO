package com.kbph.logistics.md.domain;

import java.util.List;

import com.kbph.logistics.common.domain.ComboDataDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MdocmaDTO extends ComboDataDTO {
	// MDOCMA : 문서타입 마스터

	// PK
	private String warekey; // VARCHAR(20) - 창고키
	private String doccate; // VARCHAR(10) - 문서유형
	private String doctype; // VARCHAR(10) - 문서타입

	// Columns
	private String docctnm; // VARCHAR(60) - 문서유형 명칭
	private String doctynm; // VARCHAR(60) - 문서타입 명칭
	private String doctat1; // VARCHAR(50) - 타입속성1
	private String doctat2; // VARCHAR(50) - 타입속성2
	private String doctat3; // VARCHAR(50) - 타입속성3
	private String douseyn; // 사용여부

	// Data
	private List<String> doctypes; // doctype 여러개
	private List<String> doccates; // doccate 여러개
}
