package com.ilyakrn.studentscheduleserver.web;

import com.ilyakrn.studentscheduleserver.data.tablemodels.CustomLesson;
import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLesson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/specificLessons")
public class SpecificLessonController {

    @GetMapping("{id}")
    public ResponseEntity<SpecificLesson> get(@PathVariable("id") long id){
        return null;
    }

    @PostMapping("{id}")
    public ResponseEntity<SpecificLesson> post(@PathVariable("id") long id, @RequestBody SpecificLesson specificLesson){
        return null;
    }
    @PostMapping("create")
    public ResponseEntity<SpecificLesson> create(@RequestBody SpecificLesson specificLesson){
        return null;
    }
    @PostMapping("{id}")
    public ResponseEntity<Void> delete(){
        return null;
    }
    @GetMapping("{id}/specificLessonMedias")
    public ResponseEntity<ArrayList<Long>> specificLessonMedias(@PathVariable("id") long id){
        return null;
    }

}
