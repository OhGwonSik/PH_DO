package com.kbph.logistics.configuration.filter;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbph.logistics.common.constant.Constants;
import com.kbph.logistics.common.util.HttpServletUtils;
import com.kbph.logistics.configuration.I18nConfig;
import com.kbph.logistics.configuration.security.CustomUserDetailService;
import com.kbph.logistics.configuration.security.CustomUserDetails;
import com.kbph.logistics.configuration.security.JwtProvider;
import com.kbph.logistics.smp.holder.SchemaAndFlagHolder;
import com.kbph.logistics.sy.domain.UserVO;
import com.kbph.logistics.sy.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-08-05
 * @note : auth filter
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-08-05					t.s.park							create auth filter
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Slf4j
public class AuthFilter extends UsernamePasswordAuthenticationFilter implements Closeable {
	private final CustomUserDetailService customUserDetailService;
	private final JwtProvider jwtProvider;
	private final I18nConfig i18nConfig;
	private final SchemaAndFlagHolder schemaAndFlagHolder;
	private final UserService userService;
	@Value("${server.servlet.context-path}")
	private String contextPath;
	@Value("${jwt.secret.accessToken-expire-time}")
	private long expTime;

	public AuthFilter(AuthenticationManager authManager,
							JwtProvider jwtProvider,
							CustomUserDetailService customUserDetailService,
							I18nConfig i18nConfig,
							SchemaAndFlagHolder schemaAndFlagHolder,
							UserService userService) {
		setAuthenticationManager(authManager);
		this.jwtProvider = jwtProvider;
		this.customUserDetailService = customUserDetailService;
		this.i18nConfig = i18nConfig;
		this.schemaAndFlagHolder = schemaAndFlagHolder;
		this.userService = userService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		try {
			ObjectMapper om = new ObjectMapper();
			ServletInputStream reqInputStream = request.getInputStream();
			if(reqInputStream == null) {
				return null;
			}
			UserVO userInfo = om.readValue(reqInputStream, UserVO.class);
			schemaAndFlagHolder.setSchemaBySMP(request);
			schemaAndFlagHolder.setIsAppUser(userInfo.getUseract(), userInfo.getIsAppUser());
			CustomUserDetails userDetails = customUserDetailService.loadUserByUsername(userInfo.getUseract());
			UsernamePasswordAuthenticationToken accessToken = new UsernamePasswordAuthenticationToken(userDetails, userInfo.getPasswrd(), null);
			setDetails(request, accessToken);

			return getAuthenticationManager().authenticate(accessToken);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		try {
	        LocaleContextHolder.setLocale(new Locale(((CustomUserDetails)authResult.getPrincipal()).getUserInfo().getLangkey()));
			String accessToken = jwtProvider.generateAccessToken(authResult);
			Map<String, String> res = new HashMap<>();
			res.put(Constants.Auth.ACCESS_TOKEN_NAME, accessToken);
			res.put("redirectUrl", contextPath + Constants.Uri.DEFAULT_SUCCESE_URI  + (HttpServletUtils.isMobile(request) ? (Constants.Uri.MOBILE_SUFFIX_URI) : ("")));
			res.put("langkey", ((CustomUserDetails)authResult.getPrincipal()).getUserInfo().getLangkey());
			JSONObject jsonObj = new JSONObject(res);
			response.setContentType("application/json;charset=UTF-8");
			response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
			response.setHeader("Access-Control-Allow-Credentials", "true");
			request.getSession().setAttribute(Constants.Auth.ACCESS_TOKEN_NAME, accessToken);
			
			PrintWriter out = response.getWriter();
			out.print(jsonObj);
			out.flush();
			out.close();
			//사용자 로그인 이력 저장
			userService.insertUserLoginHistory(((CustomUserDetails)authResult.getPrincipal()).getUserInfo());
			
			this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
		} catch (Exception e) {
			e.getStackTrace();
		}
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    	try {
			Map<String, String> res = new HashMap<>();
			res.put("redirectUrl", contextPath + Constants.Uri.DEFAULT_FAILURE_URI + "?error=" + true + "&exception=" + i18nConfig.getMessageSourceAccessor().getMessage(URLEncoder.encode(failed.getMessage(),"UTF-8"), LocaleContextHolder.getLocale()));
			JSONObject jsonObj = new JSONObject(res);

			response.setContentType("application/json;charset=UTF-8");
			response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
			response.setHeader("Access-Control-Allow-Credentials", "true");
			PrintWriter out = response.getWriter();
			out.print(jsonObj);
			out.flush();
			out.close();

        	this.getFailureHandler().onAuthenticationFailure(request, response, failed);
    	} catch (Exception e) {
			e.getStackTrace();
		}
    }

	@Override
	public void close() throws IOException {
		log.info("Stream closed");
	}
}
