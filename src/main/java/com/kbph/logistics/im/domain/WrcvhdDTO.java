package com.kbph.logistics.im.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-08-29
 * @note : WrcvhdDTO
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-08-29					t.s.park        					create DTO class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WrcvhdDTO extends CommonColumnDTO {
	// P.K.
	private String rcvdcky; // 입고문서번호
	// Columns
	private String warekey; // 창고키
	private String doccate; // 문서유형
	private String doctype; // 문서타입
	private String vownkey; // 차주키
	private String vehicky; // 차량키
	private String vhcfnam; // 차량번호
	private String drvernm; // 기사명
	private String pastrky; // 적치전략키
	private String rcvdcst; // 입고문서상태
	private String rcvrscd; // 입고사유코드
	private String rcvrsnm; // 입고사유내용
	private String rcarscd; // 입고취소사유코드
	private String rcarsnm; // 입고취소사유내용
	private String rsncode;
	private String rsncdnm;
	private String equipky;
	private String equinam;

	// data
	private String rcvitst; // 입고문서아이템 상태
	private String tasksts; // 지시 상태
	private String taskstsnm; // 지시상태명칭
	private String skumkey;
	private List<String> rcvdcstList; // 입고문서헤더 상태 리스트
	private List<String> taskstsList; // 작업문서헤더 상태 리스트
	private List<String> asnstatList; // 입고예정헤더 상태 리스트
	private List<String> orderlnList; //오더라인 다중검색
	private List<String> skumkeyList; //제품번호 다중검색
	private String taskoky; // task 번호
	private String eoasnky; // asn 번호
	private String whnamlc; // 창고명
	private String docctnm; // 문서유형명
	private String doctynm; // 문서타입명
	private String vonamlc; // 차주명
	private String rcvrscdnm; // 사유코드명
	private String rcvdcstnm; // 문서상태명
	private String asndateFrom; // 입고예정일자 from
	private String asndateTo; // 입고예정일자 to
	private String rcvdcdtFrom; // 입고일자 from
	private String rcvdcdtTo; // 입고일자 to
	private String purcnum; // PO번호
	private String orderln; // 오더라인

	private String statcnt; // 입고완료확정 가능여부 플래그(카운트)
}
