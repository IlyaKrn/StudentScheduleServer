package com.ilyakrn.studentscheduleserver.web;

import com.ilyakrn.studentscheduleserver.data.tablemodels.CustomLesson;
import com.ilyakrn.studentscheduleserver.data.tablemodels.LessonTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/lessonTemplates")
public class LessonTemplateController {

    @GetMapping("{id}")
    public ResponseEntity<LessonTemplate> get(@PathVariable("id") long id){
        return null;
    }

    @PostMapping("{id}")
    public ResponseEntity<LessonTemplate> post(@PathVariable("id") long id, @RequestBody LessonTemplate lessonTemplate){
        return null;
    }
    @PostMapping("create")
    public ResponseEntity<LessonTemplate> create(@RequestBody LessonTemplate lessonTemplate){
        return null;
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        return null;
    }
}
