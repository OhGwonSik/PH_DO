package com.kbph.logistics.om.domain;

import java.util.List;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OoubhdDTO extends CommonColumnDTO {

	private String outboky; // 출고오더번호
	private Integer outboit; // 출고오더아이템번호
	private String warekey; // 창고키
	private String doccate; // 문서유형
	private String doctype; // 문서타입
	private String vownkey;
	private String vehicky;
	private String alstrky; // 할당전략키
	private Integer alstrit; // 할당전략번호
	private String rsncode; // 사유코드
	private String parsnnm; // 작업사유내용
	private Integer shpweig; // 출고톤수
	private String obodate; // 출고오더일자
	private String obotime; // 출고오더시간
	private String obostat; // 출고오더상태
	private String entdate; // 입차날짜
	private String enttime; // 입차시간
	private String extdate; // 출차날짜
	private String exttime; // 출차시간
	private String entdoyn; // 입동지시여부

	private String wplndat;
	private String wplntim;

	private String shpplky;
	private String regikey;
	private String oboitst;
	private String vhplnky; // 배차계획번호
	private String tpnstat; // 배차상태
	private String plnsize; // 배차사이즈
	private String whnamlc;
	private String docctnm;
	private String doctynm;
	private String tstsnam;
	private String rsncdnm;
	private String vonamlc;
	private String vhcfnam;
	private String tpriloc;
	private String trlocml;
	private String spriloc;
	private String srlocml;
	private Integer vhcmaxw;

	private String shpdcky;
	private String shpdcst;
	private String parsncd;

	private String wplstat;
	private String wpldate;
	private String wpltime;

	private String vhpdate;
	private String vhptime;
	private String tpistat;
	private String aprovyn;
	private String ooucfyn;
	private String grpkynm;
	private String skumkey;
	private String ownerky;
	private String destkey;
	private String denamlc;
	private String desaddr;
	private String remweig;
	private String ownrnam;
	private String renamlc;

	private List<String> shpplkyList;
	private List<String> skumkeyList;
	private List<String> orderlnList;
	private List<String> obostatList;

	private String vhpdatefrom;
	private String vhpdateto;

	private String oubdateFrom;
	private String oubdateTo;

	private String oubrqdtFrom;
	private String oubrqdtTo;

	private String shplimtFrom;
	private String shplimtTo;
	private String demodnm;
	private String orderln;
	private Integer alcweig;
	private String btrcate;
	private String addcoky;
	private String equipky;

	private String pageId;
}
