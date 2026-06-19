package com.kbph.logistics.api.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.kbph.logistics.api.domain.ApiCommonDTO;
import com.kbph.logistics.api.enums.ApiInfo;
import com.kbph.logistics.common.util.DateUtil;
import com.kbph.logistics.common.util.RequestUtil;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.error.RestApiException;
import com.kbph.logistics.configuration.security.CustomUserDetails;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ApiConfig {
	private final RestTemplate restTemplate;

	public ApiConfig(@Qualifier("DC") RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public <T> ResponseEntity<String> requestToDC(List<T> dataList, ApiInfo apiInfo, HttpMethod httpMethod) {
		CustomUserDetails userDetails = SecurityUtils.getCustomUserDetails();
		String subDomain = RequestUtil.extractSubDomain() != null ? RequestUtil.extractSubDomain() : "";
		if (userDetails != null && userDetails.getUserInfo() != null 
		    && !subDomain.equals("do-dy")) {	
		    return null;
		}

		ApiCommonDTO<T> apiRequest = new ApiCommonDTO<>();
		apiRequest.setApiCode(apiInfo.getApiCode());
		apiRequest.setApiData(dataList);
		apiRequest.setSendDate(DateUtil.getDate("yyMMdd"));
		apiRequest.setSendTime(DateUtil.getTime("HHmmss"));
		apiRequest.setSchema("DO_DY");

		ResponseEntity<String> responseEntity = null;
		// restTepmplate patch 사용을 위해 추가
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		try {
			log.info("apiUrl => {}", apiInfo.getUrl());
			responseEntity = restTemplate.exchange(apiInfo.getUrl(), httpMethod, new HttpEntity<>(apiRequest), String.class);
			if (!responseEntity.getStatusCode().is2xxSuccessful()) {
				throw new RestApiException();
			}
			log.info("api response => {}", responseEntity);
			return responseEntity;
		} catch (Exception e) {
			throw new RestApiException();
		}
	}
}
