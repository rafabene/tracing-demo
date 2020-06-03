package com.rafabene;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RestClient;

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
        return service.callMicroserviceBSerial(name);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/parallel/{name}")
    public String parallel(@PathParam("name") String name) {
        return service.callMicroserviceBParallel(name);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/chain/{name}")
    public String chain(@PathParam("name") String name) {
        return "MicroserviceA ==> " + microservice.chain(name);
    }

}