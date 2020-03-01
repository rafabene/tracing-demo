package com.rafabene;

import java.util.Enumeration;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/")
public class RestResource {

    @Context
    HttpServletRequest request;

    @Inject
    MyService service;

    @Inject
    @RestClient
    MicroserviceBService microservice;

    private static final Logger LOG = Logger.getLogger("RestResource");

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
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder sb = new StringBuilder();
        while(headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            sb.append(headerName + ":" + headerValue + "\n");
        }
        LOG.info(sb.toString());
        return "MicroserviceA ==> " + microservice.chain(name);
    }

}