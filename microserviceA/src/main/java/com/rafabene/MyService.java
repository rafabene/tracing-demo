package com.rafabene;


import java.util.concurrent.CompletableFuture;
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
    MicroserviceBService microserviceB;

    @Traced
    public String callMicroserviceBSerial(final String name) {
        GlobalTracer.get().scopeManager().active().span().log("Parameter: " + name);
        final String db = microserviceB.db(name);
        final String kafka = microserviceB.kafka(name);
        final String chain = microserviceB.chain(name);
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
                return microserviceB.db(name);
            }
        });
        final CompletableFuture<String> kafkaFuture = CompletableFuture.supplyAsync(() -> {
            // Activate the server span inside this task
            try (Scope scope = tracer.scopeManager().activate(serverSpan, true)) {
                return microserviceB.kafka(name);
            }
        });
        final CompletableFuture<String> chainFuture = CompletableFuture.supplyAsync(() -> {
            // Activate the server span inside this task
            try (Scope scope = tracer.scopeManager().activate(serverSpan, true)) {
                return microserviceB.chain(name);
            }
        });
        final String result = "PARALLEL: \n" + Stream.of(dbFuture, kafkaFuture, chainFuture)
            .map(CompletableFuture::join)
            .collect(Collectors.joining("\n"));
        return result;
    }

}