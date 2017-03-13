package org.tweb.search.boundary;

import org.tweb.search.control.SearchException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 *
 */
@Stateless
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
@Consumes({MediaType.APPLICATION_JSON})
@Path("search")
public class SearchResource {
    
    @Inject
    Search search;
    
    @GET
    @Path("{str}")
    public Response searchString(@PathParam("str") String str) throws IOException, SearchException {
        return Response.ok(search.search(str)).build();
    }
}
