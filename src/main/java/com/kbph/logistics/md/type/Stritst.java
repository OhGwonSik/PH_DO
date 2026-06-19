package com.kbph.logistics.md.type;

public enum Stritst {
	NEW("NEW", "신규"),
//	CONFIRMED("CONF"),
//	PASS_INSPECTION("PASS");
	PROCEED("ING", " 진행중"),
	COMPLETE("CMP", "완료"),
	CANCEL("CANCEL", "취소");

	private final String code;
	private final String description;

	Stritst(String code, String description){
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
