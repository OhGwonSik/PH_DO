package com.kbph.logistics.md.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MvhcmaDTO extends CommonColumnDTO {
	// MVHCMA : 차량 마스터

	// PK
	private String vownkey; // VARCHAR(20) - 차주키
	private String vehicky; // VARCHAR(10) - 차량코드

	// Columns
	private String vhcsnam; // VARCHAR(60) - 차량명칭
	private String vhcfnam; // VARCHAR(20) - 차량번호
	private String dlvtype; // VARCHAR(10) - 차량타입
	private String vhcopty; // VARCHAR(10) - 차량운영형태
	private String vhctype; // VARCHAR(10) - 차량종류
	private String vhctncd; // VARCHAR(10) - 차량톤수
	private int vhcmaxw; // INT(11) - 최대중량
	private double vhccapa; // DECIMAL(10,3) - 최대적재 CBM
	private String drvernm; // VARCHAR(10) - 기사이름
	private String drverph; // VARCHAR(20) - 기사 전화번호
	private String inbvhyn; // VARCHAR(1) - 입고차량여부
	private String oubvhyn; // VARCHAR(1) - 출고차량여부
	private String poscoyn; // VARCHAR(1) - 포스코등록여부
	private String vhcstat; // VARCHAR(10) - 차량상태
	private String vhcmemo; // VARCHAR(100) - 차량비고
	private String vhcvryn; // VARCHAR(1) - 가변여부
	private String vhuseyn; // VARCHAR(1) - 사용여부
	private String dmwplnm; // VARCHAR(20) - 도메인 사업자명
	private String publcyn; // VARCHAR(1) - 공용여부
	private String ifprsts;

	// data
	private String oldVownkey; // 이전 차주키
	private String oldVehicky; // 이전 차량코드
	private String vonamlc;
	private String comcdsy; // 시스템 코드여부
	private int voudtck; // 차주 update check
}