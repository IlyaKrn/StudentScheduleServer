package com.ilyakrn.studentscheduleserver.web;

import com.ilyakrn.studentscheduleserver.data.tablemodels.CustomLesson;
import com.ilyakrn.studentscheduleserver.data.tablemodels.Member;
import com.ilyakrn.studentscheduleserver.data.tablemodels.ScheduleTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/scheduleTemplates")
public class ScheduleTemplateController {

    @GetMapping("{id}")
    public ResponseEntity<ScheduleTemplate> get(@PathVariable("id") long id){
        return null;
    }

    @PostMapping("{id}")
    public ResponseEntity<ScheduleTemplate> post(@PathVariable("id") long id, @RequestBody ScheduleTemplate scheduleTemplate){
        return null;
    }
    @PostMapping("create")
    public ResponseEntity<ScheduleTemplate> create(@RequestBody ScheduleTemplate scheduleTemplate){
        return null;
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(){
        return null;
    }
    @GetMapping("{id}/lessonTemplates")
    public ResponseEntity<ArrayList<Long>> lessonTemplates(@PathVariable("id") long id){
        return null;
    }

}
