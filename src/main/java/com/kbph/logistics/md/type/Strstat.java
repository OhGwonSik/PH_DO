package com.kbph.logistics.md.type;

public enum Strstat {
	NEW("NEW", "신규"),
//	PART_CONFIRMED("PCONF"),
//	ALL_CONFIRMED("ACONF"),
//	PART_PASS_INSPECTION("PTPASS"),
//	PASS_INSPECTION("PASS"),
//	PART_CANCEL("PCANCEL"),
	PROCEED("ING", " 진행중"),
	COMPLETE("CMP", "완료"),
	CANCEL("CANCEL", "취소");

	private final String code;
	private final String description;

	Strstat(String code, String description){
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
