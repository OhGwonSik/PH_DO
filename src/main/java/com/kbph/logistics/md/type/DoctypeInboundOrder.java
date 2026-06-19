package com.kbph.logistics.md.type;

import java.util.Arrays;
import java.util.List;

public enum DoctypeInboundOrder {
	POSCO_STRATEGY("110", "포스코전략입고오더", "STRATEGY", "AUTO"),
	TOLL_STRATEGY("120", "임가공전략입고오더", "STRATEGY", ""),
	OUTSIDE_STRATEGY("130", "주문외전략입고오더", "STRATEGY", ""),
	POSCO("140", "포스코입고오더", "", "AUTO"),
	TOLL("150", "임가공입고오더", "", ""),
	OUTSIDE("160", "주문외입고오더", "", ""),
	ETC("170", "기타입고오더", "", "AUTO"),
	CLAIM("180", "클레임입고오더", "", "AUTO"),
	PHYSICAL_CREATE("190", "기타입고", "", "AUTO"),
	DIRECT_SHIPPING("810", "직송", "", "");

	private final String code;
	private final String description;
	private final String strategyClassification;
	private final String functionClassification;

	DoctypeInboundOrder(String code, String description, String strategyClassification, String functionClassification){
		this.code = code;
		this.description = description;
		this.strategyClassification = strategyClassification;
		this.functionClassification = functionClassification;
	}

	public String getCode() {
		return this.code;
	}

	public String getDescription() {
		return this.description;
	}

	public String getStrategyClassification() {
		return this.strategyClassification;
	}

	public String getFunctionClassification() {
		return this.functionClassification;
	}

	public static DoctypeInboundOrder getDoctypeToCode(final String code){
        try {
        	return Arrays.stream(DoctypeInboundOrder.values())
								        .filter(t -> t.getCode().equals(code))
								        .findFirst()
								        .orElseThrow(Exception::new);
		} catch (Exception e) {
			e.printStackTrace();
		}

        return null;
    }

	public static List<DoctypeInboundOrder> getFunctionClassificationList(final String strategyClassification){
    	return Arrays.stream(DoctypeInboundOrder.values())
							        .filter(t -> t.getStrategyClassification().equals(strategyClassification)).toList();
	}

	public static List<DoctypeInboundOrder> getStrategyClassificationList(final String functionClassification){
    	return Arrays.stream(DoctypeInboundOrder.values())
							        .filter(t -> t.getFunctionClassification().equals(functionClassification)).toList();
	}
}
