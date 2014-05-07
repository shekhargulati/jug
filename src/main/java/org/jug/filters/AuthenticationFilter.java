package org.jug.filters;

import org.jug.view.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by shekhargulati on 18/04/14.
 */
@Provider
@LoggedIn
public class AuthenticationFilter implements ContainerRequestFilter {

	@Context
	private HttpServletRequest request;
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void filter(ContainerRequestContext requestContext)
			throws IOException {
		logger.info("In AuthenticationFilter ...");
		HttpSession session = request.getSession(false);
		logger.info("Session " + session);
		if (session != null) {
			String principal = session.getAttribute("principal") != null ? session.getAttribute("principal").toString() : null;
			String sessionId = session.getId();
			logger.info("Session principal " + principal);
			logger.info("Sesssion id" + sessionId);
		}
		if (session == null || session.getAttribute("principal") == null) {
			logger.info("Returing Forbidden...");
			requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
					.entity(new View("/signin", true)).build());
		}
	}
}