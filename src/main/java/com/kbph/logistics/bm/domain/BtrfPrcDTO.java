package com.kbph.logistics.bm.domain;

import java.util.List;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BtrfPrcDTO extends CommonColumnDTO {
	//이송처리 DTO : doe082
	private String warekey;   //창고키
	private String bfhdkey;   //이송정산문서번호
	private int bfhdcit;   //이송정산문서아이템번호
	private String doccate;   //문서유형
	private String doctype;   //문서타입
	private String btrfkey;   //이송요율코드
	private String btrfky1;
	private String btrfky2;
	private String btrfky3;
	private String btrfky4;
	private String btrcate;   //정산유형
	private String btrfitc;   //품목코드
	private String btrfitn;   //품목명
	private String btrfigb;   //품목구분
	private String btrfict;   //품목계열
	private int skuleng;   //길이
	private float skuwidt;   //폭
	private int skuweig;   //중량
	private String rcvdate;   //입고일자
	private String rcvdcky;   //입고문서번호
	private String addcoky;    // 할증코드
	private String adconam;    // 할증명칭
	private float adcoper;    // 할증률
	private String addsiky;	   // 할증기준코드
	private String btrfit1;   //이송정산항목1(이송비)
	private int btrfch1;   //항목1 청구단가
	private int btrfcc1;   //항목1 청구금액
	private int btrfpa1;   //항목1 지급단가
	private int btrfpc1;   //항목1 지급금액
	private int btrfec1;   //항목1 할증청구금액
	private int btrfep1;   //항목1 할증지급금액
	private String btrfit2;   //이송정산항목2(상하차비)
	private int btrfch2;   //항목2 청구단가
	private int btrfcc2;   //항목2 청구금액
	private int btrfpa2;   //항목2 지급단가
	private int btrfpc2;   //항목2 지급금액
	private String btrfit3;   //이송정산항목3
	private int btrfch3;   //항목3 청구단가
	private int btrfcc3;   //항목3 청구금액
	private int btrfpa3;   //항목3 지급단가
	private int btrfpc3;   //항목3 지급금액
	private String btrfit4;   //이송정산항목4
	private int btrfch4;   //항목4 청구단가
	private int btrfcc4;   //항목4 청구금액
	private int btrfpa4;   //항목4 지급단가
	private int btrfpc4;   //항목4 지급금액
	private int surchco;   //추가청구금액
	private int addchco;   //추가요금(청구)
	private int addpaco;   //추가요금(지급)
	private String adcmemo;   //비고
	private String btrfcbt;   //청구처
	private String btrfpat;   //지급처
	private String sbillto;   //추가청구처
	private int holichr;	 //야간할증 청구금액
	private int holipay;	 //야간할증 지급금액
	private String payadyn;
	
	private String cunamlc;	 //master
	private String renamlc;
	private String reorgnm; 	//원본 목적지명
	private String remodnm;		//변경 목적지명
	private String btrcnam;		//정산유형명
	private String btrfbtp;		//이송청구처명
	private String rcvdcdtfrom;	//입고일자 from
	private String rcvdcdtto;	//입고일자 to
	private String btrfdaffrom;	//정산일자 from
	private String btrfdafto;	//정산일자 to
	private String poshpdtfrom;
	private String poshpdtto;
	private String vonamlc;  	//차주명 = 지급처
	private String vhcfnam;
	private String sbillnm;   //추가청구처명
	private String docctnm;		//문서유형명
	private String doctynm;		//문서타입명
	private int totalwg;
	private List<String> custkeys;	//멀티 콤보박스 값을 위한 List
	private String payadst;		//선지급완료여부 상태
	private String calwgnm;		//계산중량 명
	
	//WRCVIT
	private String deorgnm; 
	private String demodnm; 
	private String demdman; 
	private String demdadr;
	private String dempocd; 
	private String demdtln;
	
	//BTRFHD
	private String vownkey;    //차주키
	private String vehicky;    //차량코드
	private int trfdfsd;    //이송 부적기준중량
	private int trfdfwg;    //이송 부적중량
	private int trfdfch;    //부적운임 청구비용
	private int trfdfpa;    //부적운임 지급비용
	private int toorgch;    //총 기본청구금액
	private int tosurch;    //총 추가청구금액
	private int totchco;    //총 청구금액
	private int toorgpa;	//총 기본지급금액
	private int tosurpa;	//총 추가지급금액
	private int totpaco;	//총 지급금액
	private int totitch;
	private int totitpa;
	private String bfhyymm;    //정산년월
	private String fcddate;    //마감일자
	private String fcdtime;    //마감시간
	private String fcduser;    //마감사용자
	private String fcvrfyn;    //검증여부
	private String fcvdate;    //검증일자
	private String fcvtime;    //검증시간
	private String fcvuser;    //검증사용자
	private String fccfmyn;    //확정여부
	private String fccdate;    //확정일자
	private String fcctime;    //확정시간
	private String fccuser;    //확정사용자
	private String payaddt;
	//WRCVIT
	private int rcvdcit;
	private String poshpdt;
	private String poshptm;
	private String poshpdy;
	private String invoice;
	private String ownerky;
	private String ownrnam;
	private String custnam;
	private String rcvitst;
	private String custcod;
	private String skugrky;
	private String skumkey;
	private String skudesc;
	private float skuthic;
	private float grosswd;
	private int grossln;
	private int grosswg;
	private int stlayer;
	private String regiorg;
	private String destorg;
	private String regimod;
	private String destmod;
	private String ullocky;
	private String toareky;
	private String tolocky;
	private String tolayer;
	private String stlotnb;
	private String purcnum;
	private String orderln;
	private String itemcod;
	private String rcvdcdt;
	private String rcvdctm;
	private int asndqty;
	private int rchsqty;
	private String suomkey;
	private String shplimt;
	private String delvdsr;
	private String btrfstc;
	private String eoasnky;
	private int eoasnit;
	private String costrky;
	private int costrit;
	private String rcarscd;
	private String rcarsnm;
	private String iclosyn;		//이송마감여부
	private String transfer;   //이송비
    private String loading;      //상하차비
    private String selection;    //선별비
    private String storage;      //보관비
    private String stnweig;		//기준 중량
    private String calweig;		//계산 중량
    private String grossWgName;
    private String skuWeigName;
    
    //실적집계 
    private int dayweig;
    private int daytoch;
    private int daytopa;
    private int dayexco;
    private int dayexpa;
    private int dayhoch;
    private int dayhopa;
    private String trncpdtfrom;
	private String trncpdtto;
}
