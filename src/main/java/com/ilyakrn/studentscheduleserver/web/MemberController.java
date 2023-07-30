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

    @PatchMapping("{id}")
    public ResponseEntity<Member> patch(@PathVariable("id") long id, @RequestBody Member member){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!memberRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Member m = memberRepository.findById(id).get();
        User u = userRepository.findByEmail(auth.getName()).get();
        if(member.getRoles() != null)
            m.setRoles(member.getRoles());
        if (member.getRoles().contains(MemberRole.OWNER))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (!member.getRoles().contains(MemberRole.MEMBER))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        for(Member mm : memberRepository.findMemberByGroupId(m.getGroupId()).get()){
            if(u.getId() == mm.getUserId()){
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
        ArrayList<MemberRole> rs = new ArrayList<>();
        rs.add(MemberRole.MEMBER);
        Member m = new Member(0, member.getGroupId(), member.getUserId(), rs);
        for(Member mm : memberRepository.findMemberByGroupId(m.getGroupId()).get()){
            if(u.getId() == mm.getUserId() && mm.getRoles().contains(MemberRole.ADMIN)){
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
                if(mm.getRoles().contains(MemberRole.ADMIN)){
                    memberRepository.delete(m);
                    return ResponseEntity.ok().build();
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
