package com.kbph.logistics.smp.holder;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.kbph.logistics.configuration.error.RestApiException;
import com.kbph.logistics.smp.domain.SmpInstanceResponse;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class SchemaAndFlagHolder {

    private final RestTemplate restTemplate;
    private final Map<String, String> holder = new ConcurrentHashMap<>();
    private static final String SMP_INSTANCE_URI = "/apps/instances/%s";

    public SchemaAndFlagHolder(@Qualifier("SMP") RestTemplate restTemplate) {
    	this.restTemplate = restTemplate;
    }

    public String setSchemaBySMP(HttpServletRequest request) {

        // URL 패턴에서 추출해온 instance id
        // 예) https://{instance}.경북포항.com -> 여기서 추출할 계획이나 해당 도메인 미설정 상태이므로 쿼리 파라미터로 해당 값 주입
        String instance = request.getParameter("instance");
        if(instance == null) {
        	return null;
        }
        ResponseEntity<SmpInstanceResponse> response = restTemplate.exchange(SMP_INSTANCE_URI.formatted(instance), HttpMethod.GET, null, SmpInstanceResponse.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RestApiException();
        }
        SmpInstanceResponse smpInstanceResponse = response.getBody();
        if (smpInstanceResponse == null || CollectionUtils.isEmpty(smpInstanceResponse.getData())) {
            return null;
        }
        // smp api 단일 인스턴스 조회 시 복제된 스키마명 추출하는 코드
        String schema = "";
        schema = Optional.of(smpInstanceResponse.getData())
                .map(apiData -> apiData.get(0)) // 개별 인스턴스 조회시 list에 담기는 객체는 1개이므로 get(0)
                // 어플리케이션에서 다수의 스키마를 소유하고 있을 수 있다고 가정된 api 이기 때문에 list 반환
                // 다수의 스키마를 소유하고 있지 않다고 가정하고 실제로 그러하기 때문에 get(0)
                .map(apiData -> apiData.getSchemas().get(0))
                .map(SmpInstanceResponse.ApiData.Schema::getNewSchema)
                .orElseThrow(RestApiException::new);
        return holder.put("instance", schema);
    }

    public String getSchemaHolder() {
    	return holder.get("instance");
    }

    public String getIsAppUser(String useract) {
    	return holder.get(useract);
    }

    public String setIsAppUser(String useract, String isAppUser) {
    	if(isAppUser == null) {
    		isAppUser = "";
    	}
    	return holder.put(useract, isAppUser);
    }

    public String removeUserInfo(String useract) {
    	return holder.remove(useract);
    }
}
