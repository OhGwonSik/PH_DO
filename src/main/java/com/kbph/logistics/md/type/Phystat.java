package com.kbph.logistics.md.type;

public enum Phystat {
	READY("READY", "실사준비"),
	WORKING("WORKING", "실사작업"),
	CMP("COMPLETED", "실사완료");

	private final String code;
	private final String description;
	Phystat(String code, String description){
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
}
