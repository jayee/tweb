package org.tweb.application.providers;

import org.tweb.search.control.SearchException;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

/**
 * Created by jonas on 1/24/17.
 */
@Provider
public class SearchExceptionMapper implements ExceptionMapper<SearchException> {

    @Inject
    Logger logger;

    @Override
    public Response toResponse(SearchException exception) {
        logger.severe(exception.getMessage());

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                header("reason", "Search string parse exception").
                build();
    }

}
