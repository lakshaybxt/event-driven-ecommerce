package com.microservice.product_service.exception;

public class BusinessRuleException extends BaseException {
    public BusinessRuleException() {
    }

    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessRuleException(Throwable cause) {
        super(cause);
    }
}
