package com.kbph.logistics.md.type;

public enum Wplstat {
	NEW("NEW", "신규"),
	ING("ING", "작업중"),
	CMP("CMP", "완료"),
	CANCEL("CANCEL", "취소");

	private final String code;
	private final String description;

	Wplstat(String code, String description){
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
