package com.kbph.logistics.tm.type;

public enum Dispatch {
	TPNSTAT("TPNSTAT"),
	DOCCATE("800"),
	DOCTYPE("820"),
	APROVYN("APROVYN"),
	TPISTAT("TPISTAT"),
	STAY("STAY"),
	APPROVAL("APPROVAL"),
	FINISH("FINISH"),
	START("START"),
	ARRIVE("ARRIVE"),
	CANCEL("CANCEL"),
	WMDOCCATE("200"),
	WAREHOUSE("warehouse"),
	OWNER("owner"),
	ENABLED("Y"),
	DISABLED("N"),
	REFUSAL("REFUSAL"),
	TMP("TMP");
	
	private final String string;
	
	Dispatch(String string) {
		this.string = string;
	}
	
	public String getString() {
		return string;
	}
}
