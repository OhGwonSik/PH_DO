package com.kbph.logistics.common.enums;

public enum Inout {
	IN("IN"),
	OUT("OUT");

	private final String string;

	Inout(String string){
		this.string = string;
	}

	public String getString() {
		return string;
	}
}
