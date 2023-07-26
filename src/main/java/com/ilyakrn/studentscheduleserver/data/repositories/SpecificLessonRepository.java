package com.ilyakrn.studentscheduleserver.data.repositories;


import com.ilyakrn.studentscheduleserver.data.tablemodels.Group;
import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecificLessonRepository extends JpaRepository<SpecificLesson, Long> {
}
