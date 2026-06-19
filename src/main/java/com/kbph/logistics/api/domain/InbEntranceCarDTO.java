package com.kbph.logistics.api.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class InbEntranceCarDTO {
	private String eoasnky; // 입고예정오더번호
//	private String vehicky; // 차량코드
	private String vhcfnam; // 차량번호
	private String entdttm; // 입차일시 -> 2024-11-09 17:33:39
	private String enttime; // 입차시간
}
