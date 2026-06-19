package com.kbph.logistics.sm.domain;

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
public class WstkkyDTO  extends CommonColumnDTO {
	private String stockky; // VARCHAR(10) NOT NULL - 재고키
	private String warekey; // VARCHAR(20) NOT NULL - 창고키
	private String areakey; // VARCHAR(20) NOT NULL - 창고동키
	private String locakey; // VARCHAR(20) NOT NULL - 창고동키
	private Integer lolayer; // INT(11)  NOT NULL - 단 위치
	private String lotnmky; // VARCHAR(10) NOT NULL - LOT NUMBER
	private String ownerky; // VARCHAR(20) NOT NULL - 판매계약사키
	private String custcod; // VARCHAR(20) NOT NULL - 고객키
	private String fncusky; // 최종고객사
	private String skugrky; //제품그룹키
	private String skumkey; // VARCHAR(50) NOT NULL - 제품번호
	private String skudesc; // VARCHAR(100) NOT NULL - 제품명
	private Integer skuweig; // DECIMAL(10,3) NOT NULL - 중량
	private Double skuwidt; // INT(11) NOT NULL - 폭
	private Integer skuleng; // INT(11) NOT NULL - 길이
	private Double skuthic; // INT(11) NOT NULL - 두께
	private Integer grosswg; // 주문 중량
	private Double grosswd; // 주문 폭
	private Integer grossln; // 주문 길이
	private Double grossth; // 주문 두께
	private String stlotnb; // VARCHAR(20) NOT NULL - Stock LOT Number
	private String purcnum; // VARCHAR(50) NOT NULL - PO번호
	private String orderln; // VARCHAR(20) NOT NULL - 오더라인
	private String regiorg; // VARCHAR(20) NOT NULL - 원본 목적지키
	private String destorg; // VARCHAR(20) NOT NULL - 원본 상세착지키
	private String regimod; // VARCHAR(20) NOT NULL - 변경 목적지키
	private String destmod; // VARCHAR(20) NOT NULL - 변경 상세착지키
	private String itemcod; // VARCHAR(20) NOT NULL - 품목코드
	private Integer stotqty; // INT(11) NOT NULL - 총재고수량
	private Integer taskqty; // INT(11) NOT NULL - 작업수량
	private Integer sallqty; // INT(11) NOT NULL - 할당수량
	private Integer sbloqty; // INT(11) NOT NULL - 블락수량
	private Integer planqty; // INT(11) 출고예정수량
	private Integer plcmqty; // INT(11) NOT NULL - 출고예정확정 수량
	private String addcoky; // 할증코드
	private String suomkey; // VARCHAR(10) NOT NULL - Default unit of measure
	private String rcvdcdt; // VARCHAR(8) - 입고일자
	private String lotat01; // VARCHAR(20) - 재고속성01
	private String lotat02; // VARCHAR(20) - 재고속성02
	private String lotat03; // VARCHAR(20) - 재고속성03
	private String stkstat; // VARCHAR(10) NOT NULL - 재고상태
	private String blockid; // VARCHAR(20) - 블락ID
	private String rcvdcky; // VARCHAR(10) - 입고문서번호
	private Integer rcvdcit; // INT(11) - 입고문서아이템
	private String adjsoky; // VARCHAR(10) - 조정문서번호
	private Integer adjsoit; // INT(11) -  조정문서아이템
	private String physoky; // VARCHAR(10) - 실사문서번호
	private Integer physoit; // INT(11) -  실사문서아이템
	private String shpdcky; // VARCHAR(10) - 출고문서번호
	private Integer shpdcit; // INT(11) -  출고문서아이템
	private String taskoky; // VARCHAR(10) - 작업문서번호
	private Integer taskoit; // INT(11) -  작업문서아이템
	private String demdman;  //VARCHAR(60) - 담당자명
	private String demdadr;  //VARCHAR(100) - 상세착지 주소
	private String demdtln;  //VARCHAR(20) - 담당자 전화번호
	private String dempocd; //VARCHAR(10) - 상세착지 우편번호
	private String deorgnm; // 원본착지명
	private String demodnm;  //VARCHAR(60) - 변경착지명
	private String custnam; // 고객명
	private String ownrnam; // 판매계약사명
	private String poshpdt; // 포스코출하일자
	private String poshptm; // 포스코출하시간
	private String poshpdy; // 포스코출하요일

	private String skustat; //제품상태

	private String demodyn; // 착지 변경여부
	private String emskuyn; //긴급재여부
	private String stkmemo; //제품메모
	private String equipky; // 설비키

	private String btrcate; //정산유형

	private String rcvdctm;
	private String desaddr;

	private String inprscd; // 입력 사유코드
	private String inprsnm; // 입력 상세사유
	//명칭
	private String whnamlc;
	private String areanam;
	private String locanam;
	private String ownamlc;
	private String cunamlc;
	private String renamlc;
	private String denamlc;
	private String statnam;
	private String skustnm; // 제품상태명
	private String reorgnm;
	private String remodnm;
	private String fcnamlc;
	private String grpkynm;
	private String addconm;
	private String btrcnam;

	//WTAKIT 작업(선별)테이블
	private String tasksts; //작업 상태
	private String toareky; // VARCHAR(20) NOT NULL - TO창고동
	private String tolocky; // VARCHAR(20) NOT NULL - TO베드
	private Integer tolayer; // INT(11) - TO단
	private Integer fordqty; // INT(11) - NOT NULL - 지시수량
	private String parsncd; // VARCHAR(10) - 작업(선별)사유
	private String parsnnm; // VARCHAR(200) - 작업(선별)상세사유
	private String doccate;
	private String doctype;


	private String phystat;
	private String frareky;
	private String frlocky;
	private Integer frlayer;
	private String phymode;
	private String phyname;
	private String phygrky;
	private Integer physqty;
	private Integer compqty;

	private Integer stlayer;

	private String btrfstc; // 진도코드
	private String shplimt; // 출하기한
	private String delvdsr; // 희망납기

	//wadjit
	private String adjdcst;
	private String adjitst;
	private String adjmode;
	private String adjsrmk;
	private String adjgrky;
	private String rsncdnm;

	private Integer tmpupck;

	private String skuweigFrom;
	private String skuweigTo;
	private String skuwidtFrom;
	private String skuwidtTo;
	private String shplimtFrom;
	private String shplimtTo;

	private String skulengFrom;
	private String skulengTo;

	private String regikey;
	private String destkey;
	private String plnsize;

	private String oldowky; // 조정에서 판매고객이 바뀔때 사용

	//wphyit
	private String rmstkyn; //제품삭제여부 (제품삭제)
	private String retunyn; //제품반납여부 (제품삭제)
	private String phydttm;// api용 수정일시

	private String comcdtx; //api용 품목코드명

	private List<Integer> lolayerList;
	private List<String> locakeyList;
	private List<String> regikeyList;
	private List<String> custkeyList;
	private List<WstkkyDTO> checkList;
	private List<String> skumkeyList;
	private List<String> orderlnList;
	private List<String> purcnumList;
	private List<String> itemcodList;
}
