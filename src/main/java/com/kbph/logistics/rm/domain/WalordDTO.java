package com.kbph.logistics.rm.domain;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-07-09
 * @note : Walord table DTO
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-07-09					t.s.park        					create DTO class
 * 2024-07-09					t.s.park        					create DTO field
 * 2024-08-30					t.s.park        					edit field
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Getter
@Setter
@ToString
public class WalordDTO extends CommonColumnDTO {
	// P.K.
	private String warekey;  // varchar(20) 창고키
	private String doccate; // varchar(10) 문서유형
	private String doctype; // varchar(10) 문서타입

	// Column
	private String alstrky; // varchar(20) 할당전략키
	private String aluseyn; // varchar(1) 할당전략 사용여부

	// Data
	private String whnamlc; // 창고명
	private String docctnm; // 문서유형명
	private String doctynm; // 문서타입명
}
