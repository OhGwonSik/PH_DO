package com.kbph.logistics.configuration.security;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kbph.logistics.common.constant.Constants;
import com.kbph.logistics.configuration.I18nConfig;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomErrorController extends BasicErrorController {
	private final I18nConfig i18nConfig;
	@Value("${server.servlet.context-path}")
	private String contextPath;

	public CustomErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties,
			List<ErrorViewResolver> errorViewResolvers, I18nConfig i18nConfig) {
		super(errorAttributes, serverProperties.getError(), errorViewResolvers);
		this.i18nConfig = i18nConfig;
	}

	@Override
	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE) //요청 json, 응답 html
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		log.error("==CustomErrorController - HTML ip:{}, ErrorAttributes:{}", request.getRemoteAddr(), getErrorAttributes(request)); // 로그 추가

		return customErrorHtml(request, response);
	}

	@Override
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE) //요청 json, 응답 json
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		log.error("==CustomErrorController - API ip:{}, ErrorAttributes:{}", request.getRemoteAddr(), getErrorAttributes(request)); // 로그 추가
		ResponseEntity<Map<String, Object>> error = super.error(request);

		Object errMsg = getErrorAttributes(request).get("message");
		error.getBody().put("message", i18nConfig.getMessageSourceAccessor().getMessage((errMsg != null && !com.kbph.logistics.api.constant.Constants.NO_MESSAGE_AVAILABLE.equals(errMsg.toString().trim()) ? errMsg.toString().trim() : "ms.restApiErr"), LocaleContextHolder.getLocale()));

		return error;
	}

	public ModelAndView customErrorHtml(HttpServletRequest request, HttpServletResponse response) {
		HttpStatus status = this.getStatus(request);
		int statusCode = status.value();
		Map<String, Object> model = new HashMap<>(Collections.unmodifiableMap(this.getErrorAttributes(request, this.getErrorAttributeOptions(request, MediaType.TEXT_HTML))));
		response.setStatus(statusCode);

		return new ModelAndView(Constants.Uri.ERROR_URI, model); // 에러별 view 사용x
	}

	private Map<String, Object> getErrorAttributes(HttpServletRequest request) {
//		return super.getErrorAttributes(request,	isIncludeStackTrace(request, MediaType.TEXT_HTML)
//																										? ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE, ErrorAttributeOptions.Include.MESSAGE)
//																										: ErrorAttributeOptions.defaults());
		return super.getErrorAttributes(request,	ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE, ErrorAttributeOptions.Include.MESSAGE));
	}
}