package com.ilyakrn.studentscheduleserver.data.repositories;


import com.ilyakrn.studentscheduleserver.data.tablemodels.CustomLesson;
import com.ilyakrn.studentscheduleserver.data.tablemodels.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomLessonRepository extends JpaRepository<CustomLesson, Long> {
}
