package com.rafabene;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@ApplicationScoped
public class RestResource {

    @Inject
    private MyService service;

    @Inject
    @RestClient
    MicroserviceBService microservice;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/serial/{name}")
    public String serial(@PathParam("name") String name) {
        Log.info("Received request for serial call with parameter: " + name);
        return service.callMicroserviceBSerial(name);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/parallel/{name}")
    public String parallel(@PathParam("name") String name) {
        Log.info("Received request for parallel call with parameter: " + name);
        return service.callMicroserviceBParallel(name);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/chain/{name}")
    public String chain(@PathParam("name") String name) {
        Log.info("Received request for chained call with parameter: " + name);
        return "MicroserviceA ==> " + microservice.chain(name);
    }

}