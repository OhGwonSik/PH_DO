package com.kbph.logistics.tm.domain;

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
public class TplnitDTO extends CommonColumnDTO{

	private String vhplnky;
	private String warekey;
	private String whnamlc;
	private int vhplnit;
	private String custcod;
	private int destord;
	private String destkey;
	private String plnsize;
	private int plnweig;
	private String ownerky;
	private String ownamlc;
	private String skumkey;
	private String skudesc;
	private double skuthic;
	private double skuwidt;
	private int skuleng;
	private int skuweig;
	private int tritqty;
	private String itemcod;
	private String emskuyn;
	private String grpkynm;
	private String regiorg;
	private String regimod;
	private String tpistat;
	private String comcdtx;
	private String denamlc;
	private String cunamlc;
	private String trncpdt;
	private String trncptm;
	private int planqty;
	private int obitqty;
	private String skugrky;
	private double grosswd;
	private int grossln;
	private double grossth;
	private int grosswg;
	private String purcnum;
	private String orderln;
	private String suomkey;
	private String shpplky;
	private int shpplit;
	private String btrcate;
	private String addcoky;
	private String addnaml;
	private String destorg;
	private String destmod;
	private String demodnm;
	private String demdman;
	private String dempocd;
	private String demdadr;
	private String demdtln;
	private String desmemo;
	private String stockaddr;
	private String poshpdt;
	private String poshptm;
	private String poshpdy;
	private String custnam;
	private String ownrnam;
	private String deorgnm;
	private String stkmemo;
	private String shplimt;
	private String delvdsr;
	private String btrfstc;
	private String btrcnam;
	private String stockky;
	private String bildate;
	private String rgorgnm;
	private String rgmodnm;
	private String demodyn;
	private String rcvdcdt;
	private String rcvdctm;
	private String rcvdcky;
	private Integer rcvdcit;
	private String fncusky;
	private String fcnamlc;
	private String lotat01;
	private String lotat02;
	private String lotat03;

	//mcusma
	private String custeln;

	//app
	private List<String> schemaList;
	private String schema;
	private String useract;

	//WSTKKY
	private String lotnmky;
	private String areakey;
	private String locakey;
	private int lolayer;
	private int stotqty;
	private String upinfst;
	private int stlayer;
	private String skustat;
	private String stlotnb;

	private String pinamlc;
	private String ronamlc;
	private String rmnamlc;
	private String itemmlc;
	private String plnamlc;
	private String sknamlc;
	private String donamlc;
	private String dmnamlc;
	private String tinamlc;
	private String renamlc;
	private List<TplnitDTO> updateTpistatList;
	private String pnszmlc;

	// data
	private Integer alcweig;
	private String wplcfyn;
	private String vehicky;
	private String tpnstat;
	private String wpldate;
	private String wpltime;
	private String shpweig;
	private String remweig;
	private String wplstat;
	private String tpriloc;
	private String spriloc;
	private String doccate;
	private String doctype;
	private String regikey;
	private String wplstnm;
	private String tpistnm;
	private String wplitst;
	private String addcnam;
	private String savepnt;
	private String postpdt;
	private String postpdy;
	private String postptm;
}
