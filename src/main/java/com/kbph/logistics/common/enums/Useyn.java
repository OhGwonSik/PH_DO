package com.kbph.logistics.common.enums;

public enum Useyn {
	USE("Y"),
	UNUSE("N");

	private final String string;

	Useyn(String string){
		this.string = string;
	}

	public String getString() {
		return string;
	}
}
