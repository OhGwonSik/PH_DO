package com.kbph.logistics.fcm;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonParseException;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.error.InsertCheckedException;
import com.kbph.logistics.fcm.type.FcmEnum;
import com.kbph.logistics.tm.domain.TmAppDTO;
import com.kbph.logistics.tm.domain.TmDispatchDTO;
import com.kbph.logistics.tm.mapper.TmAppMapper;
import com.kbph.logistics.tm.service.TmAppService;
import com.kbph.logistics.tm.service.TmDispatchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FcmService {

	private final ObjectMapper objectMapper;
	private final TmAppMapper tmAppMapper;
	private final TmAppService tmAppService;
	private final TmDispatchService tmDispatchService;

	@Autowired
	@Qualifier("DC")
	private RestTemplate restTemplate;

	public void fcmPushNotification(TmAppDTO app) throws IOException {

        if(app.getType().equals("tm")) {
			app.setAlarmti("\uD83D\uDE9A 배차 요청이 왔어요");
			app.setAlarmdt("배차계획을 확인해주세요.");
		}else if(app.getType().equals("control")) {
			app.setAlarmti("\uD83C\uDFE0 입동 지시가 왔어요");
			TmAppDTO location = tmAppMapper.selectAlarmControlParameterName(app);
			app.setAlarmdt(location.getLocanam() + "(으)로 입동 해주세요.");
		}else if(app.getType().equals("cancel")) {
			app.setAlarmti("\uD83D\uDE9A 입동 취소");
			app.setAlarmdt("대기장소로 이동해주세요.");
		}
        
        //useract는 각각 개별 기사들의 아이디
        //creuser, lmouser 시스템 상 useract
        //api로 인해 생성되는 경우 schema, creuser, lmouser 다 줘야함
        if(SecurityUtils.getCustomUserDetails() != null && SecurityUtils.getCustomUserDetails().getUserInfo() != null) {
        	app.setSchema(SecurityUtils.getCustomUserDetails().getUserInfo().getSchema());
        	app.setCommonSchema(SecurityUtils.getCustomUserDetails().getUserInfo().getCommonSchema());
        	app.setCreuser(SecurityUtils.getCustomUserDetails().getUserInfo().getUseract());
        	app.setLmouser(SecurityUtils.getCustomUserDetails().getUserInfo().getUseract());
        }
        
        if(app.getUseract() != null && !app.getUseract().equals("")) {
			TmAppDTO user = tmAppService.getUserFcmToken(app);
			int insertCnt = insertAlrmhi(app);
			
			if(user.getFcmtokn() != null && !user.getFcmtokn().equals("")) {
				app.setTargetToken(user.getFcmtokn());
				sendMessageTo(app);
			}
			
			if(insertCnt == 0) {
	        	throw new InsertCheckedException();
	        }
			
		}else {
			TmDispatchDTO dispatch = new TmDispatchDTO();
			dispatch.setVehicky(app.getVehicky());
			dispatch.setSchema(app.getSchema());
			dispatch.setCommonSchema(app.getCommonSchema());
			
			if(app.getVownkey() != null && !app.getVownkey().equals("")) {
				TmDispatchDTO vehicleInformation = tmDispatchService.getVehicleInformation(dispatch);
				dispatch.setVownkey(vehicleInformation.getVownkey());
			}else {
				dispatch.setVownkey(app.getVownkey());
			}
			
			List<TmDispatchDTO> driverList = tmDispatchService.getVehicleDriverMapping(dispatch);
			
			if(driverList != null && !driverList.isEmpty()) {
				for(TmDispatchDTO driver : driverList) {
					app.setUseract(driver.getUseract());
					int insertCnt = insertAlrmhi(app);
					
					TmAppDTO user = tmAppService.getUserFcmToken(app);  
					
					if(user.getFcmtokn() != null && !user.getFcmtokn().equals("")) {
						app.setTargetToken(user.getFcmtokn());
						sendMessageTo(app);
					}
					
					if(insertCnt == 0) {
			        	throw new InsertCheckedException();
			        }
				}
			}
		}
	}

	public boolean sendMessageTo(TmAppDTO app) throws IOException {
        String message = makeMessage(app.getTargetToken(), app.getAlarmti(), app.getAlarmdt());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");

        HttpEntity<String> requestEntity = new HttpEntity<>(message, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(FcmEnum.API_URL.getString(), HttpMethod.POST, requestEntity, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            // 예외를 무시하고 false 반환
            return false;
        }
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
    	targetToken = targetToken.replace("\"", "");
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = FcmEnum.FIREBASE_KEY.getString();

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of(FcmEnum.GOOGLE_URL.getString()));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    public int insertAlrmhi(TmAppDTO app) {
    	int insertCnt = 0;

    	String alarmky = tmAppMapper.selectAlarmky(app.getSchema());

    	app.setAlarmky(alarmky);

    	if(app.getType().equals(FcmEnum.TM.getString())) {
    		app.setAlrmcod(FcmEnum.DISPATCH.getString());
    	}else if(app.getType().equals(FcmEnum.CONTROL.getString())) {
    		app.setAlrmcod(FcmEnum.COMWARE.getString());
    	}else if(app.getType().equals(FcmEnum.CANCEL.getString())) {
    		app.setAlrmcod(FcmEnum.UPCANCEL.getString());
    	}

    	insertCnt += tmAppMapper.insertAlrmhi(app);

    	if(insertCnt == 0) {
    		throw new InsertCheckedException();
    	}

    	return insertCnt;
    }
}
