package com.craft.demo.service;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {
    private final Map<Long, CopyOnWriteArrayList<SseEmitter>> emitters = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(SseService.class);
    public SseEmitter subscribe(long jobId) {
        final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        CopyOnWriteArrayList<SseEmitter> emitterList = emitters.getOrDefault(jobId, new CopyOnWriteArrayList<>());
        emitterList.add(emitter);
        this.emitters.put(jobId, emitterList);
        emitter.onCompletion(() -> this.emitters.get(jobId).remove(emitter));

        return emitter;
    }

    public void sendEvent(final String message, final long jobId) {
        this.emitters.get(jobId).forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("event-test")
                        .data(message));
            } catch (final Exception e) {
                emitter.complete();
            }
        });
    }
}
