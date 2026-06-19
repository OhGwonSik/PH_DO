package com.kbph.logistics.rm.domain;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-07-09
 * @note : Walsit table DTO
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
public class WalsitDTO extends CommonColumnDTO {
	// P.K.
	private String warekey;  // varchar(20) 창고키
	private String alstrky; // varchar(20) 할당전략키
	private Integer alstrit; // int(11) 할당전략아이템

	// Column
	private String alstrmd; // 할당방식키
	private String alstrst; // 할당전략 정렬방식
	private Integer alstrwg; // 할당전략가중치
	private String alestyn; // 필수조건yn
	private String aluseyn; // 할당방식 사용여부

	// Data
	private String whnamlc;
	private String alstrnm;
	private String alstrtg;
	private String doccate;
	private String doctype;
	private String alsthnm; // 전략명(헤더)

}
