package org.tweb.application.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;


/**
 *
 */
@ApplicationPath("resources")
public class JAXRSConfiguration extends ResourceConfig {

    public JAXRSConfiguration() {
        register(JacksonFeature.class);
        packages(true, "org.tweb");
    }
    
}
