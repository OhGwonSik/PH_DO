package com.kbph.logistics.sy.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SrolwhDTO extends CommonColumnDTO {

	private String rolgkey;	// 롤 그룹 키
	private String warekey;	// 창고 키
	private String wauseyn;	// 권한별 창고 사용여부

	private String whnamlc; //창고명

	private String state; //그리드용 권한별 창고 사용여부
}
