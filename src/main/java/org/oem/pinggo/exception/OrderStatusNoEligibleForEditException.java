package org.oem.pinggo.exception;

public class OrderStatusNoEligibleForEditException extends RuntimeException {

    public OrderStatusNoEligibleForEditException(String message) {
        super(message);
    }
}