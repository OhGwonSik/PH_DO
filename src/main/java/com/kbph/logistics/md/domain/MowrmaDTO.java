package com.kbph.logistics.md.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MowrmaDTO extends CommonColumnDTO {
	// MOWRMA : 판매계약사 마스터

	// PK
	private String ownerky; // VARCHAR(20) - 판매계약사코드

	// Columns
	private String ownamlc; // VARCHAR(60) - 판매계약사명
	private String ownamko; // VARCHAR(60) - 판매계약사명 한글
	private String ownamen; // VARCHAR(60) - 판매계약사명 영어
	private String ownaddr; // VARCHAR(100) - 주소
	private String ownteln; // VARCHAR(20) - 전화번호
	private String owposbx; // VARCHAR(20) - 지번
	private String owposcd; // VARCHAR(20) - 우편번호
	private String owrepnm; // VARCHAR(60) - 대표자 이름
	private String owreptl; // VARCHAR(20) - 대표자 전화번호
	private String owrepem; // VARCHAR(60) - 대표자 이메일
	private String owblctc; // VARCHAR(20) - 사업자 등록번호
	private int owneord; // INT(11) - 조회순서
	private String owuseyn; // VARCHAR(1) - 사용여부

	// data
	private String oldOwnerky; // 이전 판매계약사키
}