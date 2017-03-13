package org.tweb.application.providers;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by jonas on 1/24/17.
 */
@Provider
public class IOExceptionMapper implements ExceptionMapper<IOException> {

    @Inject
    Logger logger;

    @Override
    public Response toResponse(IOException exception) {
        logger.severe(exception.getMessage());

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                header("reason", "Some IO error occured").
                build();
    }

}
