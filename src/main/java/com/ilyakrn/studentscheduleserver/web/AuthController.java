package com.ilyakrn.studentscheduleserver.web;

import com.ilyakrn.studentscheduleserver.data.repositories.UserRepository;
import com.ilyakrn.studentscheduleserver.data.tablemodels.Role;
import com.ilyakrn.studentscheduleserver.data.tablemodels.User;
import com.ilyakrn.studentscheduleserver.jwt.models.JwtLoginRequest;
import com.ilyakrn.studentscheduleserver.jwt.models.JwtRegisterRequest;
import com.ilyakrn.studentscheduleserver.jwt.models.JwtResponse;
import com.ilyakrn.studentscheduleserver.jwt.models.RefreshJwtRequest;
import com.ilyakrn.studentscheduleserver.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.message.AuthException;
import java.util.ArrayList;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtLoginRequest authRequest) throws AuthException {
        if(authRequest.getEmail() == null || authRequest.getEmail().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(authRequest.getPassword() == null || authRequest.getPassword().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        try {
            final JwtResponse token = authService.login(authRequest);
            return ResponseEntity.ok(token);
        } catch (AuthException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        if(request.getRefreshToken() == null || request.getRefreshToken().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        try {
            final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
            return ResponseEntity.ok(token);
        } catch (AuthException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        if(request.getRefreshToken() == null || request.getRefreshToken().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        try {
            final JwtResponse token = authService.refresh(request.getRefreshToken());
            return ResponseEntity.ok(token);
        } catch (AuthException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("register")
    public ResponseEntity<JwtResponse> register(@RequestBody JwtRegisterRequest authRequest) throws AuthException {
        if(authRequest.getEmail() == null || authRequest.getEmail().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(authRequest.getPassword() == null || authRequest.getPassword().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(authRequest.getFirstName() == null || authRequest.getFirstName().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(authRequest.getLastName() == null || authRequest.getLastName().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(!userRepository.existsByEmail(authRequest.getEmail())){
            ArrayList<Role> roles = new ArrayList<>();
            roles.add(Role.USER);
            userRepository.save(new User(0, authRequest.getEmail(), authRequest.getPassword(), authRequest.getFirstName(), authRequest.getLastName(), false, roles));
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        try {
            final JwtResponse token = authService.login(new JwtLoginRequest(authRequest.getEmail(), authRequest.getPassword()));
            return ResponseEntity.ok(token);
        } catch (AuthException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}