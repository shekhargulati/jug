package org.jug.view;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;


public class View implements Viewable {

    public static final String DEFAULT_MODEL_NAME = "model";

    private List<String> errors;

    /** */
    private String path;
    private Object model;
    private String modelName;
    private boolean redirect;
    private  boolean absolute;

	private TemplateEngine templateEngine;

	
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
        this.absolute = false;
    }

    public View(String path, boolean redirect, Object model) {
        this.path = path;
        this.redirect = redirect;
        this.absolute = false;
        this.model = model;
    }

    public View(String path, boolean redirect, boolean absolute) {
        this.path = path;
        this.redirect = redirect;
        this.absolute = absolute;
    }
    
    public View(String path, boolean redirect, boolean absolute, Object model) {
        this.path = path;
        this.redirect = redirect;
        this.absolute = absolute;
        this.model = model;
    }

    public View setTemplateEngine(TemplateEngine templateEngine){
    	this.templateEngine = templateEngine;
    	return this;
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
    	if(templateEngine == null){
    		throw new RuntimeException("Template Engine is null");
    	}
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

    public boolean isAbsolute() {
        return absolute;
    }
}
