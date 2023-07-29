package com.ilyakrn.studentscheduleserver.web;

import com.ilyakrn.studentscheduleserver.data.repositories.*;
import com.ilyakrn.studentscheduleserver.data.tablemodels.*;
import com.ilyakrn.studentscheduleserver.web.util.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/lessonTemplates")
public class LessonTemplateController {



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
    @Autowired
    private ScheduleTemplateRepository scheduleTemplateRepository;

    @GetMapping("{id}")
    public ResponseEntity<LessonTemplate> get(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!lessonTemplateRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        LessonTemplate lt = lessonTemplateRepository.findById(id).get();
        ScheduleTemplate st = scheduleTemplateRepository.findById(lt.getScheduleTemplateId()).get();
        for(Member m : memberRepository.findMemberByGroupId(st.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                return ResponseEntity.ok(lt);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(lt);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("{id}")
    public ResponseEntity<LessonTemplate> post(@PathVariable("id") long id, @RequestBody LessonTemplate lessonTemplate){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!lessonTemplateRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        LessonTemplate lt = lessonTemplateRepository.findById(id).get();
        ScheduleTemplate st = scheduleTemplateRepository.findById(lt.getScheduleTemplateId()).get();
        if (lessonTemplate.getTime() != 0)
            lt.setTime(lessonTemplate.getTime());
        if (lessonTemplate.getLessonId() != 0)
            lt.setLessonId(lessonTemplate.getLessonId());
        for(Member m : memberRepository.findMemberByGroupId(st.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                if(m.getAccessLevel() <= 1){
                    lt = lessonTemplateRepository.save(lt);
                    Scheduler.updateSchedule(st.getId());
                    return ResponseEntity.ok(lt);
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @PostMapping("create")
    public ResponseEntity<LessonTemplate> create(@RequestBody LessonTemplate lessonTemplate){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (lessonTemplate.getTime() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (lessonTemplate.getScheduleTemplateId() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (lessonTemplate.getLessonId() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(!scheduleTemplateRepository.existsById(lessonTemplate.getId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if(!customLessonRepository.existsById(lessonTemplate.getLessonId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        ScheduleTemplate st = scheduleTemplateRepository.findById(lessonTemplate.getScheduleTemplateId()).get();
        LessonTemplate lt = new LessonTemplate(0, lessonTemplate.getScheduleTemplateId(), lessonTemplate.getLessonId(), lessonTemplate.getTime());
        for(Member m : memberRepository.findMemberByGroupId(st.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                if(m.getAccessLevel() <= 1){
                    lt = lessonTemplateRepository.save(lt);
                    Scheduler.updateSchedule(st.getId());
                    return ResponseEntity.ok(lt);
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!lessonTemplateRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        LessonTemplate lt = lessonTemplateRepository.findById(id).get();
        ScheduleTemplate st = scheduleTemplateRepository.findById(lt.getScheduleTemplateId()).get();
        for(Member mm : memberRepository.findMemberByGroupId(st.getGroupId()).get()){
            if(u.getId() == mm.getUserId()){
                if(mm.getAccessLevel() <= 1){
                    lessonTemplateRepository.delete(lt);
                    Scheduler.updateSchedule(st.getId());
                    return ResponseEntity.ok().build();
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
