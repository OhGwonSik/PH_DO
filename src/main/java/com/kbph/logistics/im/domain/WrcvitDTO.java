package com.kbph.logistics.im.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-08-29
 * @note : WrcvitDTO
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-08-29					t.s.park        					create DTO class
 * 2024-08-30					t.s.park        					edit field
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WrcvitDTO extends CommonColumnDTO {
	// P.K.
	private String rcvdcky; // 입고문서번호
	private Integer rcvdcit; // 입고문서아이템 번호

	// Columns
	private String poshpdt; // 포스코 출하일자
	private String invoice; // 포스코 송장번호
	private String poshptm; // 포스코 출하시간
	private String poshpdy; // 포스코 출하요일
	private String warekey; // 창고키
	private String ownerky; // 판매계약사키
	private String ownrnam; // 판매계약사명칭
	private String rcvitst; // 입고아이템 상태
	private String custcod; // 고객키
	private String custnam; // 고객명
	private String fncusky; // 최종고객키
	private String skugrky; // 제품그룹키
	private String skumkey; // 제품번호
	private String skudesc;  // 제품명
	private Integer skuleng; // 길이
	private Float skuwidt; // 폭
	private Float skuthic; // 두께
	private Integer skuweig; // 무게
	private Integer grossln; // 주문길이
	private Float grosswd; // 폭
	private Float grossth; // 주문두께
	private Integer grosswg; // 주문 무게
	private Integer stlayer; // 단 위치
	private Integer vslayer; // 단위치(역순)
	private String regiorg; // 원본 목적지
	private String destorg; // 원본 상세착지
	private String regimod; // 변경 목적지
	private String destmod; // 변경 상세착지
	private String demodnm; // 변경상세착지명
	private String demdman; // 수정상세착지담당자
	private String demdadr; // 수정상세착지주소
	private String dempocd; // 수정상세착지우편번호
	private String demdtln; // 수정상세착지연락처
	private String ullocky; // 하차 포인트
	private String toareky; // to area
	private String tolocky; // to location
	private Integer tolayer; // to layer
	private String equipky; // 지시설비코드
	private String addcoky; // 할증코드
	private String skustrd; // 규격
	private String stlotnb; // Stock Lot number
	private String purcnum; // PO번호
	private String orderln; // 오더라인
	private String itemcod; // 품목코드
	private String rcvdcdt; // 입고일시
	private String rcvdctm; // 입고시간
	private Integer asndqty; // 입고예정수량
	private Integer rchsqty; // 입고완료수량
	private String suomkey; // Unit of measure
	private String suomnam; // Unit of measure 명
	private String shplimt; // 출하기한
	private String delvdsr; // 희망납기
	private String btrfstc; // 진도코드
	private String eoasnky; // 입고예정번호
	private Integer eoasnit; // 입고예정 아이템번호
	private String costrky; // 입고오더 번호
	private Integer costrit; // 입고오더 아이템 번호
	private String rcvrscd; // 입고 사유코드
	private String rcvrsnm; // 입고 사유내용
	private String rcarscd; // 입고취소 사유코드
	private String rcarsnm; // 입고취소 사유내용
	private String btrcate; // 정산유형
	private String closeyn; // 정산마감여부
	private String serinum; // 일련번호
	private String savepos; // 저장위치
	private String schdate; // 출하일정
	private String mixload; // 혼적
	private String desttrg; // 착지
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
	private String doccate; // 문서유형
	private String doctype; // 문서타입

	private String rcvitnm; // 상태명
	private String itcodnm; // 품목코드명

	private String ullocnm; //하차포인트명
	private String toarenm; //to창고동명
	private String tolocnm; //to베드명

	private String btrcnam;
	// Data
	private String fordqty; // 지시 개수
	private String taskoky; // 지시번호
	private String whnamlc; // 창고명
	private String docctnm;
	private String doctynm;
	private String ownamlc;
	private String grpkynm;
	private String asnitsn;
	private String cunamlc;
	private String reorgnm;
	private String remodnm;
	private String deorgnm;
	private String addconm;
	private String tasksts;
	private String taskstsnm;

}
