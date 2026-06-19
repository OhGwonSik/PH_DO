package com.kbph.logistics.api.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class OubEntranceCarDTO {
	private String outboky; // 출고오더번호
	private String vehicky; // 차량코드
	private String vhcfnam; // 차량번호
	private String entdttm; // 입차일시
}