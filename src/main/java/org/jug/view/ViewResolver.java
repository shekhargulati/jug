/**
 * (c) 2010 Copyright by Vivian Steller <vivian@steller.info>. All rights reserved. 
 */
package org.jug.view;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class ViewResolver {
    public boolean isResolvable(Object object) {
        return isResolvable(object.getClass(), object.getClass().getGenericSuperclass(), null);
    }

    public boolean isResolvable(Class<?> type, Type genericType, Annotation[] methodAnnotations) {
        if (Viewable.class.isAssignableFrom(type))
            return true;
        else
            return false;
    }

    public Viewable getView(Object object) {
        if (object == null)
            return null;

        return getView(object, object.getClass(), object.getClass().getGenericSuperclass(), null);
    }

    public Viewable getView(Object object, Class<?> type, Type genericType, Annotation[] annotations) {
        if (object instanceof Viewable) {
            return (Viewable) object;
        }
        return null;
    }


    protected Viewable createView(String path, Object model, String modelName) {
        return new View(path, model, modelName);
    }
}
