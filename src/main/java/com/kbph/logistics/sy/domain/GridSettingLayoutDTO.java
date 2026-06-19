package com.kbph.logistics.sy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GridSettingLayoutDTO extends CommonColumnDTO{
	private String compkey;		//Company Key
	private String useract;		//유저 ID
	private String progrid;		//프로그램 ID
	private String pgridid;		//그리드 ID

	private Boolean nubrcel;	//numberCell
	private String hovermd;		//hoverMode
	private int frezcol;	//freezeCols
	private int frezrow;	//freezeRows
	private Boolean colbodr;	//columnBorders
	private Boolean rowbodr;	//rowBorders
	private Boolean strprow;	//stripeRows
	private int gheight;	//Grid height
}
