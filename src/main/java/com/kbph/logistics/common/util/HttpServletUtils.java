package com.kbph.logistics.common.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpServletUtils {
	public static boolean isMobile(HttpServletRequest request) {
		String checkDevice = request.getHeader("USER-AGENT");

		return checkDevice != null && (checkDevice.indexOf("Android") != -1 || checkDevice.indexOf("iPhone") != -1 || checkDevice.indexOf("iPad") != -1);
	}
}
