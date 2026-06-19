package com.kbph.logistics.im.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author : OP
 * @version : 1.0.0
 * @since : 2024-08-01
 * @note : WrcvitDTO ================================================== DATE
 *       AUTHOR NOTE
 *       --------------------------------------------------------------------------------------
 *       2024-08-01 OP create DTO class 2024-08-30 t.s.park edit field
 *       --------------------------------------------------------------------------------------
 *       ==================================================
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WasnitDTO extends CommonColumnDTO {
	// P.K.
	private String eoasnky; // 입고예정번호 VARCHAR(10)

	// Columns
	private String warekey; // 창고키 VARCHAR(20)
	private Integer eoasnit; // 입고예정아이템번호 INT(11)
	private String poshpdt; // 포스코 출하일자
	private String poshptm; // 포스코 출하시간
	private String poshpdy; // 포스코 출하요일
	private String invoice; // 송장번호
	private String ownerky; // 판매계약사키
	private String ownrnam; // 판매계약사명칭
	private String asnstat; // 입고예정헤더상태코드
	private String asnitst; // 입고예정아이템상태코드
	private String skugrky; // 제품그룹키
	private String skumkey; // 제품코드
	private String skudesc; // 제품명
	private Integer stlayer; // 적재단
	private Integer skuleng; // 길이
	private Float skuwidt; // 폭
	private Float skuthic; // 두께
	private Integer skuweig; // 무게
	private Integer grossln; // 주문길이
	private Float grosswd; // 폭
	private Float grossth; // 주문두께
	private Integer grosswg; // 주문 무게
	private String suomkey; // 단위
	private String skustrd; // 규격
	private String addcoky; // 할증코드
	private Integer asndqty; // 입고예정수량
	private Integer rchsqty; // 입고완료수량
	private String custcod; // 고객사코드
	private String custnam; // 고객사명칭
	private String fncusky; // 최종고객사코드
	private String regiorg; // 원본목적지
	private String destorg; // 원본상세착지
	private String regimod; // 수정목적지
	private String destmod; // 수정상세착지
	private String demodnm; // 변경상세착지명
	private String demdman; // 수정상세착지담당자
	private String demdadr; // 수정상세착지주소
	private String dempocd; // 수정상세착지우편번호
	private String demdtln; // 수정상세착지연락처
	private String ullocky; // 하차포인트
	private String frareky; // from창고동
	private String toareky; // to창고동
	private String tolocky; // to베드
	private Integer tolayer; // to단
	private String itemcod; // 품목코드
	private String purcnum; // 고객 PO번호
	private String orderln; // 오더라인
	private String stlotnb; // Stock lot number
	private String btrfstc; // 진도코드
	private String shplimt; // 출하기한
	private String delvdsr; // 희망납기
	private String skustat; // 제품상태
	private String rsncode; // 사유코드
	private String parsnnm; // 작업사유내용
	private String rcarscd; // 작업취소사유
	private String rcarsnm; // 작업취소사유내용
	private String emskuyn; // 긴급재여부
	private String restkyn; // 재입고여부
	private String costrky; // 입고오더번호
	private Integer costrit; // 입고오더아이템번호
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
	private String preprde; // 전처리착지
	private String arvdate; // 양하도착예정
	private String shpodat; // 출하지시일시
	private String trsodat; // 운송지시일시
	private String indimet; // 내경
	private String outdimt; // 외경
	private String stkmemo; // 제품메모
	private String strstat; // 입고오더헤더상태코드
	private int exlayer; // 예상 단

	private String asnityn; // WASNHD 입고검수확정여부
	// data
	private String whnamlc; // 창고명
	private String ownamlc; // 판매계약사명
	private String cunamlc; // 고객사명
	private String grpkynm; // 제품그룹명
	private String suomnam; // 단위명
	private String rsncdnm; // 사유코드명
	private String rscate1; // 사유분류
	private String itcodnm; // 아이템코드명
	private String rgorgnm; // 원본목적지명
	private String deorgnm; // 원본상세착지명
	private String rgmodnm; // 변경목적지명
	private String skustnm; // 제품상태명
	private String areanam; // 창고동명
	private String locanam; // 베드명
	private String ullocnm; // 하차포인트명
	private String toarenm; // to 창고동명
	private String tolocnm; // to 베드명
	private String asnitsn; // 입고예정정보아이템상태
	private String stritst; // 입고오더상태코드
	private Integer vslayer; // 보여주기용 단수(역순) 임시
	private Integer lolayer; // 실제 적치 베드 단
	private String equipky; // 지시설비코드
	private List<String> asnitstList; // asn 아이템 상태 리스트
	private List<String> taskstsList;
	private String isStrategy; // 전략반영여부
	private String btrcnam; // 정산유형명
	private String fncusnm; // 최종고객사명
	private String addconm; // 할증기준코드명칭


	// 입고예정-> 입고+ 재고용
	private String rcvdcky;
	private Integer rcvdcit;
	private String taskoky;
	private Integer taskoit;
	private String doccate;
	private String doctype;
	private String docctnm;
	private String doctynm;
	private String tasksts;
	private String rcvdcdt;
	private String rcvdctm;

	private String stockky;
	private String lotnmky;

	// API
	private String vehicky; // 차량코드
	private String cncldat; // 입고예정취소일시
	private String loctype; // 베드 타입
	private String useract;
	private String schema;
	private String vhcfnam; // 차량번호
	private String asncfyn; // 예정확정여부
}
