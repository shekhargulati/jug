package org.jug.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by shekhargulati on 18/04/14.
 */
@AfterLogin
@Provider
public class AfterLoginFilter implements ContainerResponseFilter {

    @Context
    private HttpServletRequest request;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        logger.info("Inside AfterLoginFilter...");
        int status = responseContext.getStatus();
        if (status == Response.Status.OK.getStatusCode()) {
            logger.info("User is successfully loggedin..");
            HttpSession session = request.getSession(true);
            System.out.println("AfterLoginFilter sessionId : " + session.getId());
            session.setAttribute("principal", responseContext.getEntity());
        }
    }


}