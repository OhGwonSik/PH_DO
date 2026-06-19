package com.kbph.logistics.api.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class OutboundOperationDTO {
	private String outboky; // 출고오더번호
	private String taskoky; // 작업문서번호
	private String skumkey; // 제품번호
	private Integer frlayer; // from 단 (사용안함)
	private Integer tolayer; // to 단

	private String tasksts; // 작업 상태
}
