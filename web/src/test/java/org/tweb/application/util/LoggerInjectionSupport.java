package org.tweb.application.util;

import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 */
public class LoggerInjectionSupport {

    @Inject
    Logger logger;

    public Logger getLogger() {
        return logger;
    }

}
