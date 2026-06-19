package com.kbph.logistics.bm.domain;

import java.util.List;

import com.kbph.logistics.common.domain.CommonColumnDTO;
import com.kbph.logistics.md.domain.McodemDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BtrcatDTO extends CommonColumnDTO {

	private String btrcate; 	// 정산유형
	private String btrcnam;		// 정산유형명
	private String btrfbtp; 	// 이송청구처타입
	private String btrpbtp; 	// 운송청구처타입
	private String orwidyn; 	// 주문폭기준여부
	private String tfonlyn; 	// 이송(입고) 실적전용여부
	private String pfonlyn; 	// 운송(출고) 실적전용여부
	private String dfstdyn;		// 부적운임 우선기준
	private String ksgbun1;		// 큰수레유형 구분1
	private String ksgbun2;		// 큰수레유형 구분2
	private String ksgbun3;		// 큰수레유형 구분3
	
	private List<McodemDTO> btrpbtpList;	//이송청구처 타입
	private List<McodemDTO> btrfbtpList;	//운송청구처 타입
	
}
