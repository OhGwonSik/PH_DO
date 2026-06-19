package com.kbph.logistics.rm.domain;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-07-09
 * @note : Walshd table DTO
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-07-09					t.s.park        					create DTO class
 * 2024-07-15					t.s.park        					create DTO field
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Getter
@Setter
@ToString
public class WalshdDTO extends CommonColumnDTO {
	// P.K.
	private String warekey;  // varchar(20) 창고키
	private String alstrky; // varchar(20) 할당전략키

	// Column
	private String alstrnm; // varchar(60) 할당전략명
	private String aluseyn; // varchar(1) 할당전략 사용여부

	// Data
	private String whnamlc; // 창고명
}
