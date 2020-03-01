package com.rafabene.microserviceB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

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

    @Autowired
    private Tracer tracer;

    public String storeInDatabase(String name){
        // Spring doesn't have @Traced annotation, thus we need to create
        // a Span manually. See: https://github.com/opentracing-contrib/java-spring-cloud/issues/98
        Span span = tracer.buildSpan("service call").start();
        tracer.activateSpan(span);
        try{
            count++;
            String msg = getMessage(name, "DB", count);
            span.log("Parameter: " + name + ", count=" + count );
            Message message = new Message(msg);
            messageRepository.save(message);
            return msg;
        }finally{
            span.finish();
        }
    }

    public String storeInKafkaTopic(String name){
        String msg = getMessage(name, "Kafka", count);
        kafkaTemplate.send(TOPIC, msg);
        return msg;
    }

    private String getMessage(String name, String dest, int count){
        return "Microservice A (from frontend): " + name + " => Microservice B (save to " + dest + "): " + count;
    }
    
}