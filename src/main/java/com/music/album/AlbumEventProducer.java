package com.music.album;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AlbumEventProducer {

    private static Logger LOGGER = LoggerFactory.getLogger(AlbumEventProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public AlbumEventProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void produce(UUID key, Object event) {
        try {
            kafkaTemplate.send("album", key.toString(), objectMapper.writeValueAsString(event));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
