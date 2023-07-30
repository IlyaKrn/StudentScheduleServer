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
@RequestMapping("api/specificLessons")
public class SpecificLessonController {


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

    @GetMapping("{id}")
    public ResponseEntity<SpecificLesson> get(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!specificLessonRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLesson sl = specificLessonRepository.findById(id).get();
        for(Member m : memberRepository.findMemberByGroupId(sl.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                return ResponseEntity.ok(sl);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(sl);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<SpecificLesson> patch(@PathVariable("id") long id, @RequestBody SpecificLesson specificLesson) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!specificLessonRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLesson sl = specificLessonRepository.findById(id).get();
        if(specificLesson.getCanceled() != null)
            sl.setCanceled(specificLesson.getCanceled());
        for(Member m : memberRepository.findMemberByGroupId(sl.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                if(m.getRoles().contains(MemberRole.ADMIN)){
                    sl = specificLessonRepository.save(sl);
                    return ResponseEntity.ok(sl);
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("{id}/specificLessonMedias")
    public ResponseEntity<ArrayList<Long>> specificLessonMedias(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!specificLessonRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLesson sl = specificLessonRepository.findById(id).get();
        ArrayList<SpecificLessonMedia> slms = (ArrayList<SpecificLessonMedia>) specificLessonMediaRepository.findSpecificLessonMediaBySpecificLessonId(sl.getId()).get();
        ArrayList<Long> ids = new ArrayList<>();
        for (SpecificLessonMedia slm : slms){
            ids.add(slm.getId());
        }
        for(Member m : memberRepository.findMemberByGroupId(sl.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                return ResponseEntity.ok(ids);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(ids);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
