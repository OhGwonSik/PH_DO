package com.kbph.logistics.api.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class SelectionOperationComplDTO {
	private String taskoky; // 선별작업번호
	private String skumkey; // 제품번호
	private String toareky; // TO 창고동
	private String tolocky; // TO 베드(베드)
	private int tolayer; // 적치 단

	private String tasksts; //작업상태
}