package com.ilyakrn.studentscheduleserver.web;

import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLesson;
import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLessonMedia;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/specificLessonMedias")
public class SpecificLessonMediaController {

    @GetMapping("{id}")
    public ResponseEntity<SpecificLessonMedia> get(@PathVariable("id") long id){
        return null;
    }

    @PostMapping("{id}")
    public ResponseEntity<SpecificLessonMedia> post(@PathVariable("id") long id, @RequestBody SpecificLessonMedia specificLessonMedia){
        return null;
    }
    @PostMapping("create")
    public ResponseEntity<SpecificLessonMedia> create(@RequestBody SpecificLessonMedia specificLessonMedia){
        return null;
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        return null;
    }
    @GetMapping("{id}/specificLessonMediaComments")
    public ResponseEntity<ArrayList<Long>> specificLessonMediaComments(@PathVariable("id") long id){
        return null;
    }

}