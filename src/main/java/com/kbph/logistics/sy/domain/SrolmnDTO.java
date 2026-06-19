package com.kbph.logistics.sy.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SrolmnDTO extends CommonColumnDTO {

	// [24.06.28] 기존 UesrMenuDTO => SmnumaDTO 수정

	private String menukey; // VARCHAR(50) 메뉴키
	private String mnhdkey; // VARCHAR(50) 헤더ID
	private String menufid; // VARCHAR(50) 상위 메뉴키
	private String menugbn; // VARCHAR(10) 메뉴구분(FLD PGM)
	private String menunam; // VARCHAR(100) 메뉴명
	private String menuseq; // VARCHAR(50) 순번
	private String menulky; // VARCHAR(20) 메뉴 라벨키
	private String progrid; // VARCHAR(50) 프로그램ID
	private String prgmurl; // VARCHAR(200) 프로그램 경로
	private String mnuseyn; // VARCHAR(1) 사용여부

	private String rmuseyn; // VARCHAR(1) 롤 메뉴 사용여부

	private String rolgkey;
	private String rolnmky;

	private String state;
}

