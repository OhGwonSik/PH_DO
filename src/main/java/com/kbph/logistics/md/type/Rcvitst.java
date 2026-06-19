package com.kbph.logistics.md.type;

public enum Rcvitst {
	NEW("NEW", "신규"),
	CANCEL("CANCEL", "취소"),
	PROCEED("ING", "입고진행"),
	COMPLETE("RVCOMP", "완료"),
	RETURN("RETURN", "반납");

	private final String code;
	private final String description;

	Rcvitst(String code, String description){
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
