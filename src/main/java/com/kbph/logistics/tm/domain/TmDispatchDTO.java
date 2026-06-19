package com.kbph.logistics.tm.domain;

import java.util.List;

import com.kbph.logistics.common.domain.ComboDataDTO;
import com.kbph.logistics.common.domain.CommonColumnDTO;
import com.kbph.logistics.md.domain.McodemDTO;
import com.kbph.logistics.md.domain.MdocmaDTO;
import com.kbph.logistics.md.domain.MgrpmaDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TmDispatchDTO extends CommonColumnDTO {

	private List<MdocmaDTO> doctypeList;
	private List<McodemDTO> tpnstatList;
	private List<McodemDTO> aprovynList;
	private List<McodemDTO> tpistatList;
	private List<TplnhdDTO> updateTpnstatList;
	private List<TplnitDTO> updateTpistatList;
	private List<ComboDataDTO> destkeyList;
	private List<TmDispatchDTO> headGridCheckList;
	private List<TmDispatchDTO> itemGridCheckList;
	private List<TmDispatchDTO> mlocmaList;
	private List<TmDispatchDTO> searchList;
	private List<String> shpplkyList;
	private List<MgrpmaDTO> plnsizeList;
	private List<String> regikeySelectedList;
	private List<TmDispatchDTO> stockkyList;
	private List<TmDispatchDTO> emergencyList;

	//WPLNHD
	private String shpplky;
	private String warekey;
	private String regikey;
	private int shpweig;
	private String skugrky;
	private String wpldate;
	private String wpltime;
	private String doccate;
	private String doctype;
	private String wplstat;
	private String chkdisp;
	private String denamlc;
	private String desaddr;

	//WPLNIT
	private Integer shpplit;
	private String custcod;
	private String ownerky;
	private String regiorg;
	private String regimod;
	private String destorg;
	private String destmod;
	private String skumkey;
	private String skudesc;
	private int skuleng;
	private double skuwidt;
	private double skuthic;
	private int skuweig;
	private int planqty;
	private String suomkey;
	private String itemcod;
	private String purcnum;
	private String orderln;
	private String emskuyn;
	private double grosswd;
	private int grossln;
	private double grossth;
	private int grosswg;
	private String destkey;
	private String btrcate;
	private String btrcnam;
	private String addcoky;
	private String addnaml;
	private String custnam;
	private String ownrnam;
	private String deorgnm;
	private String stkmemo;
	private String demdman;
	private String dempocd;
	private String shplimt;
	private String delvdsr;
	private String btrfstc;
	private String wplitst;
	private String wplnamc;
	private String stockky;

	//MVHCMA
	private String vehicky;
	private String conamlc;
	private String dlnamlc;
	private String vhcsnam;
	private String vhcfnam;
	private String vtnamlc;
	private String cdnamlc;
	private int vhcmaxw;
	private String drvernm;
	private String drverph;
	private String vhcvryn;
	private String vhcmemo;
	private String stnamlc;
	private String vownkey;
	private String vonamlc;
	private String vhctype;
	private String dmwplnm;

	//SUSRMA
	private String usernam;
	private String telphnm;
	private String useract;
	private String userkey;

	//MLOCMA
	private String loctype;
	private String locstat;
	private String locanam;
	private String locakey;
	private int lolayer;

	private String plnsize;
	private Integer szgubun;

	//WSTKKY
	private String poshpdt;
	private String poshptm;
	private String poshpdy;
	private String demodnm;
	private String demdadr;
	private String demdtln;
	private String stockaddr;
	private String areakey;
	private String lotnmky;
	private String savepnt;
	private String fncusky;
	private String fcnamlc;
	private String demodyn;
	private int stotqty;
	private String rcvdcdt;
	private String rcvdctm;
	private String lotat01;
	private String lotat02;
	private String lotat03;
	private String stkstat;
	private String rcvdcky;
	private int rcvdcit;
	private String stlotnb;
	private String upinfst;

	//custom
	private int maxlayer;
	private String pageId;
	private boolean state;
	private int remweig;
	private String whnamlc;
	private String renamlc;
	private String grpkynm;
	private String docctnm;
	private String doctynm;
	private String wplamlc;
	private String cunamlc;
	private String ownamlc;
	private String ronamlc;
	private String rmnamlc;
	private String itemmlc;
	private String wpldateFrom;
	private String wpldateTo;
	private String donamlc;
	private String dmnamlc;
	private int emerweig;
	private int odweig;
	private double totalweig;
	private List<ComboDataDTO> regikeyList;
	private String schema;
	private String commonSchema;

	private String eoasnky;
	private String outboky;
	private String vhplnky;
	private int vhplnit;
	private String rowkey;
	private int stlayer;
	private String skustat;
}
