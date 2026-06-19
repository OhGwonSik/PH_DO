package com.kbph.logistics.md.type;

public enum DoctypeOutbound {
	GENERAL_SHIPMENT("710", "전략일반출고"),
	URGENT_RESEND("720", "긴급재출고"),
	SCHEDULED_SHIPMENT("730", "지정일반출고"),
	OTHER_SHIPMENT("740", "기타출고"),
	RETURN_SHIPMENT("750", "반출출고");

	private final String code;
	private final String description;

	DoctypeOutbound(String code, String description){
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
