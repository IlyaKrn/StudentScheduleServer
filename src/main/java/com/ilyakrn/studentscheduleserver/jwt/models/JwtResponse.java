package com.ilyakrn.studentscheduleserver.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class JwtResponse {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;

}
