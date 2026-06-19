package com.kbph.logistics.common.enums;

public enum SubDomain {
	DO_DR("do-dr"),
	DO_DY("do-dy"),
	LOCAL("localhost");

	private final String string;

	SubDomain(String string){
		this.string = string;
	}

	public String getString() {
		return string;
	}
}