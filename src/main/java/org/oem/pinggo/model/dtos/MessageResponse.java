package org.oem.pinggo.model.dtos;

import lombok.Getter;

@Getter
public class MessageResponse {

    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}