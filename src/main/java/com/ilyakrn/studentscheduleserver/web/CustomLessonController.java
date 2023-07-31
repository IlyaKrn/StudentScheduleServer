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
@RequestMapping("api/customLessons")
public class CustomLessonController {


    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomLessonRepository customLessonRepository;
    @Autowired
    private LessonTemplateRepository lessonTemplateRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private SpecificLessonRepository specificLessonRepository;

    @GetMapping("{id}")
    public ResponseEntity<CustomLesson> get(@PathVariable("id") long id){
        if(!customLessonRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        CustomLesson cl = customLessonRepository.findById(id).get();
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(cl);
        Member m = memberRepository.findByGroupIdAndUserId(cl.getGroupId(), u.getId()).get();
        if(m != null){
            return ResponseEntity.ok(cl);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<CustomLesson> patch(@PathVariable("id") long id, @RequestBody CustomLesson customLesson){
        if(!customLessonRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        CustomLesson cl = customLessonRepository.findById(id).get();
        if (customLesson.getName() != null)
            cl.setName(customLesson.getName());
        if (customLesson.getTeacher() != null)
            cl.setTeacher(customLesson.getTeacher());
        Member m = memberRepository.findByGroupIdAndUserId(cl.getGroupId(), u.getId()).get();
        if(m != null){
            if(m.getRoles().contains(MemberRole.ADMIN)){
                cl = customLessonRepository.save(cl);
                return ResponseEntity.ok(cl);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }
    @PostMapping("create")
    public ResponseEntity<CustomLesson> create(@RequestBody CustomLesson customLesson){
        if (customLesson.getName() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (customLesson.getGroupId() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (customLesson.getTeacher() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (!groupRepository.existsById(customLesson.getGroupId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        Member m = memberRepository.findByGroupIdAndUserId(customLesson.getGroupId(), u.getId()).get();
        if(m != null){
            if (m.getRoles().contains(MemberRole.ADMIN)){
                CustomLesson cl = customLessonRepository.save(new CustomLesson(0, customLesson.getGroupId(), customLesson.getName(), customLesson.getTeacher()));
                return ResponseEntity.ok(cl);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("{id}/lessonTemplates")
    public ResponseEntity<ArrayList<Long>> lessonTemplates(@PathVariable("id") long id){
        if(!lessonTemplateRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        CustomLesson cl = customLessonRepository.findById(id).get();
        ArrayList<LessonTemplate> lts = (ArrayList<LessonTemplate>) lessonTemplateRepository.findLessonTemplateByLessonId(cl.getId()).get();
        lts.sort(new Comparator<LessonTemplate>() {
            @Override
            public int compare(LessonTemplate o1, LessonTemplate o2) {
                return -1 * Long.compare(o1.getTime(), o2.getTime());
            }
        });
        ArrayList<Long> ids = new ArrayList<>();
        for (LessonTemplate lt : lts){
            ids.add(lt.getId());
        }
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(ids);
        Member m = memberRepository.findByGroupIdAndUserId(cl.getGroupId(), u.getId()).get();
        if(m != null){
            return ResponseEntity.ok(ids);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("{id}/specificLessons")
    public ResponseEntity<ArrayList<Long>> specificLessons(@PathVariable("id") long id){
        if(!specificLessonRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        CustomLesson cl = customLessonRepository.findById(id).get();
        ArrayList<SpecificLesson> sls = (ArrayList<SpecificLesson>) specificLessonRepository.findSpecificLessonByLessonId((cl.getId())).get();
        sls.sort(new Comparator<SpecificLesson>() {
            @Override
            public int compare(SpecificLesson o1, SpecificLesson o2) {
                return -1 * Long.compare(o1.getTime(), o2.getTime());
            }
        });
        ArrayList<Long> ids = new ArrayList<>();
        for (SpecificLesson sl : sls){
            ids.add(sl.getId());
        }
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(ids);
        Member m = memberRepository.findByGroupIdAndUserId(cl.getGroupId(), u.getId()).get();
        if(m != null){
            return ResponseEntity.ok(ids);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
