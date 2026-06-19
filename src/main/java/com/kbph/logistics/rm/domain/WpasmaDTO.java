package com.kbph.logistics.rm.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-07-09
 * @note : Wpasma table DTO
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
public class WpasmaDTO extends CommonColumnDTO {
	// primary key
	private String pastrmd; // varchar(20) 적치방식키

	// field
	private String pastrnm; // varchar(60) 적치방식명
	private String pastrtg; // varchar(20) 적치기준
	private Integer pastrod; // int(11) 적치순서
	private String pauseyn; // varchar(1) 적치방식 사용여부
}
