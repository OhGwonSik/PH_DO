package com.kbph.logistics.md.type;

public enum Asnitst {
	NEW("NEW", "신규"),
	CONFIRMED("CONF", "입고승인"),
	CANCEL("CANCEL", "취소"),
	PASS_INSPECTION("PASS", "검수완료"),
	PROCEED("ING", "작업진행"),
	COMPLETE("CMP", "완료");

	private final String code;
	private final String description;

	Asnitst(String code, String description){
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
