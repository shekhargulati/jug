package org.jug;

import java.io.Serializable;

/**
 * Created by shekhargulati on 21/03/14.
 */
public class ViewResourceNotFoundException  extends RuntimeException implements Serializable {

    public ViewResourceNotFoundException() {
    }

    public ViewResourceNotFoundException(String message) {
        super(message);
    }

    public ViewResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
