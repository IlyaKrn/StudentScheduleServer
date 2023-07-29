package com.ilyakrn.studentscheduleserver.data.repositories;


import com.ilyakrn.studentscheduleserver.data.tablemodels.Group;
import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SpecificLessonRepository extends JpaRepository<SpecificLesson, Long> {
    Optional<List<SpecificLesson>> findSpecificLessonByGroupId(long id);

    Optional<List<SpecificLesson>> findSpecificLessonByLessonId(long id);
}
