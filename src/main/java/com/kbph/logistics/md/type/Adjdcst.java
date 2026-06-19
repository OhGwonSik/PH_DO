package com.kbph.logistics.md.type;

public enum Adjdcst {
	NEW("NEW", "신규"),
	CMP("CMP", "완료");

	private final String code;
	private final String description;

	Adjdcst(String code, String description){
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
