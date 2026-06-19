package com.kbph.logistics.configuration.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthFailureHandler implements AuthenticationFailureHandler {
//	private final I18nConfig i18nConfig;
//	private static final String DEFAULT_FAILURE_URL = "/login";
//	@Value("${server.servlet.context-path}")
//	private String contextPath;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
//		response.setContentType("application/json;charset=UTF-8");
//		RequestDispatcher dis = request.getRequestDispatcher(contextPath + DEFAULT_FAILURE_URL + "?error=" + true + "&exception=" + i18nConfig.getMessageSourceAccessor().getMessage(URLEncoder.encode(exception.getMessage(),"UTF-8"), Locale.getDefault()));
//		dis.forward(request, response);
	}
}