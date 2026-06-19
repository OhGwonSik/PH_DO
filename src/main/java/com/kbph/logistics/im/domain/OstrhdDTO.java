package com.kbph.logistics.im.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-08-01
 * @note : OstrhdDTO
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-08-01					t.s.park        					create DTO class
 * 2024-08-30					t.s.park        					edit field
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OstrhdDTO extends CommonColumnDTO {
	// P.K.
	private String costrky; // 입고오더번호

	// Column
	private String warekey; // 창고키
	private String doccate; // 문서유형
	private String doctype; // 문서타입
	private String rsncode; // 사유코드
	private String parsnnm; // 작업사유내용
	private String vownkey; // 차주키
	private String vehicky; // 차량코드
	private String vhcfnam; // 차량번호
	private String drvernm; // 차량기사명
	private String strstat; // 입고오더상태
	private String strdate; // 입고오더일자
	private String strtime; // 입고오더시간
}
