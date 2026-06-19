package com.kbph.logistics.bm.type;

public enum Crud {
	CREATE("C"),
	READ("R"),
	UPDATE("U"),
	DELETE("D");
	
	private final String string;
	
	Crud(String string){
		this.string = string;
	}
	
	public String getString() {
		return string;
	}
}
