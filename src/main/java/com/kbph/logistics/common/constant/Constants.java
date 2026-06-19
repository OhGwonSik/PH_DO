package com.kbph.logistics.common.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
	@UtilityClass
	public static class Uri {
		public static final String ALERT_FAILURE_URI = "/alert";
		public static final String AUTH_URI = "/auth";
		public static final String DEFAULT_FAILURE_URI = "/login";
		public static final String DEFAULT_SUCCESE_URI = "/main";
		public static final String ERROR_URI = "/error";
		public static final String LOGIN_URI = "/login";
		public static final String LOGOUT_URI = "/logout";
		public static final String MOBILE_SUFFIX_URI = "/tablet";
		public static final String MAIN_URI = "/main";
		public static final String SIGN_UP_URI = "/signup";
		public static final String DOWNLOAD_URI = "/download";
	}

	@UtilityClass
	public static class Auth {
		public static final String ACCESS_TOKEN_NAME = "accessToken";
		public static final String ROLE_PREFIX = "ROLE_";
		public static final String USER = "USER";
		public static final String USER_NAME = "useract";
		public static final int ACCOUNT_LOCK_LIMIT = 5;
		public static final int CACHE_RELOAD_SEC = 1;
	}
	
	@UtilityClass
	public static class ApplicationInfo {
		public static final String APPLICATION_NAME = "DO";
	}
}
