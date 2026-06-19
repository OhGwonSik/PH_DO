package com.kbph.logistics.bm.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaddcoDTO extends CommonColumnDTO {
	private String useract;
	
	//BADDCO : 할증기준
	private String addcoky; 	// VARCHAR(10) 할증기준코드
	private String addsiky; 	// VARCHAR(60) 할증코드
	private int minleng;	//INT(11) 최소길이
	private int maxleng;	//INT(11) 최대길이
	private float minwidt;	//FLOAT 최소 폭
	private float maxwidt;	//FLOAT 최대 폭
	private float adcoper;		//FLOAT 할증률
	private String syscdyn;
	
	private String comcdtx;
}
