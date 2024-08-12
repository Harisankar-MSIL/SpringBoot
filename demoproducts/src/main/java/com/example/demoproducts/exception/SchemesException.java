package com.example.demoproducts.exception;

public class SchemesException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SchemesException(String message) {
        super(message);
    }

    public SchemesException(String message, Throwable cause) {
        super(message, cause);
    }
}
