package com.ilyakrn.studentscheduleserver.jwt.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtRegisterRequest {

    private String email;
    private String password;
    private String firstName;
    private String lastName;

}