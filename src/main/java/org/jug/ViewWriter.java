package org.jug;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.spi.InternalServerErrorException;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class ViewWriter implements MessageBodyWriter<Object> {

    private ViewResolver viewResolver = new ViewResolver();

    @Override
    public long getSize(Object obj, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return viewResolver.isResolvable(type, genericType, annotations);
    }

    @Override
    public void writeTo(Object obj, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws IOException, WebApplicationException {
        Viewable view = viewResolver.getView(obj, type, genericType, annotations);

        if (view == null)
            throw new InternalServerErrorException("No View annotation found for object of type " + type.getName());

        HttpServletRequest request = ResteasyProviderFactory.getContextData(HttpServletRequest.class);
        HttpServletResponse response = ResteasyProviderFactory.getContextData(HttpServletResponse.class);
        Cookie[] cookies = request.getCookies();
        boolean jsessionIdCookieExists = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), "JSESSIONID")) {
                    System.out.println("Cookie exist with JSESSIONID " + cookie.getValue());
                    jsessionIdCookieExists = true;
                }
            }
        }

        if (!jsessionIdCookieExists && notStaticResource(request)) {
            System.out.println("Setting new cookie for requet with requestURI " + request.getRequestURI());
            System.out.println("Setting new cookie for request with serveltPath " + request.getServletPath());
            System.out.println("Setting new cookie for request with contextPath " + request.getContextPath());
            Cookie sessionCookie = new Cookie("JSESSIONID", request.getSession().getId());
            response.addCookie(sessionCookie);
        }
        try {
            if (view.isRedirect()) {
                String contextPath = request.getContextPath();
                response.sendRedirect(contextPath + view.getPath());
            } else {
                String processedTemplate = view.render(request, response);
                String charset = mediaType.getParameters().get("charset");
                if (charset == null) entityStream.write(processedTemplate.getBytes());
                else entityStream.write(processedTemplate.getBytes(charset));
            }
        } catch (ServletException ex) {
            throw new WebApplicationException(ex);
        }
    }

    private boolean notStaticResource(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        if (StringUtils.endsWith(servletPath, "js") || StringUtils.endsWith(servletPath, "css") || StringUtils.endsWith(servletPath, "styles") || StringUtils.endsWith(servletPath, "scripts")) {
            return false;
        }
        return true;

    }

    public ViewResolver getViewResolver() {
        return viewResolver;
    }

    public void setViewResolver(ViewResolver viewResolver) {
        this.viewResolver = viewResolver;
    }

}
