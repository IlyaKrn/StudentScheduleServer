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
        if(!memberRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Member m = memberRepository.findById(id).get();
        User u = userRepository.findByEmail(auth.getName()).get();
        for(Member mm : memberRepository.findMemberByGroupId(m.getGroupId()).get()){
            if(u.getId() == mm.getUserId()){
                return ResponseEntity.ok(m);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(m);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("{id}")
    public ResponseEntity<Member> post(@PathVariable("id") long id, @RequestBody Member member){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!memberRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Member m = memberRepository.findById(id).get();
        User u = userRepository.findByEmail(auth.getName()).get();
        if (member.getAccessLevel() != 0)
            m.setAccessLevel(member.getAccessLevel());
        for(Member mm : memberRepository.findMemberByGroupId(m.getGroupId()).get()){
            if(u.getId() == mm.getUserId()){
                if(m.getAccessLevel() > mm.getAccessLevel()){
                    m = memberRepository.save(m);
                    return ResponseEntity.ok(m);
                }
                if(m.getAccessLevel() == 0 && mm.getAccessLevel() == 0){
                    m = memberRepository.save(m);
                    mm.setAccessLevel(1);
                    memberRepository.save(mm);
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
        if (!groupRepository.existsById(member.getGroupId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (!userRepository.existsById(member.getUserId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        Member m = new Member(0, member.getGroupId(), member.getUserId(), 2);
        for(Member mm : memberRepository.findMemberByGroupId(m.getGroupId()).get()){
            if(u.getId() == mm.getUserId() && mm.getAccessLevel() <= 1){
                m = memberRepository.save(m);
                return ResponseEntity.ok(m);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
