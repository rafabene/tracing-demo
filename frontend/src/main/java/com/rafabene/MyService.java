package com.rafabene;

import java.time.temporal.ChronoUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
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
        return microservice.db(name);
    }

}