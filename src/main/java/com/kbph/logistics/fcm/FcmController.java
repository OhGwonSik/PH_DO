package com.kbph.logistics.fcm;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.tm.domain.TmAppDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FcmController {

	private final FcmService fcmService;
	
	
	@PostMapping("/fcm/api")
	public ResponseEntity<Void> pushTestMessage(@RequestBody TmAppDTO app) {
		
		//fcm은 토큰 유효성 검사를 할 수가 없음
		//따로 에러처리가 필요하나 이 부분에 대해서는 시연이 끝난 후 논의가 필요함
		//현재 에러를 우선 없애달라고 하여 일단 200처리
		
		try {
			fcmService.fcmPushNotification(app);
	        return ResponseEntity.ok().build(); // 200 OK  
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error  
	    }
	}
}
