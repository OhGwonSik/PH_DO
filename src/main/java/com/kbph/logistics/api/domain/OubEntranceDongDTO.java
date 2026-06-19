package com.kbph.logistics.api.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class OubEntranceDongDTO {
	private String outboky; // 출고오더번호
	private String vhcfnam; // 차량번호
	private String warekey; // 창고
	private String areakey; // 창고동
	private String tolocky; // 하차포인트
	private int endflag;
}
