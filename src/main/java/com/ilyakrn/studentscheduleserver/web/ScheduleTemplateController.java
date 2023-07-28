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

import java.util.ArrayList;
import java.util.Comparator;

@RestController
@RequestMapping("api/scheduleTemplates")
public class ScheduleTemplateController {


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
    private SpecificLessonMediaRepository specificLessonMediaRepository;
    @Autowired
    private SpecificLessonMediaCommentRepository specificLessonMediaCommentRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private Scheduler scheduler;

    @GetMapping("{id}")
    public ResponseEntity<ScheduleTemplate> get(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!scheduleTemplateRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        ScheduleTemplate st = scheduleTemplateRepository.findById(id).get();
        for(Member m : memberRepository.findMemberByGroupId(st.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                return ResponseEntity.ok(st);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN) || auth.getAuthorities().contains(Role.ULTIMATE))
            return ResponseEntity.ok(st);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("{id}")
    public ResponseEntity<ScheduleTemplate> post(@PathVariable("id") long id, @RequestBody ScheduleTemplate scheduleTemplate){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!scheduleTemplateRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        ScheduleTemplate st = scheduleTemplateRepository.findById(id).get();
        if (scheduleTemplate.getName() == null)
            scheduleTemplate.setName(st.getName());
        if (scheduleTemplate.getTimeStart() == 0)
            scheduleTemplate.setTimeStart(st.getTimeStart());
        if (scheduleTemplate.getTimeStop() == 0)
            scheduleTemplate.setTimeStop(st.getTimeStop());
        for(Member m : memberRepository.findMemberByGroupId(st.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                if(m.getAccessLevel() <= 1){
                    st = scheduleTemplateRepository.save(new ScheduleTemplate(st.getId(), st.getGroupId(), scheduleTemplate.getName(), scheduleTemplate.getTimeStart(), scheduleTemplate.getTimeStop()));
                    scheduler.updateSchedule(st.getId());
                    return ResponseEntity.ok(st);
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @PostMapping("create")
    public ResponseEntity<ScheduleTemplate> create(@RequestBody ScheduleTemplate scheduleTemplate){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        if (scheduleTemplate.getName() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (scheduleTemplate.getTimeStart() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (scheduleTemplate.getTimeStop() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(!groupRepository.existsById(scheduleTemplate.getGroupId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        for(Member m : memberRepository.findMemberByGroupId(scheduleTemplate.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                if(m.getAccessLevel() <= 1){
                    ScheduleTemplate st = scheduleTemplateRepository.save(new ScheduleTemplate(0, scheduleTemplate.getGroupId(), scheduleTemplate.getName(), scheduleTemplate.getTimeStart(), scheduleTemplate.getTimeStop()));
                    return ResponseEntity.ok(st);
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userRepository.existsByEmail(auth.getName()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!scheduleTemplateRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        ScheduleTemplate st = scheduleTemplateRepository.findById(id).get();
        for(Member m : memberRepository.findMemberByGroupId(st.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                if(m.getAccessLevel() <= 1){
                    lessonTemplateRepository.deleteLessonTemplateByScheduleTemplateId(id);
                    scheduleTemplateRepository.deleteById(id);
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
        if(!scheduleTemplateRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User u = userRepository.findByEmail(auth.getName()).get();
        ScheduleTemplate st = scheduleTemplateRepository.findById(id).get();
        ArrayList<LessonTemplate> lts = (ArrayList<LessonTemplate>) lessonTemplateRepository.findLessonTemplateByScheduleTemplateId(id).get();
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
        for(Member m : memberRepository.findMemberByGroupId(st.getGroupId()).get()){
            if(u.getId() == m.getUserId()){
                return ResponseEntity.ok(ids);
            }
        }
        if(auth.getAuthorities().contains(Role.ADMIN) || auth.getAuthorities().contains(Role.ULTIMATE))
            return ResponseEntity.ok(ids);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
