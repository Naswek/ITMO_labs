package org.example.exceptions;

import java.io.Serial;

public class EmptyRequestException extends Throwable {

    @Serial
    private static final long serialVersionUID = 1L;

    public EmptyRequestException (String s) {
        super (s);
    }

    public EmptyRequestException () {
        super ();
    }
}
