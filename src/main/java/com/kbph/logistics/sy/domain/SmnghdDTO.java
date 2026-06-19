package com.kbph.logistics.sy.domain;

import java.io.Serializable;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SmnghdDTO extends CommonColumnDTO implements Serializable {
	private static final long serialVersionUID = -1446470837241875657L;
	private String compkey;	// Company Key
	private String mnhdkey;	// 헤더키
	private String mnhdnam;	// 헤더명
	private String mnhdlky;	// 헤더라벨
	private String mnhddsc; // 헤더 설명
	private String hduseyn;	// 사용여부
}
