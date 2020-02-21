package com.rafabene;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.opentracing.Traced;

import io.opentracing.util.GlobalTracer;

/**
 * MyService
 */
@ApplicationScoped
public class MyService {

    @Traced
    public String callMicroserviceB(String name){
        GlobalTracer.get().scopeManager().active().span().log(name);
        return name;
    }

    @Traced
    public String callMicroserviceC(){
        return "MethodB";
    }
}