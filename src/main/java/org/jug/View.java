package org.jug;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class View implements Viewable {

    public static final String DEFAULT_MODEL_NAME = "model";
    private List<String> errors;

    /** */
    private String path;
    private Object model;
    private String modelName;
    private boolean redirect;

    public View(String path) {
        this(path, null, null);
    }

    public View(String path, Object model) {
        this(path, model, DEFAULT_MODEL_NAME);
    }

    public View(String path, Object model, String modelName) {
        this.path = path;
        this.model = model;
        this.modelName = modelName;
    }

    public View(String path, Object model, String modelName, List<String> errors) {
        this.path = path;
        this.model = model;
        this.modelName = modelName;
        this.errors = errors;
    }

    public View(String path, boolean redirect) {
        this.path = path;
        this.redirect = redirect;
    }

    public String getPath() {
        return this.path;
    }

    public Object getModel() {
        return this.model;
    }

    public String getModelName() {
        return this.modelName;
    }

    public String render(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, WebApplicationException {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setTemplateMode("HTML5`");
        templateResolver.setPrefix("/WEB-INF/templates");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(3600000L);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        WebContext context = new WebContext(request, response, request.getServletContext());
        if (this.model != null) {
            if (this.model instanceof Map) {
                Map<String, Object> map = (Map) this.model;
                Set<Map.Entry<String, Object>> entries = map.entrySet();
                for (Map.Entry<String, Object> entry : entries) {
                    context.setVariable(entry.getKey(), entry.getValue());
                }
            } else {
                context.setVariable(this.modelName, this.model);
            }
        }
        if (errors != null && !errors.isEmpty()) {
            context.setVariable("errors", errors);
        }
        return templateEngine.process(this.path, context);
    }

    public boolean isRedirect() {
        return this.redirect;
    }

}
