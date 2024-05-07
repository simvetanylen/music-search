package com.music.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.trace.Span;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
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
            var span = Span.current().getSpanContext();
            var record = new ProducerRecord<>(
                    "album",
                    null,
                    key.toString(),
                    objectMapper.writeValueAsString(event),
                    List.of(
                            new RecordHeader("event-name", event.getClass().getName().getBytes(StandardCharsets.UTF_8)),
                            new RecordHeader("trace-id", span.getTraceId().getBytes(StandardCharsets.UTF_8)),
                            new RecordHeader("span-id", span.getSpanId().getBytes(StandardCharsets.UTF_8))
                    )
            );
            kafkaTemplate.send(record);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
