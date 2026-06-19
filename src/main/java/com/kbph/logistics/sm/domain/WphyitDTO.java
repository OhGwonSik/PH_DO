package com.kbph.logistics.sm.domain;

import java.util.List;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WphyitDTO extends CommonColumnDTO{

	private String physoky;
	private Integer physoit;
	private String warekey;
	private String doccate;
	private String docctnm;
	private String doctype;
	private String doctynm;
	private String phyname;
	private String phystat;
	private String phystnm;
	private String phymode;
	private String phygrky;
	private String stockky;
	private String lotnmky;
	private String areakey;
	private String areanam;
	private String locakey;
	private String locanam;
	private Integer stlayer;
	private String custcod;
	private String custnam;
	private String ownerky;
	private String ownrnam;
	private String parsncd;
	private String parsnnm;
	private String skumkey;
	private String skudesc;
	private String suomkey;
	private Integer systqty;
	private Integer physqty;
	private Integer compqty;
	private String phydttm;// api용 수정일시
	private String oldowky;

	private String regiorg;
	private String destorg;
	private String regimod;
	private String destmod;
	private String whnamlc;

	private String btrcate; // 정산유형
	private String btrfstc; // 진도코드
	private String skugrky; // 제품그룹키
	private String addcoky; // 할증코드
	private String btrcnam;
	private String itemcod;

	private String emskuyn; //긴급재여부
	private String stkmemo; //제품메모
	private String rmstkyn;//제품삭제여부
	private String retunyn;//반납여부

	private String poshpdt; // 포스코출하일자
	private String poshptm; // 포스코출하시간
	private String poshpdy; // 포스코출하요일

	private String purcnum; // PO번호
	private String orderln; // 오더라인

	private Integer skuweig;
	private Double skuwidt;
	private Integer skuleng;
	private Double skuthic;

	private Integer grosswg; // 주문 중량
	private Double grosswd; // 주문 폭
	private Integer grossln; // 주문 길이
	private Double grossth; // 주문 두께

	private String shplimt; // 출하기한
	private String delvdsr; // 희망납기
	private String rcvdcdt; //입고일자
	private String rcvdctm; //입고시간
	private String rcvdcky;
	private String rcvdcit;

	private Integer lolayer;
	private String frareky;
	private String frlocky;
	private Integer frlayer;
	private String skustat;

	private String fncusky; // 최종고객사

	private String rcvitst;

	private String demdman;  //VARCHAR(60) - 담당자명
	private String demdadr;  //VARCHAR(100) - 상세착지 주소
	private String demdtln;  //VARCHAR(20) - 담당자 전화번호
	private String dempocd; //VARCHAR(10) - 상세착지 우편번호

	private String orgarea; // 기존 창고동
	private String orgloca; // 기존 베드
	private Integer orgslay; // 기존 단
	private String orgarnm; // 기존 창고동명
	private String orglcnm; // 기존 베드명

	private String adjsoky;
	private Integer adjsoit;

	private String reorgnm;
	private String remodnm;
	private String deorgnm;
	private String demodnm;
	private String grpkynm;
	private String addconm;
	private String comcdtx;

	private Integer tmpupck; //다른 테이블 업데이트 체크용

	private List<String> custkeyList;
	private List<String> regikeyList;
	private List<String> phystatList;
	private List<String> skumkeyList;
	private List<String> orderlnList;
}
