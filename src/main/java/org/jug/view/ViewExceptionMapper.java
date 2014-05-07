package org.jug.view;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by shekhargulati on 21/03/14.
 */
@Provider
public class ViewExceptionMapper implements ExceptionMapper<ViewException> {
	
    @Override
    public Response toResponse(ViewException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new View("/500", exception.getMessage(), "error").setTemplateEngine(exception.getTemplateEngine())).build();
    }
}
