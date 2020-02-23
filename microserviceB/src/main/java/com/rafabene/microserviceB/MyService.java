package com.rafabene.microserviceB;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private Tracer tracer;

    public String storeInDatabase(String name){
        // Spring doesn't have @Traced annotation, thus we need to create
        // a Span manually. See: https://github.com/opentracing-contrib/java-spring-cloud/issues/98
        Span span = tracer.buildSpan("service call").start();
        tracer.activateSpan(span);
        try{
            count++;
            String msg = "Microservice A (from frontend): " + name + " => Microservice B (save to DB): " + count;
            span.log("Parameter: " + name + ", count=" + count );
            Message message = new Message(msg);
            messageRepository.save(message);
            return msg;
        }finally{
            span.finish();
        }
    }
    
}