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
public class BtrpitDTO extends CommonColumnDTO {
	//TABLE : BTRPIT 운송정산 아이템
	String bphdkey;    // 운송정산문서번호
	String warekey;
	int bphdcit;       // 운송정산아이템번호
	String doccate;    // 문서유형
	String doctype;    // 문서타입
	String bpchkey;    // 운송청구요율코드
	String bppakey;    // 운송지급요율코드
	String btrcate;    // 정산유형
	String btrpitc;    // 품목코드
	String btrfitn;    // 품목명
	String btrpigb;    // 품목구분
	String btrfict;    // 품목계열
	int skuleng;    // 길이
	float skuwidt;    // 폭
	int skuweig;    // 중량
	String trnindt;    // 운송완료일자
	String vhplnky;    // 운송계획번호
	String regiorg;    // 원본목적지
	String regimod;    // 변경목적지
	String addcoky;    // 할증코드
	String adconam;    // 할증명칭
	float adcoper;    // 할증률
	String btrpitm;    // 정산항목
	int btrpchr;    // 청구단가
	int btrpcco;    // 청구금액
	int btrppay;    // 지급단가
	int btrppco;    // 지급금액
	int btrpecc;    // 할증 청구금액
	int btrpepc;    // 할증 지급금액
	int holichr;
	int holipay;
	int surchco;    // 추가청구금액
	int addchco;    // 추가요금(청구)
	int addpaco;    // 추가요금(지급)
	int trpdfch;	//부적운임 청구
	int trpdfpa;	//부적운임 지급
	String adcmemo;    // 비고
	String btrpcbt;    // 청구처
	String btrppat;    // 지급처
	String sbillto;    // 추가청구처
	int totitch;		//총 아이템 청구합산
	int totitpa;		// 총 아이템 지급합산
	String poshpdt;
	String poshptm;
	String poshpdy;

	int vhplnit;    // 운송계획아이템
	String custcod;    // 고객
	String destord;    // 고객도착순서
	String destkey;    // 상세착지
	String tpistat;    // 운송아이템 상태
	String ownerky;    // 판매계약사키
	String skumkey;    // 제품번호
	String skudesc;    // 제품명
	String obitqty;    // 출고예정수량
	String tritqty;    // 운송수량
	String itemcod;    // 품목코드
	int plnsize;    // 배차사이즈
	float plnweig;    // 배차중량
	float skuthic;    // 두께
	String purcnum;    // PO번호
	String orderln;    // 오더라인
	String suomkey;    // 기본 UOM
	String emskuyn;    // 긴급재여부
	String eoasnky;    // 입고예정번호
	int eoasnit;    // 입고예정아이템
	String cooutky;    // 출고예정번호
	int cooutit;    // 출고아이템번호
	String trncptm;    // 운송완료시간
	String poshdat;    // 출하일자
	String mixcuyn;    // 합짐여부
	String vhpdate;    // 배차계획일자
	String vhptime;    // 배차계획시간
	String tpnstat;    // 배차계획상태
	String trnmemo;    // 배차메모
	String stpitdt;    // 운송시작일자
	String stpittm;    // 운송시작시간
	String trnintm;    // 운송완료시간
	String btrpitn;    // 품목명
	String btrpict;    // 품목계열
	float grosswd;     // 주문폭
	String addcnam;    // 할증명칭
	String addsiky;	   // 할증기준코드
	String invoice;    // 송장번호
	String payadyn;
	String payadst;
	String payaddt;
	String pccdate;
	String pcctime;
	String pccuser;
	String btrfstc;

	//TPLNIT
	private String deorgnm; 
	private String demodnm; 
	private String demdman; 
	private String demdadr;
	private String dempocd; 
	private String demdtln;
	
	String speidat;
	String speitim;
	
	List<ComboDataDTO> custkeyList;
	List<String> skumkeyList;
	
	String custnam;
	String ownrnam;
	String btrcnam;
	int grosswg;
	String calweig;
	int grossln;
	String reorgnm;
	String remodnm;
	String sbillnm;    
	String regikey;
	String pcvrfyn;		//검증여부
	String pccfmyn;		//확정여부
	String vhcfnam;
	String btrpdaffrom;
	String btrpdafto;
	String confirmdatefrom;
	String confirmdateto;
	String docctnm;
	String doctynm;
	String whnamlc;
	String btcbtnm;
	String btrfcbt;		//집계내역안에서 검색조건 내 청구처 key
	String btrfpat;		//집계내역안에서 검색조건 내 지급처 key
	String calwgnm;		//계산중량 명
}
