package com.kbph.logistics.bm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class BtrfhdDTO extends CommonColumnDTO {
	//TABLE : BTRFHD 이송정산헤더
	private String warekey;    //창고키
	private String bfhdkey;    //이송정산문서번호
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
	private String bfhyymm;    //정산년월
	private String bfhmemo;	   //헤더 비고
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
	
	private String rcvdcdt;		//입고일자
	private String rcvdctm;		//입고시간
	private String rcvdate;
	private String inbvhnm;		//입고차번
	private String poshpdt;		//출하일자
	private String poshptm;		//출하시간
	private String poshpdy;		//출하요일
	private String payadyn;		//선지급 여부
	private int tothoch;		//총 야간할증금액(청구)
	private int tothopa;		//총 야간할증금액(지급)
	private int holchog;		//총 야간할증금액(지급)
	private int holpaog;		//총 야간할증금액(지급)
	//BTRFIT
	private String btrfit1;   //이송정산항목1(이송비)
	private int btrfcc1;   //항목1 청구금액
	private int btrfpc1;   //항목1 지급금액
	private int btrfec1;   //항목1 할증청구금액
	private int btrfep1;   //항목1 할증지급금액
	private String btrfit2;   //이송정산항목2(상하차비)
	private int btrfcc2;   //항목2 청구금액
	private int btrfpc2;   //항목2 지급금액
	private String btrfit3;   //이송정산항목3
	private int btrfcc3;   //항목3 청구금액
	private int btrfpc3;   //항목3 지급금액
	private String btrfit4;   //이송정산항목4
	private int btrfcc4;   //항목4 청구금액
	private int btrfpc4;   //항목4 지급금액
	private int holichr;   //휴일/야간할증청구
	private int holipay;   //휴일/야간할증지급
	private String rcvdcky;	//입고문서번호
	private String iclosyn;		//정산마감여부
	private int dfstdch;
	private int dfstdpa;

	private String grossWgName;
	private String calweig;		//계산중량
	private int totalwg;		//총 중량
	private String btrfitc;
	private String btrfigb;
	private String tabId;
	private String btrcate;
	private String fcddatefrom;		//마감일자 from :doe083
	private String fcddateto;		//마감일자 to   :doe083
	private String fcvdatefrom;		//검증일자 from :doe084
	private String fcvdateto;		//검증일자 to   :doe084
	private String bfhdcit;
	private String whnamlc;
	private String vonamlc;
	private String vhcfnam;
	private String drvernm;
	private int totalch;
	private int totalpa;
	private int totcit1; //청구금액(이송)
	private int totcit2; //청구금액(상하차)
	private int totcit3; //청구금액(선별)
	private int totpit1; //지급금액(이송)
	private int totpit2; //지급금액(상하차)
	private int totpit3; //지급금액(선별)
	private int totpit4; //지급금액(선별)
	private int totexch; //총 할증금액(청구)
	private int totexpa; //총 할증금액(지급)
	private String btrstat;
	private String holidyn;
	private String holrate;
}
