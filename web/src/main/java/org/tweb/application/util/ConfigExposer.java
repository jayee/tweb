/*
 */
package org.tweb.application.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

/**
 * @author jonas
 */
public class ConfigExposer {

    @Inject
    Logger logger;

    @Produces
    public Config expose(InjectionPoint ip) {
        try {
            return ConfigFactory.load();
        } catch (Exception e) {
            if (e.getCause().getClass().isInstance(new FileNotFoundException())) {
                // Use only default reference
                logger.warning("Unable to read config file tweb.conf, using defaults only: " + e.getCause().getMessage());
                return ConfigFactory.defaultReference();
            }
            throw e;
        }


    }
}
