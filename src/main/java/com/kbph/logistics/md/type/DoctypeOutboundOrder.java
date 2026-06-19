package com.kbph.logistics.md.type;

import java.util.Arrays;

public enum DoctypeOutboundOrder {
	GENERAL_SHIPMENT("210", "전략일반출고"),
	URGENT_RESEND("220", "긴급재출고"),
	SCHEDULED_SHIPMENT("230", "지정일반출고"),
	OTHER_SHIPMENT("240", "기타출고");

	private final String code;
	private final String description;

	DoctypeOutboundOrder(String code, String description){
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public static DoctypeOutboundOrder getDoctypeToCode(final String code){
        try {
        	return Arrays.stream(DoctypeOutboundOrder.values())
								        .filter(t -> t.getCode().equals(code))
								        .findFirst()
								        .orElseThrow(Exception::new);
		} catch (Exception e) {
			e.printStackTrace();
		}

        return null;
    }
}
