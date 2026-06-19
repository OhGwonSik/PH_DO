package com.kbph.logistics.om.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-09-06
 * @note : OutboundOrderForTreeGridDTO
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
public class OutboundOrderForTreeGridDTO extends CommonColumnDTO{
	//header
	private String shpplky; // 출고계획번호
	private String warekey; // 창고키
	private String custcod; // 고객사코드
	private String fncusky; // 최종고객사키
	private String regikey; // 목적지키
	private String destkey; // 상세착지키
	private String desaddr; // 상세착지주소
	private Integer shpweig; // 출고톤수
	private String skugrky; // 제품그룹키
	private String wpldate; // 출고예정일자
	private String wpltime; // 출고예정시간
	private String doccate; // 문서유형
	private String doctype; // 문서타입
	private String wplstat; // 계획상태
	private String chkdisp; // 배차신청여부
	private String rsncode; // 사유코드
	private String parsnnm; // 사유코드
	private Integer remweig; // 잔여중량

	// item
	private Integer shpplit; // 출고계획아이템
	private String ownerky; // 판매계약사키
	private String regiorg; // 원본목적지
	private String destorg; // 원본상세착지
	private String regimod; // 수정목적지
	private String destmod; // 수정상세착지
	private String demodnm; // 수정상세착지명
	private String skumkey; // 제품번호
	private String skudesc; // 제품명
	private String tpriloc; //우선정차베드
	private String spriloc; //차선정차베드
	private Integer skuweig; // 중량
	private Double skuwidt; // 폭
	private Integer skuleng; // 길이
	private Double skuthic; // 두께
	private Integer grosswg; // 주문중량
	private Double grosswd; // 주문폭
	private Integer grossln; // 주문길이
	private Double grossth; // 주문두께
	private Integer planqty; //출고예정수량
	private String itemcod; // 품목코드
	private String suomkey; // 단위
	private String stlotnb; // Stock lot number
	private String purcnum; // 고객 PO번호
	private String orderln; // 오더라인
	private String btrfstc; // 진도코드
	private String shplimt; // 출하기한
	private String delvdsr; //희망기한
	private String emskuyn; // 긴급재 여부
	private String stkmemo; // 제품 메모
	private String restkyn; // 재입고 여부
	private String wplitst; // 출고예정 아이템 상태
	private String deorgnm; //원본상세착지명
	private String demdman; //담당자명
	private String demdadr; //주소
	private String dempocd; //우편번호
	private String demdtln; //상세착지 연락처
	private String custnam; //고객사이름
	private String fcnamlc; //최종고객사명칭
	private String denamlc; // 도착지명
	private String rcvdcdt; // 입고일자
	private String rcvdctm; // 입고시간
	private String rcvdcky; // 입고문서번호
	private String rcvdcit; // 입고문서아이템번호

	// tree node
	private List<OutboundOrderForTreeGridDTO> children;

	//sum skuweig
	@JsonProperty("pq_child_sum")
	private PqChildSumDTO pqChildSum;

	// Data
	private String stockky;
	private String rsncdnm; // 작업사유내용

}
