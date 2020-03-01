package com.rafabene;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
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
    public String callMicroserviceBSerial(final String name) {
        GlobalTracer.get().scopeManager().active().span().log("Parameter: " + name);
        final String db = microservice.db(name);
        final String kafka = microservice.kafka(name);
        final String chain = microservice.chain(name);
        return "SERIAL: \n" + db + "\n" + kafka + "\n" + chain;
    }

    @Traced
    public String callMicroserviceBParallel(final String name) {
        Tracer tracer = GlobalTracer.get();
        // We need to get server span to active it on each CompletableFuture
        Span serverSpan = tracer.scopeManager().active().span();
        serverSpan.log("Parameter: " + name);
        final CompletableFuture<String> dbFuture = CompletableFuture.supplyAsync(() -> {
            // Activate the server span inside this task
            try (Scope scope = tracer.scopeManager().activate(serverSpan, true)) {
                return microservice.db(name);
            }
        });
        final CompletableFuture<String> kafkaFuture = CompletableFuture.supplyAsync(() -> {
            // Activate the server span inside this task
            try (Scope scope = tracer.scopeManager().activate(serverSpan, true)) {
                return microservice.kafka(name);
            }
        });
        final CompletableFuture<String> chainFuture = CompletableFuture.supplyAsync(() -> {
            // Activate the server span inside this task
            try (Scope scope = tracer.scopeManager().activate(serverSpan, true)) {
                return microservice.chain(name);
            }
        });
        final String result = "PARALLEL: \n" + Stream.of(dbFuture, kafkaFuture, chainFuture)
            .map(CompletableFuture::join)
            .collect(Collectors.joining("\n"));
        return result;
    }

}