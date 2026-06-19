package com.kbph.logistics.bm.type;

public enum Vowaffi {
	//차량 소속
	CODE("VOWAFFI"),
	CONTRACT("CONTRACT"),
	DIRECT("DIRECT");
	
	private final String string;
	Vowaffi(String string){
		this.string = string;
	}

	public String getString() {
		return string;
	}
}
