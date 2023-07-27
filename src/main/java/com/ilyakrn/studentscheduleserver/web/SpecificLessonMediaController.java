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
@RequestMapping("api/specificLessonMedias")
public class SpecificLessonMediaController {



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
    @Autowired
    private SpecificLessonMediaRepository specificLessonMediaRepository;
    @Autowired
    private SpecificLessonMediaCommentRepository specificLessonMediaCommentRepository;


    @GetMapping("{id}")
    public ResponseEntity<SpecificLessonMedia> get(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!specificLessonMediaRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLessonMedia slm = specificLessonMediaRepository.findById(id).get();
        SpecificLesson sl = specificLessonRepository.findById(slm.getSpecificLessonId()).get();
        for(Member m : memberRepository.findMemberByGroupId(sl.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                return ResponseEntity.ok(slm);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN) || auth.getAuthorities().contains(Role.ULTIMATE))
            return ResponseEntity.ok(slm);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @PostMapping("create")
    public ResponseEntity<SpecificLessonMedia> create(@RequestBody SpecificLessonMedia specificLessonMedia){
        if (specificLessonMedia.getSpecificLessonId() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (specificLessonMedia.getUrl() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (!specificLessonRepository.existsById(specificLessonMedia.getSpecificLessonId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLessonMedia slm = new SpecificLessonMedia(0, u.getId(), specificLessonMedia.getSpecificLessonId(), specificLessonMedia.getUrl());
        SpecificLesson sl = specificLessonRepository.findById(slm.getSpecificLessonId()).get();
        for(Member mm : memberRepository.findMemberByGroupId(sl.getGroupId()).get()){
            if(u.getId() == mm.getUserId()){
                slm = specificLessonMediaRepository.save(slm);
                return ResponseEntity.ok(slm);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLessonMedia slm = specificLessonMediaRepository.findById(id).get();
        if(slm.getUserId() == u.getId()){
            specificLessonMediaRepository.delete(slm);
            specificLessonMediaCommentRepository.deleteByMediaId(slm.getId());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("{id}/specificLessonMediaComments")
    public ResponseEntity<ArrayList<Long>> specificLessonMediaComments(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!specificLessonMediaRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLessonMedia slm = specificLessonMediaRepository.findById(id).get();
        SpecificLesson sl = specificLessonRepository.findById(slm.getSpecificLessonId()).get();
        ArrayList<SpecificLessonMediaComment> slmcs = (ArrayList<SpecificLessonMediaComment>) specificLessonMediaCommentRepository.findSpecificLessonMediaCommentByMediaId(slm.getId()).get();
        ArrayList<Long> ids = new ArrayList<>();
        for (SpecificLessonMediaComment slmc : slmcs){
            ids.add(slmc.getId());
        }
        for(Member m : memberRepository.findMemberByGroupId(sl.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                return ResponseEntity.ok(ids);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN) || auth.getAuthorities().contains(Role.ULTIMATE))
            return ResponseEntity.ok(ids);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
