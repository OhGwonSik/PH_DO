package com.kbph.logistics.im.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-08-01
 * @note : InboundOrderForTreeGridDTO
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-08-01					t.s.park        					create DTO class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class InboundOrderForTreeGridDTO {
	//header
	private String costrky; // 입고오더번호
	private String warekey; // 창고키
	private String doccate; // 문서유형
	private String doctype; // 문서타입
	private String rsncode; // 사유코드
	private String parsnnm; // 작업사유내용
	private String vownkey; // 차주키
	private String vehicky; // 차량코드
	private String drvernm; // 차량기사명
	private String strstat; // 입고오더상태
	private String strdate; // 입고오더일자
	private String strtime; // 입고오더시간
	private String hdsavyn; //예정확정 헤더 저장 가능여부

	// item
	private Integer costrit; // 입고오더아이템번호
	private String ownerky; // 판매계약사키
	private String ownrnam; // 판매계약사명칭
	private String poshpdt; // 포스코 출하일자
	private String poshptm; // 포스코 출하시간
	private String poshpdy; // 포스코 출하요일
	private String invoice; // 송장번호
	private String stritst; // 입고오더아이템상태
	private String skugrky; // 제품그룹키
	private String skumkey; // 제품명
	private String skudesc; // 제품명
	private Integer stlayer; // 적재단
	private Double skuweig; // 중량
	private Integer skuwidt; // 폭
	private Integer skuleng; // 길이
	private Double grosswg; // 주문중량
	private Integer grosswd; // 주문폭
	private Integer grossln; // 주문길이
	private Double grossth; // 주문두께
	private Integer skuthic; // 두께
	private String suomkey; // 단위
	private String skustrd; // 규격
	private Integer stroqty; // 입고오더수량
	private Integer strcqty; // 입고완료수량
	private String custcod; // 고객사키
	private String custnam; // 고객사명칭
	private String fncusky; // 최종고객사 코드/명칭
	private String regiorg; // 원본목적지
	private String destorg; // 원본상세착지
	private String regimod; // 수정목적지
	private String destmod; // 수정상세착지
	private String itemcod; // 품목코드
	private String purcnum; // 고객 PO번호
	private String orderln; // 오더라인
	private String stlotnb; // Stock lot number
	private String btrfstc; // 진도코드
	private String btrcate; // 정산유형
	private String shplimt; // 출하기한
	private String delvdsr; // 희망납기
	private String skustat; // 제품상태
	private String parsncd; // 작업사유코드
	private String strmemo; // 비고
	private String equipky; // 설비코드
	private String demodyn; // 상세착지변경여부
	private String emskuyn; // 긴급재여부
	private String restkyn; // 재입고여부

	private String deorgnm; //원본착지명
	private String demodnm; // 변경상세착지명
	private String demdman; // 수정상세착지담당자
	private String demdadr; // 수정상세착지주소
	private String dempocd; // 수정상세착지우편번호
	private String demdtln; // 수정상세착지연락처

	private String stkmemo; // 제품메모

	// data
	private String toareky; // to area
	private String tolocky; // to location
	private Integer tolayer; // to layer
	private String ullocky; // 하차포인트
	private String vhcfnam; // 차량번호

	private String rcvdcdt; // 입고일자
	private String rcvdctm; // 입고시간

	// grid tree node
	private List<InboundOrderForTreeGridDTO> children;

	// API
	private String eoasnky; // 입고예정번호
}
