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
import java.util.logging.Logger;

@RestController
@RequestMapping("api/members")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;

    @GetMapping("{id}")
    public ResponseEntity<Member> get(@PathVariable("id") long id){
        if(!memberRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        Member m = memberRepository.findById(id).get();
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(m);
        Member mm = memberRepository.findByGroupIdAndUserId(m.getGroupId(), u.getId()).get();
        if(mm != null){
            return ResponseEntity.ok(m);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<Member> patch(@PathVariable("id") long id, @RequestBody Member member){
        if(!memberRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        Member m = memberRepository.findById(id).get();
        if(member.getRoles() != null){
            if (m.getRoles().contains(MemberRole.OWNER))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            if (!m.getRoles().contains(MemberRole.MEMBER))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            m.setRoles(member.getRoles());
            member.getRoles().clear();
            if(m.getRoles().contains(MemberRole.OWNER))
                member.getRoles().add(MemberRole.OWNER);
            if(m.getRoles().contains(MemberRole.ADMIN))
                member.getRoles().add(MemberRole.ADMIN);
            if(m.getRoles().contains(MemberRole.MEMBER))
                member.getRoles().add(MemberRole.MEMBER);
            m.setRoles(member.getRoles());
        }
        Member mm = memberRepository.findByGroupIdAndUserId(m.getGroupId(), u.getId()).get();
        if(mm != null){
            if(m.getRoles().contains(MemberRole.OWNER) && mm.getRoles().contains(MemberRole.OWNER)){
                m = memberRepository.save(m);
                ArrayList<MemberRole> rs = (ArrayList<MemberRole>) mm.getRoles();
                rs.remove(MemberRole.OWNER);
                mm.setRoles(rs);
                memberRepository.save(mm);
                return ResponseEntity.ok(m);
            }
            if(mm.getRoles().contains(MemberRole.OWNER)){
                m = memberRepository.save(m);
                return ResponseEntity.ok(m);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }
    @PostMapping("create")
    public ResponseEntity<Member> create(@RequestBody Member member){
        if (member.getUserId() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (member.getGroupId() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (!groupRepository.existsById(member.getGroupId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (!userRepository.existsById(member.getUserId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        ArrayList<MemberRole> rs = new ArrayList<>();
        rs.add(MemberRole.MEMBER);
        Member m = new Member(0, member.getGroupId(), member.getUserId(), rs);
        Member mm = memberRepository.findByGroupIdAndUserId(m.getGroupId(), u.getId()).get();
        if(mm != null){
            if (mm.getRoles().contains(MemberRole.ADMIN)){
                m = memberRepository.save(m);
                return ResponseEntity.ok(m);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        if(!memberRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        Member m = memberRepository.findById(id).get();
        Member mm = memberRepository.findByGroupIdAndUserId(m.getGroupId(), u.getId()).get();
        if(mm != null){
            if(mm.getRoles().contains(MemberRole.ADMIN) || u.getId() == m.getUserId()){
                memberRepository.delete(m);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
