package com.kbph.logistics.bm.type;

public enum Btrtype {
	// 이송, 운송 청구처 타입
	BTRF_CODE("BTRFBTP"),	//이송
	BTRP_CODE("BTRPBTP"),	//운송
	CUSTOMER("CUSTOMER"),
	OWNER("OWNER");
	
	private final String string;
	Btrtype(String string){
		this.string = string;
	}

	public String getString() {
		return string;
	}
}
