package com.kbph.logistics.tm.domain;

import java.util.List;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TmAppDTO extends CommonColumnDTO{

	//app
	private String useract;
	private String schemaName;

	//운송
	private String vhplnky;

	//list
	private List<TmAppDTO> tpnstatList;
	private List<TmAppDTO> aprovynList;
	private List<TmAppDTO> alarmCheckList;
	
	//mdesma
	private String desmemo;
	
	//marema
	private String areanam;
	
	private String grouparea;

	//mcodem
	private String combovl;
	private String combonm;
	private String comcdky;
	private String comcdvl;
	
	//mlocma
	private String locakey;
	private String locanam;

	//mregma
	private String regikey;
	private String renamlc;
	private String regaddr;
	private String reuseyn;

	//schema
	private List<String> schemaList;
	private String schema;
	private String commonSchema;

	//susrvh
	private String userkey;
	private String vhcsnam;
	private String vhcfnam;
	private String drvernm;
	private String vehicky;
	private String vownkey;

	//fcm token - alarm
	private String fcmtokn;
	private String targetToken;
	private String alarmti;
	private String alarmdt;
	private String type;
	private String areakey;
	private String destination;
	private String alarmky;
	private String alrmcod;

	//alarm
	private String whnamlc;
	private String comcdtx;

	//wasnhd
	private String eoasnky;
	private String entdate;
	private String enttime;
	private String asndate;
	private String asndateFrom;
	private String asndateTo;
	private String asnpiyn;
	private int headUpdtchk;
	private String asnityn;
	private String asnstat;

	//wasnit
	private int eoasnit;
	private String cunamlc;
	private int stlayer;
	private int asndqty;
	private int itemUpdtchk;

	//wplnit
	private String shpplky;
	private int shpplit;
	private String wplitst;
	private String doccate;
	private String doctype;
	private String warekey;
	private String ownerky;
	private String ownrnam;
	private String stockky;
	private String destorg;
	private String regiorg;
	private String skugrky;
	private String regimod;
	private String destmod;
	private String custcod;
	private String custnam;
	private String custeln;
	private String skumkey;
	private String skudesc;
	private String emskuyn;
	private String chkdisp;
	private int skuweig;
	private double skuwidt;
	private int skuleng;
	private double skuthic;
	private double grosswd;
	private int grossln;
	private double grossth;
	private int grosswg;
	private int planqty;
	private String suomkey;
	private String itemcod;
	private String purcnum;
	private String orderln;
	private String shplimt;
	private String delvdsr;
	private String stkmemo;

	//wplnhd
	private String destkey;
	private int shpweig;
	private int remweig;
	private String wpldate;
	private String wpltime;
	private String rsncode;
	private String parsnnm;

	//ooubhd
	private String outboky;
	private String obodate;
	private String obotime;
	private String obostat;

	//wshphd
	private String shpdcky;
	private String docctnm;
	private String doctynm;
	private String shpdcst;
	private String wplndat;
	private String shpsday;
	private String spehdat;
	private String wplndatFrom;
	private String wplndatTo;
	private String shpsdayFrom;
	private String shpsdayTo;

	//wshpit
	private String demodnm;
	private String demdadr;
	private String demdman;
	private String dempocd;
	private String demdtln;
}
