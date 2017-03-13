package org.tweb.translation.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;

import static javax.ws.rs.HttpMethod.POST;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.tweb.application.util.LogMethod;
import org.tweb.translation.entity.Translation;

/**
 *
 */
@Stateless
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
@Consumes({MediaType.APPLICATION_JSON})
@Interceptors(LogMethod.class)
@Path("translations/{translationpath: .+}")
public class TranslationsResource {

    @Inject
    Translations translations;

    @GET
    public Response find(@PathParam("translationpath") String path, @DefaultValue("") @QueryParam("lang") String lang) {
        return Response.ok(translations.find(path, lang)).build();
    }

    @PUT
    public Response update(@PathParam("translationpath") String path, Translation translation) {
        translations.update(path, translation);
        return Response.ok().build();
    }
    
    
}
