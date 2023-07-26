package com.ilyakrn.studentscheduleserver.web;

import com.ilyakrn.studentscheduleserver.data.repositories.*;
import com.ilyakrn.studentscheduleserver.data.tablemodels.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;

@RestController
@RequestMapping("api/members")
public class MemberController {

    @GetMapping("{id}")
    public ResponseEntity<Member> get(@PathVariable("id") long id){
        return null;
    }

    @PostMapping("{id}")
    public ResponseEntity<Member> post(@PathVariable("id") long id, @RequestBody Member member){
        return null;
    }
    @PostMapping("create")
    public ResponseEntity<Member> create(@RequestBody Member member){
        return null;
    }
    @PostMapping("{id}")
    public ResponseEntity<Void> delete(){
        return null;
    }

}
