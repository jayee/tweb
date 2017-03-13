package org.tweb.application.providers;

import org.tweb.summary.control.LanguageNotFoundException;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

/**
 * Created by jonas on 1/24/17.
 */
@Provider
public class LanguageNotFoundExceptionMapper implements ExceptionMapper<LanguageNotFoundException> {

    @Inject
    Logger logger;

    @Override
    public Response toResponse(LanguageNotFoundException exception) {
        logger.severe(exception.getMessage());

        return Response.status(Response.Status.NOT_FOUND).
                header("reason", "Requested language is not found").
                build();
    }

}
