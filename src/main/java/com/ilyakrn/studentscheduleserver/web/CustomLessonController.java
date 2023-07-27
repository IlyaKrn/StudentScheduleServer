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
    private SpecificLessonRepository specificLessonRepository;

    @GetMapping("{id}")
    public ResponseEntity<CustomLesson> get(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!customLessonRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        CustomLesson cl = customLessonRepository.findById(id).get();
        for(Member m : memberRepository.findMemberByGroupId(cl.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                return ResponseEntity.ok(cl);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN) || auth.getAuthorities().contains(Role.ULTIMATE))
            return ResponseEntity.ok(cl);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("{id}")
    public ResponseEntity<CustomLesson> post(@PathVariable("id") long id, @RequestBody CustomLesson customLesson){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!customLessonRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        CustomLesson cl = customLessonRepository.findById(id).get();
        if (customLesson.getName() == null)
            customLesson.setName(cl.getName());
        if (customLesson.getTeacher() == null)
            customLesson.setTeacher(cl.getTeacher());
        for(Member m : memberRepository.findMemberByGroupId(cl.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                if(m.getAccessLevel() <= 1){
                    cl = customLessonRepository.save(new CustomLesson(cl.getId(), cl.getGroupId(), customLesson.getName(), customLesson.getTeacher()));
                    return ResponseEntity.ok(cl);
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        for(Member mm : memberRepository.findMemberByGroupId(customLesson.getGroupId()).get()){
            if(u.getId() == mm.getUserId() && mm.getAccessLevel() < 2){
                CustomLesson cl = customLessonRepository.save(new CustomLesson(0, customLesson.getGroupId(), customLesson.getName(), customLesson.getTeacher()));
                return ResponseEntity.ok(cl);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!customLessonRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        CustomLesson cl = customLessonRepository.findById(id).get();
        for(Member mm : memberRepository.findMemberByGroupId(cl.getGroupId()).get()){
            if(u.getId() == mm.getUserId()){
                if(mm.getAccessLevel() <= 2){
                    customLessonRepository.delete(cl);
                    return ResponseEntity.ok().build();
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("{id}/lessonTemplates")
    public ResponseEntity<ArrayList<Long>> lessonTemplates(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!lessonTemplateRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        CustomLesson cl = customLessonRepository.findById(id).get();
        ArrayList<LessonTemplate> lts = (ArrayList<LessonTemplate>) lessonTemplateRepository.findLessonTemplateByGroupId(cl.getGroupId()).get();
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
        for(Member m : memberRepository.findMemberByGroupId(cl.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                return ResponseEntity.ok(ids);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN) || auth.getAuthorities().contains(Role.ULTIMATE))
            return ResponseEntity.ok(ids);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("{id}/specificLessons")
    public ResponseEntity<ArrayList<Long>> specificLessons(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!specificLessonRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        CustomLesson cl = customLessonRepository.findById(id).get();
        ArrayList<SpecificLesson> sls = (ArrayList<SpecificLesson>) specificLessonRepository.findSpecificLessonByGroupId((cl.getGroupId())).get();
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
        for(Member m : memberRepository.findMemberByGroupId(cl.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                return ResponseEntity.ok(ids);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN) || auth.getAuthorities().contains(Role.ULTIMATE))
            return ResponseEntity.ok(ids);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
