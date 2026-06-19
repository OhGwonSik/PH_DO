package com.kbph.logistics.md.type;

public enum Aprovyn {
	STAY("STAY", "대기"),
	APPROVAL("APPROVAL", "승인"),
	CANCEL("CANCEL", "취소");

	private final String code;
	private final String description;

	Aprovyn(String code, String description){
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDesctiption() {
		return description;
	}
}
