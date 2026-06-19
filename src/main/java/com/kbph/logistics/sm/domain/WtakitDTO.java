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
public class WtakitDTO extends CommonColumnDTO {
	// P.K.
	private String taskoky;
	private Integer taskoit;

	// Columns
	private String warekey;
	private String taskdat;
	private String doccate;
	private String doctype;
	private String tasksts;
	private String lotnmky;
	private String ownerky;
	private String custcod;
	private String skumkey;
	private String skudesc;
	private String frareky;
	private String frlocky;
	private Integer frlayer;
	private String toareky;
	private String tolocky;
	private String ullocky;
	private Integer tolayer;
	private Integer fordqty;
	private Integer tcmpqty;
	private String allgrky;
	private String mvstdat;
	private String mvsttim;
	private String mveddat;
	private String mvedtim;
	private String parsncd;
	private String parsnnm;
	private String pkrsncd;
	private String pkrsnnm;
	private String stockky;
	private String eoasnky;
	private Integer eoasnit;
	private String costrky;
	private Integer costrit;
	private String rcvdcky;
	private Integer rcvdcit;
	private String outboky;
	private Integer outboit;
	private String shpdcky;
	private Integer shpdcit;

	private String btrcate;
	private String addcoky;
	private String btrcnam;
	private String addconm;

	private String rcarscd; // 입고취소사유코드
	private String rcarsnm; // 입고취소사유내용

	private String ownrnam;
	private String custnam;

	private String emskuyn; //긴급재여부
	private String stkmemo; //제품메모

	private String vhplnky;
	private String equipky; //설비키
	private String equinam;
	private String shpcfyn; //출고완료확정여부

	// Data
	private String regiorg;
	private String rgorgnm;
	private String regimod;
	private String rgmodnm;
	private String destorg;
	private String deorgnm;
	private String destmod;
	private String demodnm;
	private String whnamlc;
	private String docctnm;
	private String doctynm;
	private String tstsnam;
	private String ownamlc;
	private String cunamlc;
	private String fraranm;
	private String frlocnm;
	private String toaranm;
	private String tolocnm;
	private String rsncdnm;
	private String renamlc;
	private String denamlc;
	private String areakey;
	private String locakey;
	private String lolayer;
	private String stlayer;

	private Integer stotqty; // INT(11) NOT NULL - 총재고수량
	private Integer sallqty; // INT(11) NOT NULL - 할당수량
	private Integer sbloqty; // INT(11) NOT NULL - 블락수량
	private Integer planqty; // INT(11) 출고예정수량
	private Integer plcmqty; // INT(11) NOT NULL - 출고예정확정 수량

	private Integer tsitcnt; //작업 아이템 갯수
	private List<String> taskstsList;
	private List<String> custkeyList;
	private List<String> regikeyList;
	private List<String> taskstsCheckedList;
	private List<String> equipkyList;
	private List<String> skumkeyList;
	private Integer tmpupck; //다른 테이블 업데이트 체크용
	private String tpriloc;
	private String frarenm;
	private String areanam;

	private boolean physicalCreateDocFlag; //실사생성 플래그

	//doe050
	private String shpdcst;
	private String shpitst;
	private String obostat;
	private String oboitst;
	//doe051
	private Integer totitem;
	private Integer totweig;
	private String taskdatfrom;
	private String taskdatto;

	//api
	private String vehicky;
	private String vhcfnam;

}