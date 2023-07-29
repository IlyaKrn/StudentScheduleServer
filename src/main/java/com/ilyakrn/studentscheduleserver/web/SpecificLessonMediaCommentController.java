package com.ilyakrn.studentscheduleserver.web;

import com.ilyakrn.studentscheduleserver.data.repositories.*;
import com.ilyakrn.studentscheduleserver.data.tablemodels.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/specificLessonMediaComments")
public class SpecificLessonMediaCommentController {


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
    public ResponseEntity<SpecificLessonMediaComment> get(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!specificLessonMediaCommentRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLessonMediaComment slmc = specificLessonMediaCommentRepository.findById(id).get();
        SpecificLessonMedia slm = specificLessonMediaRepository.findById(slmc.getMediaId()).get();
        SpecificLesson sl = specificLessonRepository.findById(slm.getSpecificLessonId()).get();
        for(Member m : memberRepository.findMemberByGroupId(sl.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                return ResponseEntity.ok(slmc);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN) || auth.getAuthorities().contains(Role.ULTIMATE))
            return ResponseEntity.ok(slmc);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("{id}")
    public ResponseEntity<SpecificLessonMediaComment> post(@PathVariable("id") long id, @RequestBody SpecificLessonMediaComment specificLessonMediaComment){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (!userRepository.existsById(specificLessonMediaComment.getUserId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (!specificLessonMediaRepository.existsById(specificLessonMediaComment.getMediaId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (specificLessonMediaComment.getQuestionCommentId() != 0 && !specificLessonMediaCommentRepository.existsById(specificLessonMediaComment.getQuestionCommentId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLessonMediaComment slmc = specificLessonMediaCommentRepository.findById(id).get();
        SpecificLessonMedia slm = specificLessonMediaRepository.findById(slmc.getMediaId()).get();
        SpecificLesson sl = specificLessonRepository.findById(slm.getSpecificLessonId()).get();
        if(specificLessonMediaComment.getText() != null)
            slmc.setText(specificLessonMediaComment.getText());
        if(specificLessonMediaComment.getQuestionCommentId() != 0)
            slmc.setQuestionCommentId(specificLessonMediaComment.getQuestionCommentId());
        for(Member mm : memberRepository.findMemberByGroupId(sl.getGroupId()).get()){
            if(u.getId() == mm.getUserId() && mm.getAccessLevel() < 2){
                slmc = specificLessonMediaCommentRepository.save(slmc);
                return ResponseEntity.ok(slmc);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @PostMapping("create")
    public ResponseEntity<SpecificLessonMediaComment> create(@RequestBody SpecificLessonMediaComment specificLessonMediaComment){
        if (specificLessonMediaComment.getUserId() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (specificLessonMediaComment.getMediaId() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (specificLessonMediaComment.getText() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (!userRepository.existsById(specificLessonMediaComment.getUserId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (!specificLessonMediaRepository.existsById(specificLessonMediaComment.getMediaId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (specificLessonMediaComment.getQuestionCommentId() != 0 && !specificLessonMediaCommentRepository.existsById(specificLessonMediaComment.getQuestionCommentId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLessonMediaComment slmc = new SpecificLessonMediaComment(0, specificLessonMediaComment.getText(), specificLessonMediaComment.getUserId(), specificLessonMediaComment.getMediaId(), specificLessonMediaComment.getQuestionCommentId());
        SpecificLessonMedia slm = specificLessonMediaRepository.findById(slmc.getMediaId()).get();
        SpecificLesson sl = specificLessonRepository.findById(slm.getSpecificLessonId()).get();
        for(Member mm : memberRepository.findMemberByGroupId(sl.getGroupId()).get()){
            if(u.getId() == mm.getUserId() && mm.getAccessLevel() < 2){
                slmc = specificLessonMediaCommentRepository.save(slmc);
                return ResponseEntity.ok(slmc);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!specificLessonMediaCommentRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLessonMediaComment slmc = specificLessonMediaCommentRepository.findById(id).get();
        if(slmc.getUserId() == u.getId()){
            specificLessonMediaCommentRepository.delete(slmc);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
