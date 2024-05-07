package com.music.album;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class AlbumEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumEventConsumer.class);

    @KafkaListener(topics = "album", groupId = "music-group-id")
    public void listen(
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header("event-name") String eventName,
            @Header("trace-id") String traceId,
            @Header("span-id") String spanId,
            @Payload String message) {
        LOGGER.info("key = {}", key);
        LOGGER.info("event-name = {}", eventName);
        LOGGER.info("trace-id = {}", traceId);
        LOGGER.info("span-id = {}", spanId);
        LOGGER.info("message = {}", message);
    }
}
