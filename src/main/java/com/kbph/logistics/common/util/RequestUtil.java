package com.kbph.logistics.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestUtil {
	
	private static final String[] IP_HEADERS = {
	        "X-Forwarded-For",
	        "Proxy-Client-IP",
	        "WL-Proxy-Client-IP",
	        "HTTP_X_FORWARDED_FOR",
	        "HTTP_X_FORWARDED",
	        "HTTP_X_CLUSTER_CLIENT_IP",
	        "HTTP_CLIENT_IP",
	        "HTTP_FORWARDED_FOR",
	        "HTTP_FORWARDED",
	        "HTTP_VIA",
	        "REMOTE_ADDR"
	    };
	
	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	public static String getServerName() {
		return getHttpServletRequest().getServerName();
	}

	public static String getRequestURI() {
		return getHttpServletRequest().getRequestURI();
	}
	
	public static String extractSubDomain() {
		String domain = getServerName();
        int firstDotIndex = domain.indexOf('.'); 
        int dashIndex = domain.indexOf('-'); 
        
        if (firstDotIndex != -1 && dashIndex != -1) {
            return domain.substring(0, firstDotIndex);
        } 
        return null;
	}
	
	public static String getRemoteIp() {

        if (RequestContextHolder.getRequestAttributes() == null) {
            return "0.0.0.0";
        }

        HttpServletRequest request = getHttpServletRequest();
        for (String header: IP_HEADERS) {
            String ipList = request.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                return ipList.split(",")[0];
            }
        }
        return request.getRemoteAddr();
	}
}
