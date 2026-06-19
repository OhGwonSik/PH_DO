package com.kbph.logistics.rm.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-07-09
 * @note : Wpasit table DTO
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-07-09					t.s.park        					create DTO class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Getter
@Setter
@ToString
public class WpasitDTO extends CommonColumnDTO {
	// P.K.
	private String warekey;  // varchar(20) 창고키
	private String pastrky; // varchar(20) 적치전략키
	private Integer pastrit; // int(11) 적치전략아이템

	// Column
	private String pastrmd; // 적치방식키
	private String pastrst; // 적치전략 정렬방식
	private Integer pastrwg; // 적치전략가중치
	private String paestyn; // 필수조건 yn
	private String pauseyn; // 적치방식 사용여부

	// Data
	private String whnamlc;
	private String pastrnm;
	private String pastrtg;
	private String doccate;
	private String doctype;
	private String pasthnm; // 헤더 전략 명칭
}
