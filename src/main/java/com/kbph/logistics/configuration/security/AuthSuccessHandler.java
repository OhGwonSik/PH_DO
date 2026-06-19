package com.kbph.logistics.configuration.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
//	private static final String DEFAULT_SUCCESE_URI = "/main";
//	private static final String MOBILE_SUFFIX_URI = "/tablet";
//	@Value("${server.servlet.context-path}")
//	private String contextPath;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        Locale.setDefault(new Locale(((CustomUserDetails)authentication.getPrincipal()).getUserInfo().getLangkey()));
//		String checkDevice = null;
//
//		if (request.getHeader("User-Agent") != null) {
//			checkDevice = request.getHeader("User-Agent").toUpperCase();
//		}
//		RequestDispatcher dis = request.getRequestDispatcher(contextPath + DEFAULT_SUCCESE_URI  + ((checkDevice != null && checkDevice.indexOf("MOBILE") != -1) ? (MOBILE_SUFFIX_URI) : ("")));
//		dis.forward(request, response);
	}
}
