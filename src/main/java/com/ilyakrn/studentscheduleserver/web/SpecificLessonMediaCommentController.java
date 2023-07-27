package com.ilyakrn.studentscheduleserver.web;

import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLessonMedia;
import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLessonMediaComment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/specificLessonMediaComments")
public class SpecificLessonMediaCommentController {

    @GetMapping("{id}")
    public ResponseEntity<SpecificLessonMediaComment> get(@PathVariable("id") long id){
        return null;
    }

    @PostMapping("{id}")
    public ResponseEntity<SpecificLessonMediaComment> post(@PathVariable("id") long id, @RequestBody SpecificLessonMediaComment specificLessonMediaComment){
        return null;
    }
    @PostMapping("create")
    public ResponseEntity<SpecificLessonMediaComment> create(@RequestBody SpecificLessonMediaComment specificLessonMediaComment){
        return null;
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        return null;
    }
}
