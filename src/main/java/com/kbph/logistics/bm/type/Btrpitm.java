package com.kbph.logistics.bm.type;

public enum Btrpitm {
	CODE("BTRPITM","TRANSFER"),
	DOCCATE("900","920");
	
	private final String string;
	private String code;
	
	Btrpitm(String code, String string){
		this.string = string;
		this.code = code;
	}

	public String getString() {
		return string;
	}

	public String getCode() {
		return code;
	}
}
