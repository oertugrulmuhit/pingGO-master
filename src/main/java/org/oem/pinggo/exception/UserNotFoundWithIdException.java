package org.oem.pinggo.exception;

public class UserNotFoundWithIdException extends RuntimeException {

    public UserNotFoundWithIdException(String message) {
        super(message);
    }
}