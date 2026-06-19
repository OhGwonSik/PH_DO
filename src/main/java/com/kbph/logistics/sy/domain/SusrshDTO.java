package com.kbph.logistics.sy.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SusrshDTO extends CommonColumnDTO {
	private String userkey; // 유저 key
	private String useract; // 유저 ID
	private String usertyp; // 사용자타입
	private String rolgkey; // 사용자권한

	// data
	private String oldUserkey; // 이전 유저키
}
