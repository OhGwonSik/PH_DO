package com.kbph.logistics.bm.domain;

import java.util.List;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BtrpPrcDTO extends CommonColumnDTO {
	//운송처리 DTO 
	//BTRPIT
	private String warekey;		//창고키
	private String bphdkey;		//운송정산문서번호
	private int bphdcit;		//운송정산아이템번호
	private String doccate;		//문서유형
	private String doctype;		//문서타입	
	private String btrcate;		//정산유형코드
	private String btrpitc;		//품목코드
	private String btrpitn;		//품목명
	private String btrpigb;		//품목구분
	private String btrpict;		//품목계열
	private String ownerky;
	private String custcod;
	private String ownrnam;
	private String custnam;		//btrpit 고객사명
	private float skuthic;
	private int skuleng;
	private float skuwidt;
	private int skuweig;
	private int grossln;
	private float grosswd;
	private int grosswg;
	private String calweig;
	private String trnindt;		//운송완료일자
	private String vhplnky;		//운송계획번호
	private String vownkey;		//차주키
	private String vehicky;		//차량코드
	private String regikey;		//목적지키
	private String regiorg;		//원본목적지키
	private String regimod;		//변경목적지키
	private String addcoky;		//할증코드
	private String btrpitm;		//운송정산항목
	private int btrpchr;		//청구단가
	private int btrpcco;		//청구금액
	private int btrppay;		//지급단가
	private int btrppco;		//지급금액
	private int btrpecc;		//할증청구금액
	private int btrpepc;		//할증지급금액
	private int surchco;		//추가청구금액
	private int addchco;		//추가요금(청구)
	private int addpaco;		//추가요금(지급)
	private String adcmemo;		//비고
	private String btrpcbt;		//운송 청구처
	private String btrppat;		//운송 지급처
	private String sbillto;		//추가청구처
	private int totchco;
	private int totpaco;
	private int totitch;		//총청구(아이템)
	private int totitpa;		//총지급(아이템)
	private String bpchkey;		//운송청구요율코드
	private String bppakey;		//운송지급요율코드
	private int holichr;		//야간 ,휴일 할증 청구금액
	private int holipay;		//야간 ,휴일 할증 지급금액
	private String bildate;		//정산기준일자
	
	private String poshpdt;
	private String poshptm;
	private String poshpdy;
	//CUSTOM
	private String bildatefrom;	//운송도착일자 from
	private String bildateto;	//운송도착일자 to
	private String docctnm;		//문서유형명
	private String doctynm;		//문서타입명
	private String btrcnam;		//정산유형명
	private String vonamlc;		//차주명
	private String vowaffi;		//차량 소속(직접/위탁)
	private String vhcfnam; 	//차량번호
	private String addsiky;		//할증코드(사이즈별)
	private String adconam;    // 할증명칭
	private float adcoper;		//할증률
	private String cunamlc;		//master 고객사명
	private String reorgnm;		//원본목적지 명
	private String remodnm;		//변경목적지 명
	private String stnweig;		//기준중량
	private String grossWgName;	//grossWg
	private String skuWeigName;
	private String btrpbtp;		//청구처명
	private String sbillnm;		//추가청구처명
	private List<String> custkeys;		//멀티콤보박스 값 받기 위한 List
	private String payadst;		//선지급상태
	private String payaddt;		//선지급날짜
	private String calwgnm;		//계산중량 명
	private String speidat;
	private String speitim;
	
	
	//TPLNHD
	private String loadkey;		//상차지
	private String vhplnyn;		
	private String mixcuyn;
	private String vhpdate;
	private String vhptime;
	private String aprovyn;
	private String aprovdt;
	private String aprovtm;
	private String useract;
	private String tpnstat;
	private String trnmemo;
	private String stpitdt;
	private String stpittm;
	private String trnintm;
	private String tclosyn;
	private String tclodat;
	private String tclotim;
	private String tclousr;
	private String tclohky;
	private String tclohit;
	private String payadyn;	//선지급 여부
	
	
	//TPLNIT
	private String vhplnit;
	private String custkey;
	private String destord;
	private String destkey;
	private String tpistat;
	private String skumkey;
	private String skudesc;
	private String obitqty;
	private String tritqty;
	private String itemcod;
	private String plnsize;
	private String plnweig;
	private String purcnum;
	private String orderln;
	private String suomkey;
	private String emskuyn;
	private String eoasnky;
	private String eoasnit;
	private String shpplky;
	private String shpplit;
	private String trncpdt;
	private String trncptm;
	private String btrfstc;
	private String deorgnm; 
	private String demodnm; 
	private String demdman; 
	private String demdadr;
	private String dempocd; 
	private String demdtln;
	
	private String btrpdaffrom;
	private String btrpdafto;
	private String billitm;	//정산항목명
}
