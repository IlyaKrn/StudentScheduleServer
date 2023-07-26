package com.ilyakrn.studentscheduleserver.data.repositories;


import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLesson;
import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLessonMedia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecificLessonMediaRepository extends JpaRepository<SpecificLessonMedia, Long> {
}
