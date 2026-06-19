package com.kbph.logistics.sy.type;

public enum MvowmaFlag {
	SIGNED_VEHICLE_OWNER("Y") // SIGUPYN - 차주등록여부
	, UNSIGNED_VEHICLE_OWNER("N");

	private final String string;

	MvowmaFlag(String string) {
		this.string = string;
	}

	public String getString() {
		return string;
	}
}
