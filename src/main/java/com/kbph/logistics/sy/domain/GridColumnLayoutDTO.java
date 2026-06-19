package com.kbph.logistics.sy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GridColumnLayoutDTO extends CommonColumnDTO{

	private String compkey;		//Company Key
	private String useract;		//유저 ID
	private String progrid;		//프로그램 ID
	private String pgridid;		//그리드 ID
	private String dataidx;		//컬럼 인덱스

	private int sortnum;	//컬럼 순서
	private Boolean phidden;	//숨김 여부
	private String pqalign;		//위치조절(왼쪽,가운데,오른쪽)
	private String datatyp;		//컬럼 타입
	private int pqwidth;	//컬럼 너비
}
