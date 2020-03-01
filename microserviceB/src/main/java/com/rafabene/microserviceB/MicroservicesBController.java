package com.rafabene.microserviceb;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.opentracing.contrib.spring.web.client.TracingRestTemplateInterceptor;
import io.opentracing.util.GlobalTracer;


/**
 * MicroservicesBController
 */
@RequestMapping("/")
@RestController
public class MicroservicesBController {

    @Autowired
    private MyService service;


    @Value("${microservicec_URL}")
    private String msURL;

    @RequestMapping(path = "/db/{name}", method = RequestMethod.GET)
    public String dbEndpoint(@PathVariable("name") String name){    
        return service.storeInDatabase(name);
    }

    @RequestMapping(path = "/kafka/{name}", method = RequestMethod.GET)
    public String kafkaEndpoint(@PathVariable("name") String name){    
        return service.storeInKafkaTopic(name);
    }

    @RequestMapping(path = "/chain/{name}", method = RequestMethod.GET)
    public String chain(@PathVariable("name") String name){  
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate
            .setInterceptors(Collections.singletonList(
                new TracingRestTemplateInterceptor(GlobalTracer.get())));
        return "Microservice B ==> " + 
            restTemplate
                .getForEntity(msURL, String.class, params)
                .getBody();
    }

    
}