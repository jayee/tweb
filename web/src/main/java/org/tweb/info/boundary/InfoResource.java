package org.tweb.info.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Stateless
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
@Consumes({MediaType.APPLICATION_JSON})
@Path("info")
public class InfoResource {
    
    @Inject
    ApplicationInfo info;
    
    @GET
    public Response getApplicationInfo() {
        return Response.ok(info.getApplicationInfoJson()).build();
    }
}
