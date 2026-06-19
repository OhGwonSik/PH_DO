package com.kbph.logistics.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.kbph.logistics.common.constant.Constants;

import jakarta.servlet.http.HttpServletRequest;

@Primary
@Configuration
public class I18nConfig implements WebMvcConfigurer {
	@Value("${server.servlet.context-path}")
	private String contextPath;
	@Value("${message.path}")
	private String messagePath;

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
		source.setBasename(messagePath);// 메세지 프로퍼티파일의 위치와 이름을 지정한다.
		source.setDefaultEncoding("UTF-8");// 기본 인코딩을 지정한다.
		source.setCacheSeconds(Constants.Auth.CACHE_RELOAD_SEC); // 프로퍼티 파일의 변경을 감지할 시간 간격을 지정한다. 리로드 시간
		source.setUseCodeAsDefaultMessage(true); // 없는 메세지일 경우 예외를 발생시키는 대신 코드를 기본 메세지로 한다.

		return source;
	}

	@Bean
	public MessageSourceAccessor getMessageSourceAccessor() {
		return new MessageSourceAccessor(this.messageSource());
	}

	@Bean // 세션 방식을 사용한다.
	public SessionLocaleResolver localeResolver(HttpServletRequest request) {
		return new SessionLocaleResolver();
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("langkey");

		return interceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
}