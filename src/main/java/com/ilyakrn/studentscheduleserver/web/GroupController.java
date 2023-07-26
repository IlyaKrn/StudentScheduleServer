package com.ilyakrn.studentscheduleserver.web;

import com.ilyakrn.studentscheduleserver.data.repositories.GroupRepository;
import com.ilyakrn.studentscheduleserver.data.repositories.MemberRepository;
import com.ilyakrn.studentscheduleserver.data.repositories.UserRepository;
import com.ilyakrn.studentscheduleserver.data.tablemodels.Group;
import com.ilyakrn.studentscheduleserver.data.tablemodels.Member;
import com.ilyakrn.studentscheduleserver.data.tablemodels.Role;
import com.ilyakrn.studentscheduleserver.data.tablemodels.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/groups")
public class GroupController {

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("{id}")
    public ResponseEntity<Group> get(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!groupRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        Group g = groupRepository.findById(id).get();
        for(Member m : memberRepository.findMemberByGroupId(g.getId()).get()){
            if(u.getId() == m.getUserId()){
                return ResponseEntity.ok(g);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("{id}")
    public ResponseEntity<Group> post(@PathVariable("id") long id, @RequestBody Group group){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!groupRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        Group g = groupRepository.findById(id).get();
        for(Member m : memberRepository.findMemberByGroupId(g.getId()).get()){
            if(u.getId() == m.getUserId()){
                if(m.getAccessLevel() <= 1){
                    g = groupRepository.save(new Group(g.getId(), g.getChatId(), group.getName()));
                    return ResponseEntity.ok(g);
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }

    @PostMapping("create")
    public ResponseEntity<Group> post(@RequestBody Group group){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        Group g = groupRepository.save(new Group(0, 0, group.getName()));
        memberRepository.save(new Member(0, g.getId(), u.getId(), 0));
        return ResponseEntity.ok(g);
    }



}
