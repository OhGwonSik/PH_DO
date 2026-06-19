package com.kbph.logistics.md.type;

public enum Tpistat {
	PLAN("PLAN", "배차계획"),
	START("START", "운송시작"),
	ARRIVE("ARRIVE", "운송도착"),
	INVCMP("INVCMP", "송장발행완료"),
	REFUSAL("REFUSAL", "운송거절"),
	FINISH("FINISH", "운송완료"),
	CANCEL("CANCEL", "배차취소");

	private final String code;
	private final String description;

	Tpistat(String code, String description){
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
