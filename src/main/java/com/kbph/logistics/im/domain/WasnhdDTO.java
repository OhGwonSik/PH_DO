package com.kbph.logistics.im.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WasnhdDTO extends CommonColumnDTO {
	// P.K.
	private String eoasnky; // 입고예정번호 VARCHAR(10)

	// Columns
	private String warekey; // 창고키 VARCHAR(20)
	private String doccate; // 문서유형 VARCHAR(10)
	private String doctype; // 문서타입 VARCHAR(10)
	private String vownkey; // 차주키 VARCHAR(20)
	private String vehicky; // 차량코드 VARCHAR(10)
	private String vhcfnam; // 차량번호 VARCHAR(20)
	private String drvernm; // 차량기사명 VARCHAR(60)
	private String asnstat; // 입고예정상태코드 VARCHAR(10)
	private String asndate; // 입고예정일자 VARCHAR(8)
	private String asntime; // 입고예정시간 VARCHAR(6)
	private String rsncode; // 사유코드 VARCHAR(10)
	private String pastrky; // 적치전략코드 VARCHAR(20)
	private String parsnnm; // 작업사유내용 VARCHAR(1000)
	private String rcarscd; // 작업취소사유
	private String rcarsnm; // 작업취소사유내용
	private String asnpiyn; // 입고예정검수여부 VARCHAR(1)
	private String asnityn; // 입고검수대상여부 VARCHAR(1)
	private String hdsavyn; // 입고예정확정 헤더 저장 가능여부 VARCHAR(1)
	private String entdate; // 입차일자 VARCHAR(8)
	private String enttime; // 입차시간 VARCHAR(6)
	private String equipky;
	private String equinam;
	private String areanam;

	// Data
	private String rcvdcky;
	private String asndateto; // asn To date
	private String asndatefrom; // asn from date
	private String whnamlc; // 창고명
	private String asnstnm; // 입고예정상태
	private String docctnm; // 문서유형명
	private String doctynm; // 문서타입명
	private String rsncdnm; // 사유코드명
	private Integer itemcnt; // 아이템 갯수
	private Integer ttlweig; // 총 중량
	private Integer ttldqty; // 총 입고예정수량
	private String asnitst; // 입고아이템상태
	private String taskoky; // task 문서번호
	private String tasksts; // task 상태
	private String vonamlc; // 차주명
	private String forExamFlag; // 검수 대상 조회시 사용 플래그
	private String forInboundOrderFlag; // 입고오더 조회시 사용 플래그
	private List<String> asnstatList; // asn 상태 리스트
	private List<String> asnitstList; // asn 아이템 상태 리스트
	private List<String> orderlnList;
	private String purcnum; // po번호
	private String orderln; // 오더라인

	private String vhplnky;
	private String loadkey;
	private String cncldat;
	private String aprovyn;
}
