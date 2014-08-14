package org.jug.filters;

import org.jug.view.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by shekhargulati on 09/05/14.
 */
@Provider
@InjectPrincipal
public class InjectPrincipalFilter implements ContainerResponseFilter {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Context
    private HttpServletRequest request;


    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        HttpSession session = request.getSession(false);
        logger.info("Inside InjectPrincipalFilter filter() with session " + session);
        if (session != null && session.getAttribute("principal") != null && responseContext.hasEntity()) {
            View view = (View) responseContext.getEntity();
            Map<String, Object> model = view.getModel();
            Object principal = session.getAttribute("principal");
            model.put("principal", principal);
        }
    }
}
