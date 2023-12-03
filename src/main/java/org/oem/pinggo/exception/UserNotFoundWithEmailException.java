package org.oem.pinggo.exception;

public class UserNotFoundWithEmailException extends RuntimeException {

    public UserNotFoundWithEmailException(String message) {
        super(message);
    }
}