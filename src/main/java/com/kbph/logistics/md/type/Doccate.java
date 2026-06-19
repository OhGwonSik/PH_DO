package com.kbph.logistics.md.type;

import java.util.Arrays;

public enum Doccate {
	INBOUND_ORDER("100", "입고오더"),
	OUTBOUND_ORDER("200", "출고오더"),
	LOGISTICS_ORDER("300", "물류오더"),
	INBOUND("400", "입고"),
	STOCK_MOVING("500", "이동"),
	STOCK("600", "재고"),
	OUTBOUND("700", "출고"),
	TRANSIT("800", "운송"),
	SETTLEMENT("900", "정산");

	private final String code;
	private final String description;

	Doccate(String code, String description){
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return this.code;
	}

	public String getDescription() {
		return this.description;
	}

	public static Doccate getEnumToCode(String code){

        try {
        	return Arrays.stream(Doccate.values())
								        .filter(t -> t.getCode().equals(code))
								        .findFirst()
								        .orElseThrow(Exception::new);
		} catch (Exception e) {
			e.printStackTrace();
		}

        return null;
    }
}
