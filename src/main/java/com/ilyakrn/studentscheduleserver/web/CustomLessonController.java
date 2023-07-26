package com.ilyakrn.studentscheduleserver.web;

import com.ilyakrn.studentscheduleserver.data.tablemodels.CustomLesson;
import com.ilyakrn.studentscheduleserver.data.tablemodels.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/customLessons")
public class CustomLessonController {

    @GetMapping("{id}")
    public ResponseEntity<CustomLesson> get(@PathVariable("id") long id){
        return null;
    }

    @PostMapping("{id}")
    public ResponseEntity<CustomLesson> post(@PathVariable("id") long id, @RequestBody CustomLesson customLesson){
        return null;
    }
    @PostMapping("create")
    public ResponseEntity<CustomLesson> create(@RequestBody CustomLesson customLesson){
        return null;
    }
    @PostMapping("{id}")
    public ResponseEntity<Void> delete(){
        return null;
    }
    @GetMapping("{id}/lessonTemplates")
    public ResponseEntity<Long> lessonTemplates(@PathVariable("id") long id){
        return null;
    }
    @GetMapping("{id}/specificLessons")
    public ResponseEntity<Long> specificLessons(@PathVariable("id") long id){
        return null;
    }

}
