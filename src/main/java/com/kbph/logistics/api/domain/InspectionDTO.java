package com.kbph.logistics.api.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class InspectionDTO {
	private String eoasnky; // 입고예정오더번호
	private String skumkey; // 제품번호
	private int stlayer; // 단위치
	private String asnitst; //상태
}