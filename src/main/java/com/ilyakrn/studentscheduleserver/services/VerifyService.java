package com.ilyakrn.studentscheduleserver.services;

import com.ilyakrn.studentscheduleserver.web.models.VerifyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VerifyService {


    private Map<String, Long> emailCodes = new HashMap<>();
    @Autowired
    private MailService mailService;



    public void sendCode(String email){
        long code = Math.round(Math.random() * 100000);
        mailService.sendSimpleMessage(email, "Verify email", String.valueOf(code));
        emailCodes.put(email, code);
    }

    public boolean verify(VerifyRequest verifyRequest){
        if(emailCodes.get(verifyRequest.getEmail()).equals(verifyRequest.getCode())){
            emailCodes.remove(verifyRequest.getEmail());
            return true;
        }
        return true; // temporally
    }

}
