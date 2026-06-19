package com.kbph.logistics.api.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
	public static final String API_USERACT = "SYS";
	public static final String COMMON_PREFIX = "/api/common";
	public static final String MASTER_PREFIX = "/api/master";
	public static final String INBOUND_PREFIX = "/api/inbound";
	public static final String OUTBOUND_PREFIX = "/api/outbound";
	public static final String INVENTORY_PREFIX = "/api/inventory";
	public static final String DRIVER_APP_NOTICE_TYPE = "control";
	public static final String DRIVER_APP_CANCEL_TYPE = "cancel";
	public static final String NO_MESSAGE_AVAILABLE = "No message available";
	public static final String CONVERSION_FLAG = "B";
	public static final String ERROR_FLAG = "E";
	public static final String CONVERSION_ERROR_MSG = "재고 미존재 에러 발생";
}
