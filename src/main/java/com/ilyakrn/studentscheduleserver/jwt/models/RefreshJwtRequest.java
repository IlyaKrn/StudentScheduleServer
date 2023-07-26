package com.ilyakrn.studentscheduleserver.jwt.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshJwtRequest {

    public String refreshToken;

}