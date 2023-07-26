package com.ilyakrn.studentscheduleserver.data.repositories;


import com.ilyakrn.studentscheduleserver.data.tablemodels.CustomLesson;
import com.ilyakrn.studentscheduleserver.data.tablemodels.Group;
import com.ilyakrn.studentscheduleserver.data.tablemodels.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomLessonRepository extends JpaRepository<CustomLesson, Long> {
    Optional<List<CustomLesson>> findCustomLessonByGroupId(long groupId);

}
