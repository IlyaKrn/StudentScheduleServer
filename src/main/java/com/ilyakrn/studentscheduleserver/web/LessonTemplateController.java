package com.ilyakrn.studentscheduleserver.web;

import com.ilyakrn.studentscheduleserver.data.repositories.*;
import com.ilyakrn.studentscheduleserver.data.tablemodels.*;
import com.ilyakrn.studentscheduleserver.services.ScheduleService;
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
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("{id}")
    public ResponseEntity<LessonTemplate> get(@PathVariable("id") long id){
        if(!lessonTemplateRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        LessonTemplate lt = lessonTemplateRepository.findById(id).get();
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(lt);
        ScheduleTemplate st = scheduleTemplateRepository.findById(lt.getScheduleTemplateId()).get();
        Member m = memberRepository.findByGroupIdAndUserId(st.getGroupId(), u.getId()).get();
        if(m != null){
            return ResponseEntity.ok(lt);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<LessonTemplate> patch(@PathVariable("id") long id, @RequestBody LessonTemplate lessonTemplate){
        if(!lessonTemplateRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        LessonTemplate lt = lessonTemplateRepository.findById(id).get();
        ScheduleTemplate st = scheduleTemplateRepository.findById(lt.getScheduleTemplateId()).get();
        if (lessonTemplate.getTime() != 0)
            lt.setTime(lessonTemplate.getTime());
        if (lessonTemplate.getLessonId() != 0)
            lt.setLessonId(lessonTemplate.getLessonId());
        Member m = memberRepository.findByGroupIdAndUserId(st.getGroupId(), u.getId()).get();
        if(m != null){
            if(m.getRoles().contains(MemberRole.ADMIN)){
                lt = lessonTemplateRepository.save(lt);
                scheduleService.updateSchedule(st.getId());
                return ResponseEntity.ok(lt);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @PostMapping("create")
    public ResponseEntity<LessonTemplate> create(@RequestBody LessonTemplate lessonTemplate){
        if (lessonTemplate.getTime() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (lessonTemplate.getScheduleTemplateId() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (lessonTemplate.getLessonId() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(!scheduleTemplateRepository.existsById(lessonTemplate.getScheduleTemplateId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if(!customLessonRepository.existsById(lessonTemplate.getLessonId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        ScheduleTemplate st = scheduleTemplateRepository.findById(lessonTemplate.getScheduleTemplateId()).get();
        LessonTemplate lt = new LessonTemplate(0, lessonTemplate.getScheduleTemplateId(), lessonTemplate.getLessonId(), lessonTemplate.getTime());
        Member m = memberRepository.findByGroupIdAndUserId(st.getGroupId(), u.getId()).get();
        if(m != null){
            if(m.getRoles().contains(MemberRole.ADMIN)){
                lt = lessonTemplateRepository.save(lt);
                scheduleService.updateSchedule(st.getId());
                return ResponseEntity.ok(lt);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        if(!lessonTemplateRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        LessonTemplate lt = lessonTemplateRepository.findById(id).get();
        ScheduleTemplate st = scheduleTemplateRepository.findById(lt.getScheduleTemplateId()).get();
        Member m = memberRepository.findByGroupIdAndUserId(st.getGroupId(), u.getId()).get();
        if(m != null){
            if(m.getRoles().contains(MemberRole.ADMIN)){
                lessonTemplateRepository.delete(lt);
                scheduleService.updateSchedule(st.getId());
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
