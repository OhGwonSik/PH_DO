package com.kbph.logistics.api.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class InboundTaskDTO {
	private String eoasnky; // 입고예정오더번호
	private String taskoky; // 작업문서번호
	private String skumkey; // 제품번호
	private String toareky; // to창고동
	private String tolocky; // to베드
	private int tolayer; // 단위치

	private String stockky; // 재고키
	private String lotnmky; // Lot키
	private String tasksts; //작업상태

	private String schema;
}
