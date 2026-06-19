package com.kbph.logistics.bm.domain;

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
public class BtrphdDTO extends CommonColumnDTO {
	//운송처리 DTO : doe085
	String warekey;    // 창고키
	String bphdkey;    // 운송정산문서번호
	String vownkey;    // 차주키
	String vehicky;    // 차량코드
	String regikey;    // 목적지키
	String vhcfnam;		//차량번호
	int trpdfsd;
	int trpdfwg;
	int dfstdch;
	int dfstdpa;
	int trpdcwg;    // 부적중량(청구)
	int trpdpwg;    // 부적중량(지급)
	int trpdfch;    // 부적운임 청구비용
	int trpdfpa;    // 부적운임 지급비용
	int toorgch;    // 총 기본청구금액
	int tosurch;    // 총 추가청구금액
	int totchco;    // 총 청구금액
	int toorgpa;    // 총 기본지급금액
	int tosurpa;    // 총 추가지급금액
	int totpaco;    // 총 지급금액
	int tothoch;	// 총 야간할증청구금액
	int tothopa;	// 총 야간할증지급금액
	String bphyymm;    // 운송정산년월
	String bphmemo;    // 비고
	String pcddate;    // 운송정산 마감일자
	String pcdtime;    // 운송정산 마감시간
	String pcduser;    // 운송정산 마감사용자
	String pcvrfyn;    // 운송정산 검증여부
	String pcvdate;    // 운송정산 검증일자
	String pcvtime;    // 운송정산 검증시간
	String pcvuser;    // 운송정산 검증사용자
	String pccfmyn;    // 운송정산 확정여부
	String pccdate;    // 운송정산 확정일자
	String pcctime;    // 운송정산 확정시간
	String pccuser;    // 운송정산 확정사용자
	int totitpa;
	
	String vowaffi;		//차량소속
	String comcdtx;

	int bphdcit;    // 운송정산아이템번호
	String doccate;    // 문서유형
	String doctype;    // 문서타입
	String btrpkey;    // 운송요율코드
	String btrcate;    // 정산유형
	String btrpitc;    // 품목코드
	String btrfitn;    // 품목명
	String btrpigb;    // 품목구분
	String btrfict;    // 품목계열
	int skuleng;    // 길이
	int skuwidt;    // 폭
	float skuweig;    // 중량
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
	String adcmemo;    // 비고
	String btrpcbt;    // 청구처
	String btrppat;    // 지급처
	String sbillto;    // 추가청구처
	String payadyn;		//선지급 여부
	String payadog;		//선지급 original
	String payadmd;		//선지급 modify

	String vhplnit;    // 운송계획아이템
	String custkey;    // 고객
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
	String poshpdt;    // 출하일자
	String poshptm;		//출하시간
	String poshpdy;		//출하요일
	String trncpdt;
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
	float grosswd;       // 주문폭
	String addcnam;    // 할증명칭
	String addsiky;	   // 할증기준코드
	String invoice;    // 송장번호
	
	String doctynm;
	String whnamlc;
	String drvernm;
	String renamlc;
	String grossWgName;
	int totalwg;
	String btrpdaffrom;
	String btrpdafto;
	String btrpdatfrom;
	String btrpdatto;
	String pcvdatefrom;
	String pcvdateto;
	int totalch;
	int totalpa;
	int totcitm;
	int totpitm;
	int totexch;
	int totexpa;
	String calweig;
	String tclosyn;
	String btrstat;
	String payaddt;		//선지급날짜
	String holidyn;		//야간휴일 할증여부
	String holrate;		//야간휴일 할증률
	String holchog;		//기존 휴일할증청구
	String holpaog;		//기존 휴일할증지급
	String spehdat;		//출고완료일자
	String spehtim;		//출고완료시간
}
