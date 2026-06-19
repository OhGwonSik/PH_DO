package com.kbph.logistics.om.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WplnitDTO extends CommonColumnDTO {
	// P.K.
	private String shpplky;
	private int shpplit;

	// Columns
	private String doccate;
	private String doctype;
	private String warekey;
	private String poshpdt;
	private String poshptm;
	private String poshpdy;
	private String ownerky;
	private String stockky;
	private String regikey;
	private String destkey;
	private String regiorg;
	private String destorg;
	private String skugrky;
	private String grpkynm;
	private String regimod;
	private String custcod;
	private String custnam;
	private String ownrnam;
	private String fncusky;
	private String fcnamlc;
	private String destmod;
	private String demdman;
	private String deorgnm;
	private String demodnm;
	private String demodyn;
	private String demdadr;
	private String dempocd;
	private String demdtln;
	private String skumkey;
	private String skudesc;
	private String tpriloc;
	private String spriloc;
	private String emskuyn;
	private String chkdisp;
	private int skuweig;
	private String skuwidt;
	private String skuleng;
	private String skuthic;
	private String grosswd;
	private String grossln;
	private String grossth;
	private String grosswg;
	private String planqty;
	private String suomkey;
	private String itemcod;
	private String stlotnb;
	private String purcnum;
	private String orderln;
	private String shplimt;
	private String delvdsr;
	private String btrfstc;
	private String wplitst;
	private String stkmemo;
	private String rcvdcdt;
	private String rcvdctm;
	private String rcvdcky;
	private Integer rcvdcit;

	//data
	private String vhplnky;
	private Integer alcweig;
	private String plnsize;
	private String wplcfyn;
	private String vehicky;
	private String tpnstat;
	private String wpldate;
	private String wpltime;
	private String shpweig;
	private String remweig;
	private String wplstat;
	private String vhplnit;
	private String destord;
	private String tpistat;
	private String tpistnm;
	private String obitqty;
	private String tritqty;
	private String demdnam;
	private String addcoky;
	private String btrcate;
	private String eoasnky;
	private String eoasnit;
	private String trncpdt;
	private String trncptm;
	private String vhcfnam;
	private String btrcnam;
	private String reorgnm;
	private String remodnm;
	private String suomnam;
	private String areakey;
	private String locakey;
	private Integer lolayer;
	private Integer stlayer;
	private String frareky;
	private String frlocky;
	private String frlayer;
	private Integer rgitqty;
	private String whnamlc;
	private String wplitsn;
	private String ronamlc;
	private String rsncdnm;
	private String addcnam;
}
