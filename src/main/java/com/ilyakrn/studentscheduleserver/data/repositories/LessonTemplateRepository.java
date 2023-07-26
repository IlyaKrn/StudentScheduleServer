package com.ilyakrn.studentscheduleserver.data.repositories;


import com.ilyakrn.studentscheduleserver.data.tablemodels.Group;
import com.ilyakrn.studentscheduleserver.data.tablemodels.LessonTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonTemplateRepository extends JpaRepository<LessonTemplate, Long> {
}
