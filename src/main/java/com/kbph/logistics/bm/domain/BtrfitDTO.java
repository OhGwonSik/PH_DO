package com.kbph.logistics.bm.domain;

import java.util.List;

import com.kbph.logistics.common.domain.ComboDataDTO;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BtrfitDTO extends CommonColumnDTO {
	//TABLE : BTRFIT 이송정산 아이템
	String warekey;   //창고키
	String bfhdkey;   //이송정산문서번호
	int bfhdcit;   //이송정산문서아이템번호
	String doccate;   //문서유형
	String doctype;   //문서타입
	String btrfkey;   //이송요율코드
	String btrcate;   //정산유형
	String btrfitc;   //품목코드
	String btrfitn;   //품목명
	String btrfigb;   //품목구분
	String btrfict;   //품목계열
	String ownerky;
	String ownrnam;
	String custcod;
	String custnam;
	int skuleng;   //길이
	float skuwidt;   //폭
	int skuweig;   //중량
	String rcvdate;   //입고일자
	String rcvdcky;   //입고문서번호
	String addcoky;   //할증코드
	String btrfky1;
	String btrfit1;   //이송정산항목1(이송비)
	int btrfch1;   //항목1 청구단가
	int btrfcc1;   //항목1 청구금액
	int btrfpa1;   //항목1 지급단가
	int btrfpc1;   //항목1 지급금액
	int btrfec1;   //항목1 할증청구금액
	int btrfep1;   //항목1 할증지급금액
	String btrfky2;
	String btrfit2;   //이송정산항목2(상하차비)
	int btrfch2;   //항목2 청구단가
	int btrfcc2;   //항목2 청구금액
	int btrfpa2;   //항목2 지급단가
	int btrfpc2;   //항목2 지급금액
	String btrfky3;
	String btrfit3;   //이송정산항목3
	int btrfch3;   //항목3 청구단가
	int btrfcc3;   //항목3 청구금액
	int btrfpa3;   //항목3 지급단가
	int btrfpc3;   //항목3 지급금액
	String btrfky4;
	String btrfit4;   //이송정산항목4
	int btrfch4;   //항목4 청구단가
	int btrfcc4;   //항목4 청구금액
	int btrfpa4;   //항목4 지급단가
	int btrfpc4;   //항목4 지급금액
	int holichr;
	int holipay;
	int surchco;   //추가청구금액
	int addchco;   //추가요금(청구)
	int addpaco;   //추가요금(지급)
	int totitch;
	int totitpa;
	String adcmemo;   //비고
	String btrfcbt;   //청구처
	String btrfpat;   //지급처
	String sbillto;   //추가청구처
	String calweig;
	String adconam;	 //할증명칭
	float adcoper; 	 //할증률
	String vehicky;	 //차량코드
	int trfdfch;
	int trfdfpa;
	String addsiky;
	
	String calwgnm; //계산중량 calweig = stnweig
	String cunamlc;
	String renamlc;
	String cbtname;	//청구처명
	String doctynm;	//문서타입
	String sbillnm;	//추가청구처명
	String btrcnam;	//정산유형 명
	String reorgnm;	//원본목적지 명
	String remodnm;	//변경목적지 명
	String payadst;	//선지급 상태
	
	//WRCVIT
	String rcvdcit;
	String poshpdt;
	String poshptm;
	String poshpdy;
	String invoice;
	String rcvitst;
	String skugrky;
	String skumkey;
	String skudesc;
	float skuthic;
	float grosswd;
	int grossln;
	int grosswg;
	String stlayer;
	String regiorg;
	String destorg;
	String regimod;
	String destmod;
	String ullocky;
	String toareky;
	String tolocky;
	String tolayer;
	String stlotnb;
	String purcnum;
	String orderln;
	String itemcod;
	String rcvdcdt;
	String rcvdctm;
	String asndqty;
	String rchsqty;
	String suomkey;
	String shplimt;
	String delvdsr;
	String btrfstc;
	String eoasnky;
	String eoasnit;
	String costrky;
	String costrit;
	String rcarscd;
	String rcarsnm;
	String fcvrfyn;
	String closeyn;
	String fccdate;
	String fcctime;
	String fccuser;
	String deorgnm; 
	String demodnm; 
	String demdman; 
	String demdadr;
	String dempocd; 
	String demdtln;
	
	String whnamlc;
	List<ComboDataDTO> custkeyList;
	List<String> skumkeyList;
	String fccfmyn;
	String vhcfnam;
	String fcddatefrom;
	String fcddateto;
	String confirmdatefrom;
	String confirmdateto;
	String docctnm;
}
