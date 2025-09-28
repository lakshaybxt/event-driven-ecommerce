package com.microservice.order_service.exception;

public class OrderCancellationNotAllowedException extends BaseException {
    public OrderCancellationNotAllowedException() {
    }

    public OrderCancellationNotAllowedException(String message) {
        super(message);
    }

    public OrderCancellationNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderCancellationNotAllowedException(Throwable cause) {
        super(cause);
    }
}
