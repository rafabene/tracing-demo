package com.rafabene;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * MyService
 */
@ApplicationScoped
public class MyService {

    @Inject
    @RestClient
    private MicroserviceBService microserviceB;

    final String prefix = "Microservice A (from frontend): %s => %s \n";

    @WithSpan
    public String callMicroserviceBSerial(@SpanAttribute("Parameter") final String name) {
        final String db = String.format(prefix, name, microserviceB.db(name));
        final String kafka = String.format(prefix, name, microserviceB.kafka(name));
        final String chain = String.format(prefix, name, microserviceB.chain(name));
        return "SERIAL: \n" + db + kafka + chain;
    }

    @WithSpan
    public String callMicroserviceBParallel(final String name) {
        // We need to get server span to active it on each CompletableFuture
        Span serverSpan = Span.current();
        final CompletableFuture<String> dbFuture = CompletableFuture.supplyAsync(() -> {
            serverSpan.makeCurrent();
            return microserviceB.db(name);
        });
        final CompletableFuture<String> kafkaFuture = CompletableFuture.supplyAsync(() -> {
            serverSpan.makeCurrent();
            return microserviceB.kafka(name);
        });
        final CompletableFuture<String> chainFuture = CompletableFuture.supplyAsync(() -> {
            serverSpan.makeCurrent();
            return microserviceB.chain(name);
        });
        final String result = "PARALLEL: \n" + Stream.of(dbFuture, kafkaFuture, chainFuture)
                .map((cf) -> {
                    return String.format(prefix, name, cf.join());
                })
                .collect(Collectors.joining());
        return result;
    }

}