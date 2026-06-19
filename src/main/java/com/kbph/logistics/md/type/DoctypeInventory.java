package com.kbph.logistics.md.type;

public enum DoctypeInventory {
	SELECTION("510", "선별"),
	SELECTION_CANCEL("520", "선별취소"),
	STATUS_CHANGE("610", "상태변경"),
	PHYSICAL_STOCK("620", "실사"),
	BLOCK("630", "블락"),
	UNBLOCK("640", "블락해제"),
	DAY_STOCK_BACKUP("650", "일재고백업(정산수불)"),
	CONVERSION("660", "전환");

	private final String code;
	private final String description;

	DoctypeInventory(String code, String description){
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
