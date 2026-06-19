package com.kbph.logistics.md.type;

public enum DoccateInventory {
	MOVEMENT("500", "이동"),
	STOCK("600", "재고");

	private final String code;
	private final String description;

	DoccateInventory(String code, String description){
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
}
