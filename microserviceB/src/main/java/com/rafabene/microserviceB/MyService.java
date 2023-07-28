package com.rafabene.microserviceb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.rafabene.microserviceb.db.Message;
import com.rafabene.microserviceb.db.MessageRepository;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;

/**
 * MyService
 */
@Component
public class MyService {

    private static int count = 0;

    private static final String TOPIC = "myTopic";

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private int getNextCount() {
        return count++;
    }

    @WithSpan
    public String storeInDatabase(@SpanAttribute("name") String name) {
        Span span = Span.current();
        span.addEvent("Store In DB called");
        long count = getNextCount();
        String msg = getMessage(name, "DB", count);
        Message message = new Message(msg);
        messageRepository.save(message);

        return msg;
    }

    @WithSpan
    public String storeInKafkaTopic(String name) {
        String msg = getMessage(name, "Kafka", getNextCount());
        kafkaTemplate.send(TOPIC, msg);
        return msg;
    }

    private String getMessage(String name, String dest, long count) {
        return "Microservice B (save to " + dest + "): " + count;
    }

}