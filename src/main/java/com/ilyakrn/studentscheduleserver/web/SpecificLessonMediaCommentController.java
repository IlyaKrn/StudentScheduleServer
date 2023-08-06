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
        if(!specificLessonMediaCommentRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLessonMediaComment slmc = specificLessonMediaCommentRepository.findById(id).get();
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(slmc);
        SpecificLessonMedia slm = specificLessonMediaRepository.findById(slmc.getMediaId()).get();
        SpecificLesson sl = specificLessonRepository.findById(slm.getSpecificLessonId()).get();
        Member m = memberRepository.findByGroupIdAndUserId(sl.getGroupId(), u.getId()).get();
        if(m != null){
            return ResponseEntity.ok(slmc);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<SpecificLessonMediaComment> patch(@PathVariable("id") long id, @RequestBody SpecificLessonMediaComment specificLessonMediaComment){
        if (!specificLessonMediaCommentRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (specificLessonMediaComment.getQuestionCommentId() != 0 && !specificLessonMediaCommentRepository.existsById(specificLessonMediaComment.getQuestionCommentId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLessonMediaComment slmc = specificLessonMediaCommentRepository.findById(id).get();
        SpecificLessonMedia slm = specificLessonMediaRepository.findById(slmc.getMediaId()).get();
        SpecificLesson sl = specificLessonRepository.findById(slm.getSpecificLessonId()).get();
        if(specificLessonMediaComment.getText() != null)
            slmc.setText(specificLessonMediaComment.getText());
        if(specificLessonMediaComment.getQuestionCommentId() != 0)
            slmc.setQuestionCommentId(specificLessonMediaComment.getQuestionCommentId());
        Member m = memberRepository.findByGroupIdAndUserId(sl.getGroupId(), u.getId()).get();
        if(m != null){
            if (u.getId() == slmc.getUserId()){
                slmc = specificLessonMediaCommentRepository.save(slmc);
                return ResponseEntity.ok(slmc);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @PostMapping("create")
    public ResponseEntity<SpecificLessonMediaComment> create(@RequestBody SpecificLessonMediaComment specificLessonMediaComment){
        if (specificLessonMediaComment.getMediaId() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (specificLessonMediaComment.getText() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (!specificLessonMediaRepository.existsById(specificLessonMediaComment.getMediaId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (specificLessonMediaComment.getQuestionCommentId() != 0 && !specificLessonMediaCommentRepository.existsById(specificLessonMediaComment.getQuestionCommentId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLessonMediaComment slmc = new SpecificLessonMediaComment(0, specificLessonMediaComment.getText(),u.getId(), specificLessonMediaComment.getMediaId(), specificLessonMediaComment.getQuestionCommentId());
        SpecificLessonMedia slm = specificLessonMediaRepository.findById(slmc.getMediaId()).get();
        SpecificLesson sl = specificLessonRepository.findById(slm.getSpecificLessonId()).get();
        Member m = memberRepository.findByGroupIdAndUserId(sl.getGroupId(), u.getId()).get();
        if(m != null){
            if (u.getId() == slmc.getUserId()){
                slmc = specificLessonMediaCommentRepository.save(slmc);
                return ResponseEntity.ok(slmc);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        if(!specificLessonMediaCommentRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLessonMediaComment slmc = specificLessonMediaCommentRepository.findById(id).get();
        SpecificLessonMedia slm = specificLessonMediaRepository.findById(slmc.getMediaId()).get();
        SpecificLesson sl = specificLessonRepository.findById(slm.getSpecificLessonId()).get();
        Member m = memberRepository.findByGroupIdAndUserId(sl.getGroupId(), u.getId()).get();
        if(m != null){
            if (u.getId() == slmc.getUserId()) {
                specificLessonMediaCommentRepository.delete(slmc);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("list")
    public ResponseEntity<ArrayList<Long>> list(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        ArrayList<SpecificLessonMediaComment> slmcs = (ArrayList<SpecificLessonMediaComment>) specificLessonMediaCommentRepository.findAll();
        slmcs.sort(new Comparator<SpecificLessonMediaComment>() {
            @Override
            public int compare(SpecificLessonMediaComment o1, SpecificLessonMediaComment o2) {
                return -1 * Long.compare(o1.getUserId(), o2.getUserId());
            }
        });
        ArrayList<Long> ids = new ArrayList<>();
        for (SpecificLessonMediaComment slmc : slmcs){
            ids.add(slmc.getId());
        }
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(ids);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
