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
    private ScheduleService scheduleService;

    @GetMapping("{id}")
    public ResponseEntity<ScheduleTemplate> get(@PathVariable("id") long id){
        if(!scheduleTemplateRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        ScheduleTemplate st = scheduleTemplateRepository.findById(id).get();
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(st);
        Member m = memberRepository.findByGroupIdAndUserId(st.getGroupId(), u.getId()).get();
        if(m != null){
            return ResponseEntity.ok(st);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<ScheduleTemplate> patch(@PathVariable("id") long id, @RequestBody ScheduleTemplate scheduleTemplate){
        if(!scheduleTemplateRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        ScheduleTemplate st = scheduleTemplateRepository.findById(id).get();
        if (scheduleTemplate.getName() != null)
            st.setName(scheduleTemplate.getName());
        if (scheduleTemplate.getTimeStart() != 0)
            st.setTimeStart(scheduleTemplate.getTimeStart());
        if (scheduleTemplate.getTimeStop() != 0)
            st.setTimeStop(scheduleTemplate.getTimeStop());
        Member m = memberRepository.findByGroupIdAndUserId(st.getGroupId(), u.getId()).get();
        if(m != null){
            if(m.getRoles().contains(MemberRole.ADMIN)){
                st = scheduleTemplateRepository.save(st);
                scheduleService.updateSchedule(st.getId());
                return ResponseEntity.ok(st);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @PostMapping("create")
    public ResponseEntity<ScheduleTemplate> create(@RequestBody ScheduleTemplate scheduleTemplate){
        if (scheduleTemplate.getName() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (scheduleTemplate.getTimeStart() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (scheduleTemplate.getTimeStop() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(!groupRepository.existsById(scheduleTemplate.getGroupId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        Member m = memberRepository.findByGroupIdAndUserId(scheduleTemplate.getGroupId(), u.getId()).get();
        if(m != null){
            if(m.getRoles().contains(MemberRole.ADMIN)){
                ScheduleTemplate st = scheduleTemplateRepository.save(new ScheduleTemplate(0, scheduleTemplate.getGroupId(), scheduleTemplate.getName(), scheduleTemplate.getTimeStart(), scheduleTemplate.getTimeStop()));
                return ResponseEntity.ok(st);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        if(!scheduleTemplateRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByEmail(auth.getName()).get();
        ScheduleTemplate st = scheduleTemplateRepository.findById(id).get();
        Member m = memberRepository.findByGroupIdAndUserId(st.getGroupId(), u.getId()).get();
        if(m != null){
            if(m.getRoles().contains(MemberRole.ADMIN)){
                lessonTemplateRepository.deleteLessonTemplateByScheduleTemplateId(id);
                scheduleService.updateSchedule(st.getId());
                scheduleTemplateRepository.deleteById(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("{id}/lessonTemplates")
    public ResponseEntity<ArrayList<Long>> lessonTemplates(@PathVariable("id") long id){
        if(!scheduleTemplateRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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
        if(auth.getAuthorities().contains(Role.ADMIN))
            return ResponseEntity.ok(ids);
        Member m = memberRepository.findByGroupIdAndUserId(st.getGroupId(), u.getId()).get();
        if(m != null){
            return ResponseEntity.ok(ids);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
