package com.ilyakrn.studentscheduleserver.web;

import com.ilyakrn.studentscheduleserver.data.repositories.*;
import com.ilyakrn.studentscheduleserver.data.tablemodels.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;

@RestController
@RequestMapping("api/members")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;
    private UserRepository userRepository;
    private GroupRepository groupRepository;

    @GetMapping("{id}")
    public ResponseEntity<Member> get(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!memberRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Member m = memberRepository.findById(id).get();
        User u = userRepository.findByEmail(auth.getName()).get();
        for(Member mm : memberRepository.findMemberByGroupId(m.getGroupId()).get()){
            if(u.getId() == mm.getUserId()){
                return ResponseEntity.ok(m);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN) || auth.getAuthorities().contains(Role.ULTIMATE))
            return ResponseEntity.ok(m);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("{id}")
    public ResponseEntity<Member> post(@PathVariable("id") long id, @RequestBody Member member){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!memberRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Member m = memberRepository.findById(id).get();
        User u = userRepository.findByEmail(auth.getName()).get();
        for(Member mm : memberRepository.findMemberByGroupId(m.getGroupId()).get()){
            if(u.getId() == mm.getUserId() && mm.getAccessLevel() < m.getAccessLevel()){
                if(member.getAccessLevel() > mm.getAccessLevel()){
                    m = memberRepository.save(new Member(m.getId(), m.getGroupId(), m.getUserId(), member.getAccessLevel()));
                    return ResponseEntity.ok(m);
                }
                if(member.getAccessLevel() == 0 && mm.getAccessLevel() == 0){
                    m = memberRepository.save(new Member(m.getId(), m.getGroupId(), m.getUserId(),0));
                    memberRepository.save(new Member(mm.getId(), mm.getGroupId(), mm.getUserId(), 1));
                    return ResponseEntity.ok(m);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }
    @PostMapping("create")
    public ResponseEntity<Member> create(@RequestBody Member member){
        if (member.getUserId() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (member.getGroupId() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        Member m = new Member(0, member.getGroupId(), member.getUserId(), 3);
        for(Member mm : memberRepository.findMemberByGroupId(m.getGroupId()).get()){
            if(u.getId() == mm.getUserId() && mm.getAccessLevel() < 2){
                m = memberRepository.save(m);
                return ResponseEntity.ok(m);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!memberRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        Member m = memberRepository.findById(id).get();
        for(Member mm : memberRepository.findMemberByGroupId(m.getGroupId()).get()){
            if(u.getId() == mm.getUserId()){
                if(mm.getAccessLevel() < m.getAccessLevel()){
                    memberRepository.delete(m);
                    return ResponseEntity.ok().build();
                }
                if(mm.getUserId() == m.getUserId()){
                    if (m.getAccessLevel() != 0) {
                        memberRepository.delete(m);
                        return ResponseEntity.ok().build();
                    }
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
