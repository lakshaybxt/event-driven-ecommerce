package com.microservice.order_service.exception;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException(String message) {
        super(message);
    }

    public EmptyCartException() {
    }

    public EmptyCartException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyCartException(Throwable cause) {
        super(cause);
    }
}