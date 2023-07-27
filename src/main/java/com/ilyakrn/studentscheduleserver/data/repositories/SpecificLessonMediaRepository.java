package com.ilyakrn.studentscheduleserver.data.repositories;


import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLesson;
import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLessonMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpecificLessonMediaRepository extends JpaRepository<SpecificLessonMedia, Long> {
    Optional<List<SpecificLessonMedia>> findSpecificLessonMediaBySpecificLessonId(long id);
}
