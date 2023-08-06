package com.ilyakrn.studentscheduleserver.web;

import com.ilyakrn.studentscheduleserver.data.repositories.*;
import com.ilyakrn.studentscheduleserver.data.tablemodels.*;
import com.ilyakrn.studentscheduleserver.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

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
    @Autowired
    private FileService fileService;


    @GetMapping("{id}")
    public ResponseEntity<SpecificLessonMedia> get(@PathVariable("id") long id){
        if(!specificLessonMediaRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLessonMedia slm = specificLessonMediaRepository.findById(id).get();
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(slm);
        SpecificLesson sl = specificLessonRepository.findById(slm.getSpecificLessonId()).get();
        Member m = memberRepository.findByGroupIdAndUserId(sl.getGroupId(), u.getId()).get();
        if(m != null){
            return ResponseEntity.ok(slm);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @PostMapping("create")
    public ResponseEntity<SpecificLessonMedia> create(@RequestBody SpecificLessonMedia specificLessonMedia, @RequestParam("image") MultipartFile image){
        if (specificLessonMedia.getSpecificLessonId() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (!specificLessonRepository.existsById(specificLessonMedia.getSpecificLessonId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLessonMedia slm = new SpecificLessonMedia(0, u.getId(), specificLessonMedia.getSpecificLessonId(), 0);
        SpecificLesson sl = specificLessonRepository.findById(slm.getSpecificLessonId()).get();
        Member m = memberRepository.findByGroupIdAndUserId(sl.getGroupId(), u.getId()).get();
        if(m != null){
            long id = 0;
            try {
                id = fileService.post(image);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            slm.setFileId(id);
            slm = specificLessonMediaRepository.save(slm);
            return ResponseEntity.ok(slm);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        if(!specificLessonMediaRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLessonMedia slm = specificLessonMediaRepository.findById(id).get();
        SpecificLesson sl = specificLessonRepository.findById(slm.getSpecificLessonId()).get();
        Member m = memberRepository.findByGroupIdAndUserId(sl.getGroupId(), u.getId()).get();
        if(m != null){
            if(slm.getUserId() == u.getId()){
                fileService.delete(slm.getFileId());
                specificLessonMediaRepository.delete(slm);
                specificLessonMediaCommentRepository.deleteByMediaId(slm.getId());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("{id}/specificLessonMediaComments")
    public ResponseEntity<ArrayList<Long>> specificLessonMediaComments(@PathVariable("id") long id){
        if(!specificLessonMediaRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        SpecificLessonMedia slm = specificLessonMediaRepository.findById(id).get();
        SpecificLesson sl = specificLessonRepository.findById(slm.getSpecificLessonId()).get();
        ArrayList<SpecificLessonMediaComment> slmcs = (ArrayList<SpecificLessonMediaComment>) specificLessonMediaCommentRepository.findSpecificLessonMediaCommentByMediaId(slm.getId()).get();
        ArrayList<Long> ids = new ArrayList<>();
        for (SpecificLessonMediaComment slmc : slmcs){
            ids.add(slmc.getId());
        }
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(ids);
        Member m = memberRepository.findByGroupIdAndUserId(sl.getGroupId(), u.getId()).get();
        if(m != null){
            return ResponseEntity.ok(ids);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("list")
    public ResponseEntity<ArrayList<Long>> list(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        ArrayList<SpecificLessonMedia> slms = (ArrayList<SpecificLessonMedia>) specificLessonMediaRepository.findAll();
        slms.sort(new Comparator<SpecificLessonMedia>() {
            @Override
            public int compare(SpecificLessonMedia o1, SpecificLessonMedia o2) {
                return -1 * Long.compare(o1.getSpecificLessonId(), o2.getSpecificLessonId());
            }
        });
        ArrayList<Long> ids = new ArrayList<>();
        for (SpecificLessonMedia slm : slms){
            ids.add(slm.getId());
        }
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(ids);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
