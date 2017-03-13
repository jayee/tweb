package org.tweb.summary.boundary;

import org.tweb.application.util.LogMethod;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Stateless
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
@Consumes({MediaType.APPLICATION_JSON})
@Interceptors(LogMethod.class)
@Path("summary")
public class SummaryResource {
    
    @Inject
    Summary summary;
    
    @GET
    @Path("{lang}")
    public Response buildSummary(@PathParam("lang") String lang) {
        if ("default".equals(lang)) {
            lang = "";
        }
        return Response.ok(summary.getTranslationSummary(lang)).build();
    }
}
