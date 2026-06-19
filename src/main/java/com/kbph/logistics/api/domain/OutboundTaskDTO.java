package com.kbph.logistics.api.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class OutboundTaskDTO {
	private String outboky; // 출고오더번호
	private String outboit; // 출고오더아이템번호
	private String taskoky; // 작업문서번호
	private String taskoit; // 작업문서아이템번호
	private String skumkey; // 제품번호
	private String toareky; // to 창고동 (사용안함)
	private String frareky; // from 창고
	private String frlocky; // from 베드
	private int frlayer; // from 단 (사용안함)
	private int tolayer; // to 단

	private String shpdcky; // 출고문서번호
	private int shpdcit; // 출고문서아이템번호

	private String ullocky; // 상차포인트
	private String tasksts; //작업상태
}
