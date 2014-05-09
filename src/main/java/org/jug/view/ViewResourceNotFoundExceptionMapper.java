package org.jug.view;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by shekhargulati on 21/03/14.
 */
@Provider
public class ViewResourceNotFoundExceptionMapper implements ExceptionMapper<ViewResourceNotFoundException> {

    @Override
    public Response toResponse(ViewResourceNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND).entity(new View("/404", exception.getMessage(), "error").setTemplateEngine(exception.getTemplateEngine())).build();
    }
}
