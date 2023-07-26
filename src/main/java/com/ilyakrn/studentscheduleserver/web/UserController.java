package com.ilyakrn.studentscheduleserver.web;

import com.ilyakrn.studentscheduleserver.data.repositories.*;
import com.ilyakrn.studentscheduleserver.data.tablemodels.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SpecificLessonMediaCommentRepository specificLessonMediaCommentRepository;

    @GetMapping("{id}")
    public ResponseEntity<User> get(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!userRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findById(id).get();
        if (!auth.getName().equals(u.getEmail()))
            u.setPassword(null);
        if(!(auth.getAuthorities().contains(Role.ADMIN) || auth.getAuthorities().contains(Role.ULTIMATE)) && !auth.getName().equals(u.getEmail()))
            u.setEmail(null);
        return ResponseEntity.ok(u);

    }

    @PostMapping("{id}")
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
        if(!(auth.getAuthorities().contains(Role.ADMIN) || auth.getAuthorities().contains(Role.ULTIMATE)) && !auth.getName().equals(u.getEmail()))
            u.setEmail(null);
        return ResponseEntity.ok(u);
    }

    @GetMapping("{id}/members")
    public ResponseEntity<ArrayList<Long>> groups(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        ArrayList<Member> ms = (ArrayList<Member>) memberRepository.findMemberByUserId(id).get();
        ArrayList<Long> ids = new ArrayList<>();
        for (Member m : ms){
            ids.add(m.getId());
        }
        if(u.getId() == id)
            return ResponseEntity.ok(ids);
        if(auth.getAuthorities().contains(Role.ADMIN) || auth.getAuthorities().contains(Role.ULTIMATE))
            return ResponseEntity.ok(ids);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }

    @GetMapping("{id}/specificLessonMediaComments")
    public ResponseEntity<ArrayList<Long>> specificLessonMediaComments(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        ArrayList<SpecificLessonMediaComment> slmcs = (ArrayList<SpecificLessonMediaComment>) specificLessonMediaCommentRepository.findSpecificLessonMediaCommentByUserId(id).get();
        ArrayList<Long> ids = new ArrayList<>();
        for (SpecificLessonMediaComment slmc : slmcs){
            ids.add(slmc.getId());
        }
        if(u.getId() == id)
            return ResponseEntity.ok(ids);
        if(auth.getAuthorities().contains(Role.ADMIN) || auth.getAuthorities().contains(Role.ULTIMATE))
            return ResponseEntity.ok(ids);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }

}
