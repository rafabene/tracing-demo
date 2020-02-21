package com.rafabene;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class FrontendResource {

    @Inject
    MyService service;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/serial/{name}")
    public String serial(@PathParam("name") String name) {
        return service.callMicroserviceB(name);
    }

}