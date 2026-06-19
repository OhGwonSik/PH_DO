package com.kbph.logistics.sse.util;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class SseEmitters {
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter addEmitter(HttpServletResponse response) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30-minute timeout
        
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("X-Accel-Buffering", "no");

        // Add emitter to the list
        emitters.add(emitter);

        // Set up completion, timeout, and error handlers
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        // Send an initial message when the connection is established
        try {
            emitter.send(SseEmitter.event().name("connect").data("Connection established"));
        } catch (IOException e) {
            emitters.remove(emitter);
        }

        return emitter;
    }

    public void sendEvent(String eventName, Object data) {
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (IOException e) {
                emitters.remove(emitter); // 실패한 Emitter 제거
            }
        });
    }
}