package com.kbph.logistics.md.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MwarmaDTO extends CommonColumnDTO {
	// MWARMA : 창고 마스터

	// PK
	private String warekey; // VARCHAR(20) - 창고키

	// Columns
	private String whnamlc; // VARCHAR(60) - 창고명칭 (LOCAL)
	private String whnamko; // VARCHAR(60) - 창고명칭 (KO)
	private String whnamen; // VARCHAR(60) - 창고명칭 (EN)
	private String waraddr; // VARCHAR(100) - 주소
	private String warteln; // VARCHAR(20) - 전화번호
	private String waposbx; // VARCHAR(10) - 지번
	private String waposcd; // VARCHAR(10) - 우편번호
	private String warepnm; // VARCHAR(60) - 대표자 이름
	private String wareptl; // VARCHAR(20) - 대표자 전화번호
	private String warepem; // VARCHAR(60) - 대표자 EMAIL
	private String wamannm; // VARCHAR(60) - 담당자 이름
	private String wamantl; // VARCHAR(20) - 담당자 전화번호
	private String wamanem; // VARCHAR(60) - 담당자 EMAIL
	private String wmptrnm; // VARCHAR(100) - 창고 메인프린터 명칭
	private String wsptrnm; // VARCHAR(100) - 창고 서브프린터 명칭
	private int watarea; // INT(11) - 총면적 m3
	private int wascapa; // INT(11) - 최대보관 CAPA
	private String poststm; // VARCHAR(6) - 실적일자 변경시간
	private String stkdytm; // VARCHAR(6) - 일 재고 변경시간
	private String holtmfm; // VARCHAR(6) - 휴일할증기준 From
	private String holtmto; // VARCHAR(6) - 휴일할증기준 To
	private int holrate; // INT(11) - 휴일할증률(%)
	private String plarmyn; // VARCHAR(1) - 배차승인알람여부
	private String proofyn; // VARCHAR(1) - 수요가증빙용 출력여부
	private String whuseyn; // VARCHAR(1) - 창고사용여부

	// Data
	private String oldWarekey; // 이전 창고키
}
