package com.kbph.logistics.configuration;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class BeanConfig {
	// API 서버 기본 URL
	private static final String ROOT_URL_API = "https://api.kpslp.kr";
//	private static final String ROOT_URL_API = "http://localhost:2000";
	// SMP 서버 기본 URL
	private static final String ROOT_URL_SMP = "https://smp.apps:8003";
	private static final long CONNECT_TIMEOUT = 3000;// 연결 제한시간
	private static final long READ_TIMEOUT = 3000;// 리스폰스 제한시간

	@Bean(name = "DC")
	RestTemplate restTemplateDC(RestTemplateBuilder builder) {
		return builder.rootUri(ROOT_URL_API).setConnectTimeout(Duration.ofMillis(CONNECT_TIMEOUT)).setReadTimeout(Duration.ofMillis(READ_TIMEOUT)).build();
	}

	@Bean(name = "SMP")
	RestTemplate restTemplateSMP(RestTemplateBuilder builder) {
		return builder.rootUri(ROOT_URL_SMP)
				.setConnectTimeout(Duration.ofMillis(CONNECT_TIMEOUT))
				.setReadTimeout(Duration.ofMillis(READ_TIMEOUT))
				.build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOriginPattern("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.setMaxAge(6000L);
		config.addExposedHeader("Content-Disposition");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean<CorsFilter> filterBean = new FilterRegistrationBean<>(new CorsFilter(source));
		filterBean.setOrder(0);

		return filterBean;
	}
}
