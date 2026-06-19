package com.kbph.logistics.rm.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-07-09
 * @note : Wpashd table DTO
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
public class WpashdDTO extends CommonColumnDTO {
	// P.K.
	private String warekey;  // varchar(20) 창고키
	private String pastrky; // varchar(20) 적치전략키

	// Column
	private String pastrnm; // varchar(60) 적치전략명
	private String pauseyn; // varchar(1) 적치전략 사용여부

	// Data
	private String whnamlc; // 창고명
}
