package com.kbph.logistics.tm.type;

public enum TmApp {
	TPNSTAT("TPNSTAT"),
	APROVYN("APROVYN"),
	CANCEL("CANCEL"),
	ENABLED("Y"),
	DISABLED("N"),
	FINISH("FINISH"),
	ARRIVE("ARRIVE"),
	START("START"),
	REFUSAL("REFUSAL");
	
	private final String string;
	
	TmApp(String string) {
		this.string = string;
	}
	
	public String getString() {
		return string;
	}
}
