package com.kbph.logistics.api.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InboundOperationDTO {
	String eoasnky; // 입고예정오더번호
	String rcvdcky; // 입고문서번호
	String taskoky; // 작업문서번호
	String skumkey; //제품번호
	String toareky; //To창고동
	String tolocky; //To베드
	int tolayer; //To베드단
	String tasksts; //작업상태
}
