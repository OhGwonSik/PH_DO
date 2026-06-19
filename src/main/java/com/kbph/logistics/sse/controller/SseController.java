package com.kbph.logistics.sse.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.kbph.logistics.sse.util.SseEmitters;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class SseController {
	
    private final SseEmitters sseEmitters;

    // 클라이언트 연결 엔드포인트
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(HttpServletResponse response) {
        return sseEmitters.addEmitter(response);
    }

    // 알림 전송 엔드포인트
    @PostMapping("/notify")
    public void notifyClients(@RequestParam String message) {
        sseEmitters.sendEvent("alert", message);
    }
}
