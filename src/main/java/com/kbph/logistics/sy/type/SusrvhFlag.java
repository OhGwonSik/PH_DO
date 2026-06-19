package com.kbph.logistics.sy.type;

public enum SusrvhFlag {
	VEHICLE_OWNER("Y") // VHOWNYN - 차주여부
	, NOT_VHICLE_OWNER("N")
	, MAPPING_USE("Y") // VHUSEYN - 매핑사용여부
	, MAPPING_UNUSE("N");

	private final String string;

	SusrvhFlag(String string) {
		this.string = string;
	}

	public String getString() {
		return string;
	}
}
