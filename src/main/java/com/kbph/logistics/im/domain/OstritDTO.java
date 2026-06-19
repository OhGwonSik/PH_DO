package com.kbph.logistics.im.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-08-01
 * @note : OstritDTO
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-08-01					t.s.park        					create DTO class
 * 2024-08-30					t.s.park        					edit field
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OstritDTO extends CommonColumnDTO {
	// P.K.
	private String costrky; // 입고오더번호
	private Integer costrit; // 입고오더아이템번호

	// Columns
	private String warekey; // 창고키
	private String ownerky; // 판매계약사키
	private String ownrnam; // 판매계약사명
	private String poshpdt; // 포스코 출하일자
	private String poshptm; // 포스코 출하시간
	private String poshpdy; // 포스코 출하날자
	private String invoice; // 송장번호
	private String stritst; // 입고오더아이템상태
	private String skugrky; // 제품그룹키
	private String skumkey; // 제품명
	private String skudesc; // 제품명
	private Integer stlayer; // 적재단
	private Integer skuweig; // 중량
	private Float skuwidt; // 폭
	private Integer skuleng; // 길이
	private Integer grosswg; // 주문중량
	private Float grosswd; // 주문폭
	private Integer grossln; // 주문길이
	private Float grossth; // 주문두께
	private Float skuthic; // 두께
	private String suomkey; // 단위
	private String skustrd; // 규격
	private Integer stroqty; // 입고오더수량
	private Integer strcqty; // 입고완료수량
	private String custcod; // 고객사키
	private String custnam; // 고객사명
	private String fncusky; // 최종고객사코드
	private String regiorg; // 원본목적지
	private String destorg; // 원본상세착지
	private String regimod; // 수정목적지
	private String destmod; // 수정상세착지
	private String demodnm; // 수정상세착지명
	private String demdman; // 수정상세착지담당자
	private String demdadr; // 수정상세착지주소
	private String dempocd; // 수정상세착지우편번호
	private String demdtln; // 수정상세착지연락처
	private String itemcod; // 품목코드
	private String purcnum; // 고객 PO번호
	private String orderln; // 오더라인
	private String stlotnb; // Stock lot number
	private String btrfstc; // 진도코드
	private String shplimt; // 출하기한
	private String delvdsr; // 희망납기
	private String skustat; // 제품상태
	private String strmemo; // 비고
	private String rsncode; // 사유코드
	private String parsnnm; // 사유내용
	private String addcoky; // 할증코드
	private String btrcate; // 정산유형
	private String closeyn; // 정산마감여부
	private String serinum; // 일련번호
	private String savepos; // 저장위치
	private String schdate; // 출하일정
	private String mixload; // 혼적
	private String 	desttrg; // 착지
	private String yardtrg; // 야드
	private String inbdate; // 입고일시
	private String inbvhnm; // 입고차번
	private String oubvhnm; // 출고차번
	private String shpdrnm; // 출고기사
	private String prsaldt; // 선매출일
	private String cmpdate; // 완료일자
	private String shppers; // 출고자
	private String oubdate; // 출고일시
	private String 	preprde; // 전처리착지
	private String arvdate; // 양하도착예정
	private String 	shpodat; // 출하지시일시
	private String trsodat; // 운송지시일시
	private String indimet; // 내경
	private String outdimt; // 외경
	private String emskuyn; // 긴급재여부
	private String restkyn; // 재입고여부
	private String demodyn; // 도착지변경여부
	private String stkmemo; // 제품메모
}
