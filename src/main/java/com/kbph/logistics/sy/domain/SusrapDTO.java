package com.kbph.logistics.sy.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SusrapDTO extends CommonColumnDTO {
	private String userkey; // 유저 key
	private String useract; // 유저 ID
	private String applkey; // 어플리케이션 key

	// data
	private String apnamen; // 어플리케이션 명 영어
}
