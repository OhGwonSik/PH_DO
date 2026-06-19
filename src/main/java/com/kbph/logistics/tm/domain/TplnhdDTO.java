package com.kbph.logistics.tm.domain;

import java.util.List;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TplnhdDTO extends CommonColumnDTO {

	//list
	private List<String> tpnstatCheckedList;
	private List<TplnhdDTO> checkList;
	private List<TplnhdDTO> updateTpnstatList;
	private List<String> regikeySelectedList;
	private List<String> dates;
	private List<TmDispatchDTO> orderGridCheckedList;
	private List<TplnitDTO> emergencyList;
	private List<TplnhdDTO> headGridCheckedList;
	private List<TplnhdDTO> searchList;
	private List<TmDispatchDTO> mlocmaList;

	//tplnhd
	private String warekey;
	private String whnamlc;
	private String vhplnky;
	private String vownkey;
	private String vonamlc;
	private String vehicky;
	private String vhcfnam;
	private String vhcsnam;
	private String vhctncd;
	private String doccate;
	private String doctype;
	private String docctnm;
	private String doctynm;
	private String loadkey;
	private String comcdtx;
	private String lonamlc;
	private String plnamlc;
	private String regikey;
	private String renamlc;
	private String tpnstat;
	private String trnmemo;
	private String mixcuyn;
	private String vhpdate;
	private String vhptime;
	private String stpitdt;
	private String stpittm;
	private String trnindt;
	private String trnintm;
	private String aprovyn;
	private String aprovdt;
	private String aprovtm;
	private String useract;
	private String drvernm;
	private String drverph;
	private String drverky;
	private String tpriloc;
	private String spriloc;
	private String plnsize;
	private String trcnldt;
	private String trcnltm;
	private String custcod;
	private String destkey;
	private int vhcmaxw;
	private String custnam;
	private int corweig;

	private String shpplky;
	private int finishDay;
	private int dayCount;
	private int totalweig;
	private int orderweig;
	private String apnamlc;
	private String vhnamlc;
	private String cunamlc;
	private int totalTrnindtCnt;
	private String targetMonth;
	private int trnmonttotal;
	private String trnmont;
	private String tpnamlc;
	private String renamlcs;
	private String regikeys;
	private int ctregiky;
	private String mnrenamlc;
	private String fncusky;
	private String fcnamlc;
	private String demodnms;

	//mvhcma
	private String vhcvryn;
	private String vhctype;

	//tplnit
	private String demodnm;
	private String dempocd;
	private String demdadr;
	private String demdtln;
	private String demdman;
	private String tpistat;

	//param
	private Integer limit;	//앱에서 쓰기 위한 limit
	private String pageId;	//pageId
	private String vhpdateFrom;
	private String vhpdateTo;

	//data
	private String trlocml;
	private String srlocml;
	private String type;
	private int totalWeight;
	private int emerweig;

	//app
	private List<String> schemaList;
	private String schema;
	private String commonSchema;
	private String status;
	private String schemaName;

	//app-운행일지
	private String day;
	private int itemweig;
	private String dtyear;
	private String dtmonth;

	//일단위
	private String trnindt01;
	private String trnindt02;
	private String trnindt03;
	private String trnindt04;
	private String trnindt05;
	private String trnindt06;
	private String trnindt07;
	private String trnindt08;
	private String trnindt09;
	private String trnindt10;
	private String trnindt11;
	private String trnindt12;
	private String trnindt13;
	private String trnindt14;
	private String trnindt15;
	private String trnindt16;
	private String trnindt17;
	private String trnindt18;
	private String trnindt19;
	private String trnindt20;
	private String trnindt21;
	private String trnindt22;
	private String trnindt23;
	private String trnindt24;
	private String trnindt25;
	private String trnindt26;
	private String trnindt27;
	private String trnindt28;
	private String trnindt29;
	private String trnindt30;
	private String trnindt31;
}
