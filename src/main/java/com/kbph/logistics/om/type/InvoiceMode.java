package com.kbph.logistics.om.type;

public enum InvoiceMode {
	DEMAND("수요가증빙용"),
	RECEIPT("인   수   증"),
	CUSTOMER("고객사보관용");

	private final String string;

	InvoiceMode(String string){
		this.string = string;
	}

	public String getString() {
		return string;
	}
}