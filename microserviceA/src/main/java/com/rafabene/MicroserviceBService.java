package com.rafabene;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * MicroserviceService
 */
@RegisterRestClient(configKey = "serviceb")
@RegisterClientHeaders
public interface MicroserviceBService {

    @Path("/db/{name}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String db(@PathParam("name") String name);

    @Path("/kafka/{name}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String kafka(@PathParam("name") String name);

    @Path("/chain/{name}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String chain(@PathParam("name") String name);

}