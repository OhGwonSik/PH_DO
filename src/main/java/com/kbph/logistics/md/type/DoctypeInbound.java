package com.kbph.logistics.md.type;

import java.util.Arrays;
import java.util.List;

public enum DoctypeInbound {
	POSCO_STRATEGY("410", "포스코전략입고", "STRATEGY", "MANUAL"),
	EXCEL_STRATEGY("411", "큰수레엑셀업로드(전략)", "STRATEGY", "MANUAL"),
	TOLL_STRATEGY("420", "임가공전략입고", "", "AUTO"),
	OUTSIDE_STRATEGY("430", "주문외전략입고", "", "AUTO"),
	POSCO("440", "포스코입고", "", "AUTO"),
	TOLL("450", "임가공입고", "", "AUTO"),
	OUTSIDE("460", "주문외입고", "", "AUTO"),
//	ETC("470", "기타입고", "", "AUTO"),
	CLAIM("480", "반출입고", "", "AUTO"),
	PHYSICAL_CREATE("490", "기타입고", "", "AUTO"),
	DIRECT_SHIPPING("810", "직송", "", "");

	private final String code;
	private final String description;
	private final String strategyClassification;
	private final String functionClassification;

	DoctypeInbound(String code, String description, String strategyClassification, String functionClassification){
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

	public static DoctypeInbound getDoctypeToCode(final String code){
        try {
        	return Arrays.stream(DoctypeInbound.values())
								        .filter(t -> t.getCode().equals(code))
								        .findFirst()
								        .orElseThrow(Exception::new);
		} catch (Exception e) {
			e.printStackTrace();
		}

        return null;
    }

	public static List<DoctypeInbound> getFunctionClassificationList(final String strategyClassification){
    	return Arrays.stream(DoctypeInbound.values())
							        .filter(t -> t.getStrategyClassification().equals(strategyClassification)).toList();
	}

	public static List<DoctypeInbound> getStrategyClassificationList(final String functionClassification){
    	return Arrays.stream(DoctypeInbound.values())
							        .filter(t -> t.getFunctionClassification().equals(functionClassification)).toList();
	}
}