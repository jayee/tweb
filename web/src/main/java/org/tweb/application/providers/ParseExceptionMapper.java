package org.tweb.application.providers;

import org.apache.lucene.queryparser.classic.ParseException;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

/**
 * Created by jonas on 1/24/17.
 */
@Provider
public class ParseExceptionMapper implements ExceptionMapper<ParseException> {

    @Inject
    Logger logger;

    @Override
    public Response toResponse(ParseException exception) {
        logger.severe(exception.getMessage());

        return Response.status(Response.Status.BAD_REQUEST).
                header("reason", "Some parsing error with request string").
                build();
    }

}
