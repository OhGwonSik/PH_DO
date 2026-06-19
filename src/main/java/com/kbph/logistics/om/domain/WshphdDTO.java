package com.kbph.logistics.om.domain;

import java.util.List;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WshphdDTO extends CommonColumnDTO{

	private String shpdcky;
	private String warekey;
	private String doccate;
	private String doctype;
	private String vownkey;
	private String vehicky;
	private String shpdcst;
	private String alstrky;
	private String alstrit;
	private String parsncd;
	private String parsnnm;
	private String shpweig;
	private String wplndat;
	private String wplntim;
	private String obodate;
	private String obotime;
	private String shpsday;
	private String shpstim;
	private String spehdat;
	private String spehtim;
	private String outboky;
	private String shpplky;
	private String vhplnky;

	private String shpcfyn;

	private String wplstat;
	private String wplitst;
	private String tasksts;
	private String taskoky;

	private String whnamlc;
	private String docctnm;
	private String doctynm;
	private String vhcfnam;
	private String vonamlc;
	private String drvernm;
	private String rsncdnm;
	private String renamlc;

	// data
	private List<String> shpdcstList;
	private List<String> skumkeyList;
	private List<String> orderlnList;
	private String shpsdayFrom;
	private String shpsdayTo;
	private String plnsize;
	private String skumkey;
	private String shpstnm;
	private String tskstnm;
	private String transyn;

	private String shplimtFrom;
	private String shplimtTo;
	private String demodnm;
	private String orderln;
}
