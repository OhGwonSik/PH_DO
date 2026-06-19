package com.kbph.logistics.md.type;

public enum Rcvdcst {
	NEW("NEW", "신규(작업 전)"),
	PART_CANCEL("PCANCEL", "입고부분취소"),
	ALL_CANCEL("ACANCEL", "입고취소"),
	PROCEED("ING", "입고진행"),
	COMPLETE("RVCMP", "입고완료");

	private final String code;
	private final String description;

	Rcvdcst(String code, String description){
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return this.description;
	}
}
