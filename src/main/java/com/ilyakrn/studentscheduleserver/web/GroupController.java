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
import java.util.Comparator;

@RestController
@RequestMapping("api/groups")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomLessonRepository customLessonRepository;
    @Autowired
    private ScheduleTemplateRepository scheduleTemplateRepository;
    @Autowired
    private SpecificLessonRepository specificLessonRepository;

    @GetMapping("{id}")
    public ResponseEntity<Group> get(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!groupRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        Group g = groupRepository.findById(id).get();
        for(Member m : memberRepository.findMemberByGroupId(g.getId()).get()){
            if(u.getId() == m.getUserId()){
                return ResponseEntity.ok(g);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(g);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<Group> patch(@PathVariable("id") long id, @RequestBody Group group){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!groupRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        Group g = groupRepository.findById(id).get();
        if (group.getName() != null)
            g.setName(group.getName());
        for(Member m : memberRepository.findMemberByGroupId(g.getId()).get()){
            if(u.getId() == m.getUserId()){
                if(m.getRoles().contains(MemberRole.ADMIN)){
                    g = groupRepository.save(g);
                    return ResponseEntity.ok(g);
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }

    @PostMapping("create")
    public ResponseEntity<Group> create(@RequestBody Group group){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (group.getName() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        Group g = groupRepository.save(new Group(0, 0, group.getName()));
        ArrayList<MemberRole> roles = new ArrayList<>();
        roles.add(MemberRole.MEMBER);
        roles.add(MemberRole.ADMIN);
        roles.add(MemberRole.OWNER);
        memberRepository.save(new Member(0, g.getId(), u.getId(), roles));
        return ResponseEntity.ok(g);
    }


    @GetMapping("{id}/members")
    public ResponseEntity<ArrayList<Long>> members(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!groupRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        ArrayList<Member> ms = (ArrayList<Member>) memberRepository.findMemberByGroupId(id).get();
        ms.sort(new Comparator<Member>() {
            @Override
            public int compare(Member o1, Member o2) {
                return Integer.compare(o1.getRoles().size(), o2.getRoles().size());
            }
        });
        ArrayList<Long> ids = new ArrayList<>();
        for (Member m : ms){
            ids.add(m.getId());
        }
        for(Member m : memberRepository.findMemberByGroupId(id).get()){
            if(u.getId() == m.getUserId()){
                return ResponseEntity.ok(ids);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(ids);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @GetMapping("{id}/customLessons")
    public ResponseEntity<ArrayList<Long>> customLessons(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!groupRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        ArrayList<CustomLesson> cls = (ArrayList<CustomLesson>) customLessonRepository.findCustomLessonByGroupId(id).get();
        ArrayList<Long> ids = new ArrayList<>();
        for (CustomLesson cl : cls){
            ids.add(cl.getId());
        }
        for(Member m : memberRepository.findMemberByGroupId(id).get()){
            if(u.getId() == m.getUserId()){
                return ResponseEntity.ok(ids);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(ids);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }


    @GetMapping("{id}/scheduleTemplates")
    public ResponseEntity<ArrayList<Long>> scheduleTemplates(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!groupRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        ArrayList<ScheduleTemplate> sts = (ArrayList<ScheduleTemplate>) scheduleTemplateRepository.findScheduleTemplateByGroupId(id).get();
        ArrayList<Long> ids = new ArrayList<>();
        for (ScheduleTemplate st : sts){
            ids.add(st.getId());
        }
        for(Member m : memberRepository.findMemberByGroupId(id).get()){
            if(u.getId() == m.getUserId()){
                return ResponseEntity.ok(ids);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(ids);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }

    @GetMapping("{id}/specificLessons")
    public ResponseEntity<ArrayList<Long>> specificLessons(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!groupRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        ArrayList<SpecificLesson> sls = (ArrayList<SpecificLesson>) specificLessonRepository.findSpecificLessonByGroupId(id).get();
        ArrayList<Long> ids = new ArrayList<>();
        for (SpecificLesson sl : sls){
            ids.add(sl.getId());
        }
        for(Member m : memberRepository.findMemberByGroupId(id).get()){
            if(u.getId() == m.getUserId()){
                return ResponseEntity.ok(ids);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(ids);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }







}
