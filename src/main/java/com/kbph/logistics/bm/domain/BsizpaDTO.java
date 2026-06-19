package com.kbph.logistics.bm.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BsizpaDTO extends CommonColumnDTO  {
	
	private String bsizpa; 	//사이즈별 지급단가
	private String btrnkey; //요율코드
	private String doccate; //문서유형
	private String doctype; //문서타입
	private int bsizpay; 	//사이즈 지급단가
	private String rowkey;
	private String useract;
	private String warekey;
	private String whnamlc;
	
	private String comcdtx;
	private float adcoper;
	private String addsiky;	//할증코드
	private String addcoky; //할증기준코드
}
