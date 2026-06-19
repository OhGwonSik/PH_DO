package com.kbph.logistics.md.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MaremaDTO extends CommonColumnDTO {
	// MAREMA : 창고동 마스터

	// PK
	private String warekey; // VARCHAR(20) - 창고키
	private String areakey; // VARCHAR(20) - 창고동키

	// Columns
	private String areanam; // VARCHAR(60) - 창고동 명칭
	private String areatyp; // VARCHAR(20) - 창고동 타입
	private String arememo; // VARCHAR(100) - 비고
	private String aruseyn; // VARCHAR(1) - 창고동 사용여부

	// Data
	private String oldWarekey; // 이전 창고키
	private String oldAreakey; // 이전 창고동 키

	private String whnamlc; // 창고 명칭 <커스텀>
	private String combonm;
}