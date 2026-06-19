package com.kbph.logistics.rm.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-07-15
 * @note : Walsma table DTO
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-07-15					t.s.park        					create DTO class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Getter
@Setter
@ToString
public class WalsmaDTO extends CommonColumnDTO {
	// P.K.
	private String alstrmd; // varchar(20) 할당방식키

	// Columns
	private String alstrnm; // varchar(60) 할당방식명
	private String alstrtg; // varchar(20) 할당기준
	private Integer alstrod; // int(11) 할당순서
	private String aluseyn; // varchar(1) 할당방식 사용여부
}
