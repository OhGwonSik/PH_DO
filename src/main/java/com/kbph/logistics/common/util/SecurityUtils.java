package com.kbph.logistics.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.kbph.logistics.configuration.security.CustomUserDetails;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityUtils {
	public static SecurityContext getContext() {
		return SecurityContextHolder.getContext();
	}

	public static Authentication getAuthentication() {
		return getContext().getAuthentication();
	}

	public static Object getPrincipal() {
		return getAuthentication().getPrincipal();
	}

	public static CustomUserDetails getCustomUserDetails() {
		if(getPrincipal() instanceof CustomUserDetails customUserDetails) {
			customUserDetails = (CustomUserDetails)getPrincipal();
			return customUserDetails;
		}

		return null;
	}

	public static String getSchema() {
		CustomUserDetails customUserDetails = getCustomUserDetails();

		if(customUserDetails != null && customUserDetails.getUserInfo() != null) {
			return customUserDetails.getUserInfo().getSchema();
		}

		return null;
	}
}
