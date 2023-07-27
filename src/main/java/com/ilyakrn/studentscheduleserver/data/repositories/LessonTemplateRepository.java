package com.ilyakrn.studentscheduleserver.data.repositories;


import com.ilyakrn.studentscheduleserver.data.tablemodels.Group;
import com.ilyakrn.studentscheduleserver.data.tablemodels.LessonTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonTemplateRepository extends JpaRepository<LessonTemplate, Long> {
    Optional<List<LessonTemplate>> findLessonTemplateByGroupId(long id);
}
