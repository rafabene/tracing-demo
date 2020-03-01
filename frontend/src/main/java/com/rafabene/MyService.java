package com.rafabene;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.opentracing.util.GlobalTracer;

/**
 * MyService
 */
@ApplicationScoped
public class MyService {

    @Inject
    @RestClient
    MicroserviceBService microservice;

    @Traced
    public String callMicroserviceB(String name){
        GlobalTracer.get().scopeManager()
            .active().span().log("Parameter: " + name);
        String db = microservice.db(name);   
        String kafka = microservice.kafka(name);
        return db + "\n" + kafka;
    }

}