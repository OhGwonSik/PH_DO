package com.kbph.logistics.md.type;

public enum Asnstat {
	NEW("NEW", "신규"),
	PART_CANCEL("PCANCEL", "부분취소"),
	CANCEL("CANCEL", "취소"),
	PART_CONFIRMED("PCONF", "입고부분승인"),
	ALL_CONFIRMED("ACONF", "입고승인"),
	PART_PASS_INSPECTION("PTPASS", "부분검수완료"),
	PASS_INSPECTION("PASS", "검수완료"),
	PROCEED("ING", "입고진행"),
	PART_COMPLETE("PCMP", "부분완료"),
	COMPLETE("CMP", "완료");

	private final String code;
	private final String description;

	Asnstat(String code, String description){
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return this.code;
	}

	public String getDescription() {
		return this.description;
	}
}
