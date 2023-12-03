package org.oem.pinggo.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {

    private String token;

    private String type = "Bearer";

    private Long id;

    private String username;

    private String email;

    public UserInfoResponse(String accessToken, Long id, String username, String email) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
