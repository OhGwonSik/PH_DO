package com.kbph.logistics.om.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author : s.h.kim
 * @version : 1.0.0
 * @since : 2024-09-19
 * @note : OmReportDTO
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-09-19					s.h.kim        						create DTO class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OmReportDTO extends CommonColumnDTO {

	// P.K
	private String outboky; // 오더번호
	private String shpdcky; // 출고문서번호 (송장번호)
	private String warekey; // 창고키
	private String vownkey; // 차주키
	private String vehicky; // 차량코드
	private String skumkey; // 제품번호
	private String ownerky; // 판매계약사

	// 출고지시서
	// page head
	private String obodate; // 오더지시일자
	private String obotime; // 오더지시시간
	private String shlocnm; // 상차포인트 명

	// detail
	private String frareky; // from 창고동
	private String frlocky; // from 베드
	private String skarloc; // 제품 위치
	private int frlayer; // 제품 단
	private String shlocky; // 정차포인트
	private String equinam; // 지시설비명칭
	private String custcod; // 고객키
	private String reorgnm; // 원본목적지
	private String deorgnm; // 원본상세착지
	private String remodnm; // 변경목적지
	private String demdadr; // 상세착지 주소

	// param
	private int quancnt; // 총 수량
	private int weigsum; // 총 중량

	// 출고송장
	// page head
	private String skudesc; // 품명
	private String vonamlc; // 기사성명
	private String regimod;
	private String destmod;
	private String denamlc;
	private int itemcnt; // 총 수량
	private int skwgsum; // 중량 N
	private int grwgsum; // 중량 G
	private String spehdat; // 출고일시
	private String renamlc; // 목적지

	// detail
	private String orderln; // 오더라인번호
	private String purcnum; // PO번호
	private String stlotnb; // Stock LOT Number
	private String delvdsr; // 납기
	private int grosswg; // 주문중량
	private int grossln; // 주문 길이
	private Float grosswd; // 주문 폭
	private Float grossth; // 주문두께
	private String savepos; // 저장위치
	private String skustrd; // 규격
	private int indimet; // 내경
	private int outdimt; // 외경

	// data
	private String proofyn; // 수요가증빙용 출력여부

	// 공통
	private String whnamlc; // 창고
	private String vhcfnam; // 차량번호
	private String ownrnam; // 판매계약사명
	private String fncusky; // 최종고객사명
	private String cunamlc; // 고객명
	private String demodnm; // 변경상세착지
	private String warteln; // 문의처
	private int skuweig; // 중량
	private int skuleng; // 길이
	private Float skuwidt; // 폭
	private Float skuthic; // 두께
	private String grpkynm; // 제품그룹명
	private int rownum; // 행번호
	private String type; // 구분 type (jasper 파일 이름)
	private String printdate; // 인쇄일

	// 프린트 로그
	private String printsq; // 인쇄이력번호
	private String progrid; // 프로그램 ID
	private String doccate; // 문서유형
	private String doctype; // 문서타입
}