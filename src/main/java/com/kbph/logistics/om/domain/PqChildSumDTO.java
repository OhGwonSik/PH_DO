package com.kbph.logistics.om.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PqChildSumDTO {
	//아이템 합계(출고계획 사용)
	private Integer skuweig;
	private Integer grosswg;
	private String parentid;
	private Integer planqty;
}
