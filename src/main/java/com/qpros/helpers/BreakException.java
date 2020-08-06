package com.qpros.helpers;

public class BreakException extends RuntimeException{
    private static final String DEFAULT_MESSAGE  = "Operation done Successfully";

    public BreakException() {
        super(DEFAULT_MESSAGE);
    }

    public BreakException(String message) {
        super(message);
    }
}
