package com.ilyakrn.studentscheduleserver.web;

import com.ilyakrn.studentscheduleserver.data.repositories.UserRepository;
import com.ilyakrn.studentscheduleserver.data.tablemodels.Role;
import com.ilyakrn.studentscheduleserver.data.tablemodels.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("${id}")
    public ResponseEntity<User> get(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!userRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findById(id).get();
        if (!auth.getName().equals(u.getEmail()))
            u.setPassword(null);
        if(!(auth.getAuthorities().contains(Role.ADMIN) || auth.getAuthorities().contains(Role.ULTIMATE)))
            u.setEmail(null);
        return ResponseEntity.ok(u);

    }

    @PostMapping("${id}")
    public ResponseEntity<User> post(@PathVariable("id") long id, @RequestBody User user){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!userRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findById(id).get();
        user.setEmail(u.getEmail());
        user.setId(u.getId());
        if(auth.getName().equals(u.getEmail())) {
            user.setBanned(u.isBanned());
        }
        else {
            user.setPassword(u.getPassword());
            user.setFirstName(u.getFirstName());
            user.setLastName(u.getLastName());
        }
        if(!(auth.getAuthorities().contains(Role.ADMIN) || auth.getAuthorities().contains(Role.ULTIMATE))) {
            user.setBanned(u.isBanned());
            user.setRoles(u.getRoles());
        }
        u = userRepository.findById(id).get();
        if (!auth.getName().equals(u.getEmail()))
            u.setPassword(null);
        if(!(auth.getAuthorities().contains(Role.ADMIN) || auth.getAuthorities().contains(Role.ULTIMATE)))
            u.setEmail(null);
        return ResponseEntity.ok(u);
    }

}
