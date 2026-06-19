package com.kbph.logistics.md.type;

public enum Adjmode {
	BEFORE("BEFORE", "이전 데이터"),
	AFTER("AFTER", "이후 데이터");

	private final String code;
	private final String description;

	Adjmode(String code, String description){
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
