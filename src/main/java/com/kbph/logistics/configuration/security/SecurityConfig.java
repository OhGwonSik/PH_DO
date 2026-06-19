package com.kbph.logistics.configuration.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import com.kbph.logistics.common.constant.Constants;
import com.kbph.logistics.configuration.I18nConfig;
import com.kbph.logistics.configuration.filter.AuthFilter;
import com.kbph.logistics.configuration.filter.AuthorizationFilter;
import com.kbph.logistics.configuration.type.UserRole;
import com.kbph.logistics.smp.holder.SchemaAndFlagHolder;
import com.kbph.logistics.sy.service.UserService;

import lombok.RequiredArgsConstructor;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-07-09
 * @note : Spring security config
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-07-09					t.s.park        					create security class
 * 2024-07-19					t.s.park							change security role
 * 2024-08-05					t.s.park							change security to token
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private static final String[] DEFAULT_LIST = {
			"/login", "/signup/**", "/logout", "/error", "/download"
	}; // permit for all user
	private static final String[] RESOURCE_LIST = {
			"/design/**", "/jasper/**", "/js/**", "/library/**", "/tablet/**", "/manifests/**", "/favicon.ico", "/img/**", "/appapk/**"
	}; // permit for all user
	private static final String[] USER_API_LIST = {
			"${server.servlet.page-path}/**",
			"/md/**", "/rm/**", "/im/**", "/sm/**", "/tm/**", "/om/**", "/bm/**"
	}; // permit for authenticated user
	private static final String[] ADMIN_API_LIST = {
			"/sy/**",
	}; // permit for authenticated admin
	private static final String[] API_SERVER_LIST = {
			"/api/**", "/sse/**"
	}; // permit for authenticated user
	private static final String[] APP_API_LIST = {
			"/app/version/get", "/app/version/update"
	}; // permit for authenticated user, 앱 업데이트관련 (초기 진입시 작동하기위해)
	private final CustomUserDetailService customUserDetailService;
	private final I18nConfig i18nConfig;
	private final AuthSuccessHandler authSuccessHandler;
	private final AuthFailureHandler authFailureHandler;
	private final JwtProvider jwtProvider;
	private final AuthProvider authProvider;
	private final SchemaAndFlagHolder schemaAndFlagHolder;
	private final UserService userService;
	@Value("${server.servlet.context-path}")
	private String contextPath;
	@Value("${server.servlet.page-path}")
	private String pagePath;

	@Bean
	protected AuthFilter authFilter() { // 로그인시 필터
		AuthFilter authFilter = new AuthFilter(authenticationManager(), jwtProvider, customUserDetailService, i18nConfig, schemaAndFlagHolder, userService);
		authFilter.setFilterProcessesUrl(Constants.Uri.AUTH_URI + Constants.Uri.LOGIN_URI); // /auth/login
		authFilter.setAuthenticationSuccessHandler(authSuccessHandler); // 인증 성공시 핸들러
		authFilter.setAuthenticationFailureHandler(authFailureHandler); // 인증 실패시 핸들러
		authFilter.afterPropertiesSet();

		return authFilter;
	}

	@Bean
	protected AuthenticationManager authenticationManager() { // 인증 매니저 생성
		return new ProviderManager(authProvider);
	}

    @Bean
    protected WebSecurityCustomizer webSecurityCustomizer(){ // 필터 제외(resource 경로)
        return web -> web.ignoring().requestMatchers(RESOURCE_LIST);
    }

	@Bean
	protected List<String> getTokenExcludePathList(){ // 로그인 후 인증필터 통과시에 제외할 url
		List<String> excludePathList = new ArrayList<>();
		excludePathList.add(contextPath + Constants.Uri.MAIN_URI); // /platform/main
		excludePathList.add(contextPath + pagePath); // /platform/page

		return excludePathList;
	}

	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(
						csrf -> csrf.disable()
				)
    			.cors(
    					cors -> cors.disable()
    			)
                .headers(
                		headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable).disable() // for no-cors
                )
                .sessionManagement(
                		session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // using thymeleaf session
                								.invalidSessionUrl(Constants.Uri.LOGIN_URI)
				)
				.authorizeHttpRequests(
						authorize -> authorize.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 일반 static resource 허용
														.requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // app preflight 허용
														.requestMatchers(DEFAULT_LIST).permitAll()
														.requestMatchers(RESOURCE_LIST).permitAll()
														.requestMatchers(APP_API_LIST).permitAll()
//    													.requestMatchers(USER_API_LIST).hasAnyRole(UserRole.ADMIN.name(), UserRole.USER.name(), UserRole.DRIVER.name(), UserRole.OWNER.name(), UserRole.TABLET.name())
    													.requestMatchers(USER_API_LIST).permitAll()
    													.requestMatchers(ADMIN_API_LIST).permitAll()	// /sy/** 내 레이아웃 그리드 세팅 포함하여 임시로 permitAll.
    													.requestMatchers(API_SERVER_LIST).permitAll()
														.anyRequest().authenticated()
				)
				.logout(
						logout -> logout.logoutUrl(Constants.Uri.LOGOUT_URI)
												.logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
												.invalidateHttpSession(true)
												.deleteCookies("JSESSIONID") // 세션 삭제
												.logoutSuccessUrl(Constants.Uri.LOGIN_URI)
				)
				.addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(new AuthorizationFilter(customUserDetailService, jwtProvider, getTokenExcludePathList()), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
