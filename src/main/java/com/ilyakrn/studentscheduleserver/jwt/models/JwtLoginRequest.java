package com.ilyakrn.studentscheduleserver.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class JwtLoginRequest {

    private String email;
    private String password;

}