package com.kbph.logistics.bm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author : s.h.kim
 * @version : 1.0.0
 * @since : 2024-09-19
 * @note : BmReportDTO
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-09-19					s.h.kim        						create DTO class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BmReportDTO extends CommonColumnDTO {
	// P.K
	private String warekey; // 창고키
	private String vehicky; // 차량코드

	// 지급내역서 columns
	private String vhcfnam; // 차량번호
	private String vonamlc; // 차주명칭
	private int extrapa; // 할증금액
	private int payment; // 지급금액
	private int tosurpa; // 추가금액
	private int totpaco; // 합계금액
	private int holipay; // 휴일할증금액
	private String trsmemo; // 비고 (부과세)

	// 청구내역서 columns
	private String wamannm; // 담당자 성명
	private String waraddr; // 창고 주소
	private String tccdate; // 정산확정일자
	private String btrfcbt; // 청구처
	private String sbillto;	//추가청구처
	private String btrfpat; //지급처
	private int extrach; // 할증금액
	private int charge; // 지급금액
	private int tosurch; // 추가금액
	private int totchco; // 합계금액
	private int holichr; // 휴일할증금액
	private String bphmemo; // 비고 (부과세)
	private String payadyn; // 선지급여부
	private String btrchr1;		//청구단가 1(이송비)
	private String btrchr2; 	//청구단가 2(상하차비)
	private String btrchr3; 	//청구단가 3(선별비)
	private String btrchr4; 	//청구단가 4(보관비)
	private String btrpay1;		//지급단가 1(이송비)
	private String btrpay2; 	//지급단가 2(상하차비)
	private String btrpay3; 	//지급단가 3(선별비)
	private String btrpay4; 	//지급단가 4(보관비)

	// common
	private String whnamlc; // 창고명칭
	private String trsdate; // 날짜
	private String bfhdkey; // 이송문서번호
	private String bphdkey; // 운송문서번호
	private String btrcnam; // 정산유형
	private String renamlc; // 목적지명칭
	private int trsdfpa; // 부적운임
	private int totalwg; // 중량
	private int rownum; // 행번호
	private String type; // 구분 type (jasper 파일 이름)
	private String printdate; // 인쇄일
	private int regicnt;		//목적지 cnt (표기 되는 목적지를 제외한 이외 목적지 카운트)

	// data
	private String confirmdatefrom; // 정산일자 from
	private String confirmdateto; // 정산일자 to
	private int weigsum; // 총 중량
	private int costsum; // 합계금액

	// 프린트 로그
	private String printsq; // 인쇄이력번호
	private String progrid; // 프로그램 ID
	private String doccate; // 문서유형
	private String doctype; // 문서타입
	private int reissue; // 재발행
}
