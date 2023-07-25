package com.ilyakrn.studentscheduleserver.data.tablemodels;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Table;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    USER, ADMIN, ULTIMATE;

    @Override
    public String getAuthority() {
        return name();
    }


    public static Role getFromString(String s){
        switch (s){
            case "USER":
                return Role.USER;
            case "ADMIN":
                return Role.ADMIN;
            case "ULTIMATE":
                return Role.ULTIMATE;
            default:
                throw new ClassCastException("String " + s + " can not be cast to Role");
        }
    }
}