package com.ilyakrn.studentscheduleserver.web;

import com.ilyakrn.studentscheduleserver.data.repositories.*;
import com.ilyakrn.studentscheduleserver.data.tablemodels.*;
import com.ilyakrn.studentscheduleserver.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Autowired
    private FileService fileService;

    @GetMapping("{id}")
    public ResponseEntity<User> get(@PathVariable("id") long id){
        if(!userRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findById(id).get();
        if (!auth.getName().equals(u.getEmail()))
            u.setPassword(null);
        if(!auth.getAuthorities().contains(Role.ADMIN) && !auth.getName().equals(u.getEmail()))
            u.setEmail(null);
        return ResponseEntity.ok(u);

    }

    @PatchMapping("{id}")
    public ResponseEntity<User> patch(@PathVariable("id") long id, @RequestBody User user, @RequestParam(value = "image", required = false) MultipartFile image){
        if(!userRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findById(id).get();
        if (image != null && auth.getName().equals(u.getEmail())) {
            try {
                String ava = fileService.post(image);
                u.setAva(ava);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        else if (image != null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (user.getPassword() != null && auth.getName().equals(u.getEmail()))
            u.setPassword(user.getPassword());
        else if (user.getPassword() != null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (user.getFirstName() != null && auth.getName().equals(u.getEmail()))
            u.setFirstName(user.getFirstName());
        else if (user.getFirstName() != null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (user.getLastName() != null && auth.getName().equals(u.getEmail()))
            u.setLastName(user.getLastName());
        else if (user.getLastName() != null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (user.getBanned() != null && !auth.getName().equals(u.getEmail()) && auth.getAuthorities().contains(Role.ADMIN))
            u.setBanned(user.getBanned());
        else if (user.getBanned() != null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(user.getRoles() != null && auth.getAuthorities().contains(Role.ULTIMATE)) {
            if(user.getRoles().contains(Role.ULTIMATE))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            if(!user.getRoles().contains(Role.USER))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            u.setRoles(user.getRoles());
            user.getRoles().clear();
            if(u.getRoles().contains(Role.ADMIN))
                user.getRoles().add(Role.ADMIN);
            if(u.getRoles().contains(Role.USER))
                user.getRoles().add(Role.USER);
            u.setRoles(user.getRoles());
        }
        u = userRepository.save(u);
        if (!auth.getName().equals(u.getEmail()))
            u.setPassword(null);
        if(!auth.getAuthorities().contains(Role.ADMIN) && !auth.getName().equals(u.getEmail()))
            u.setEmail(null);
        return ResponseEntity.ok(u);
    }

    @GetMapping("{id}/members")
    public ResponseEntity<ArrayList<Long>> groups(@PathVariable("id") long id){
        if(!userRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findById(id).get();
        ArrayList<Member> ms = (ArrayList<Member>) memberRepository.findMemberByUserId(id).get();
        ArrayList<Long> ids = new ArrayList<>();
        for (Member m : ms){
            ids.add(m.getId());
        }
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(ids);
        if(u.getEmail().equals(auth.getName()))
            return ResponseEntity.ok(ids);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }

    @GetMapping("{id}/specificLessonMediaComments")
    public ResponseEntity<ArrayList<Long>> specificLessonMediaComments(@PathVariable("id") long id){
        if(!userRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findById(id).get();
        ArrayList<SpecificLessonMediaComment> slmcs = (ArrayList<SpecificLessonMediaComment>) specificLessonMediaCommentRepository.findSpecificLessonMediaCommentByUserId(id).get();
        ArrayList<Long> ids = new ArrayList<>();
        for (SpecificLessonMediaComment slmc : slmcs){
            ids.add(slmc.getId());
        }
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(ids);
        if(u.getEmail().equals(auth.getName()))
            return ResponseEntity.ok(ids);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }

}
