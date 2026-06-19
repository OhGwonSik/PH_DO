package com.kbph.logistics.bm.type;

public enum Btrfitm {
	CODE("BTRFITM"),
	LOADING("LOADING"),
	SELECTION("SELECTION"),
	STORAGE("STORAGE"),
	TRANSFER("TRANSFER"),
	STNWEIG("STNWEIG"),
	GROSS("GROSS"),
	GROSSWG("GROSSWG"),
	SKUWEIG("SKUWEIG"),
	DOCCATE("900"),
	DOCTYPE("910");
	
	private final String string;
	Btrfitm(String string){
		this.string = string;
	}

	public String getString() {
		return string;
	}
}
