package com.kbph.logistics.om.domain;

import java.util.List;

import com.kbph.logistics.sm.domain.WstkkyDTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class OoubitDTO extends WstkkyDTO {
	private String outboky; // 출고오더번호
	private Integer outboit; // 출고오더아이템번호
	private String warekey; // 창고키
	private String poshpdt; // 포스코 출하일자
	private String poshptm; // 포스코 출하시간
	private String poshpdy; // 포스코 출하요일
	private String ownerky; // 판매계약사키
	private String custcod; // 고객키
	private String regiorg; // 원본 목적지키
	private String destorg; // 원본 상세착지키
	private String regimod; // 변경 목적지키
	private String destmod; // 변경 상세착지키
	private String demodyn; // 상세착지변경 여부
	private String demdman; // 담당자명
	private String demdadr; // 상세착지 주소
	private String dempocd; // 상세착지 우편번호
	private String demdtln; // 담당자 전화번호
	private String obomemo; // 고객 요구사항
	private String oubrqdt; // 고객 도착요청일
	private String oubrqtm; // 고객 도착요청시간
	private String skugrky; // 제품그룹키
	private String skumkey; // 제품 번호
	private String skudesc; // 제품명
	private Integer skuweig; // 중량
	private Double skuwidt; // 폭
	private Integer skuleng; // 길이
	private Double skuthic; // 두께
	private Integer grosswg; // 주문 중량
	private Double grosswd; // 주문 폭
	private Integer grossln; // 주문 길이
	private Double grossth; // 주문 두께
	private String frareky; // from 창고동
	private String frlocky; // from 베드
	private Integer frlayer; // from 단
	private String shlocky; // 상차포인트
	private String rgitqty; // 출고지시수량
	private String shcfqty; // 출고완료수량
	private String suomkey; // Default unit of measure
	private String shpplky; // 출고계획번호
	private Integer shpplit; // 출고계획아이템
	private String vhplnky; // 배차계획번호
	private Integer vhplnit; // 운송계획아이템
	private String plnsize; // 배차사이즈
	private String oboitst; // 출고오더아이템 상태
	private String itemcod; // 품목코드
	private String purcnum; // PO번호
	private String orderln; // 오더라인
	private String stlotnb; // Stock LOT Number
	private String shplimt; // 출하기한
	private String delvdsr; // 희망납기
	private String btrfstc; // 진도코드
	private String parsncd; // 작업사유코드
	private String parsnnm; // 작업사유내용
	private String rsncdnm; // 작업사유내용
	private String shpdcky;
	private Integer shpdcit;
	private String equipky; //설비키
	private String shpdcst;

	private String wplndat;
	private String wplntim;
	private String rcvdcdt;
	private String rcvdctm;
	private String rcvdcky;
	private Integer rcvdcit;

	private String whnamlc;
	private String ownrnam;
	private String custnam;
	private String fncusky; // 최종 고객사
	private String fcnamlc; // 최종 고객사 명칭
	private String rgorgnm;
	private String reorgnm;
	private String deorgnm;
	private String remodnm;
	private String rgmodnm;
	private String demodnm;
	private String fraranm;
	private String frlocnm;
	private String shlonam;
	private String tstsnam;

	private String taskoky;
	private Integer taskoit;
	private String doccate;
	private String doctype;
	private String tasksts;
	private String toareky;
	private String tolocky;
	private String btrcate;
	private String btrcnam;

	private String lotnmky;
	private String stockky;
	private String areakey;
	private String locakey;
	private Integer lolayer;
	private Integer stlayer;
	private Integer tolayer;
	private String shpitst;
	private String allgrky;
	private String tpistat;
	private String vhcfnam;
	private String tpriloc;
	private String spriloc;
	private String vehicky;
	private String stkmemo;
	private String speidat;
	private String speitim;
	private Integer shalqty;
	private String addcoky;
	private String addcnam;

	private List<String> shpplkyList;

	private String oubrqdtFrom;
	private String oubrqdtTo;
	private String pageId;
	private String addnaml;
}
