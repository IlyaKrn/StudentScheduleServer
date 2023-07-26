package com.ilyakrn.studentscheduleserver.data.repositories;


import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLessonMedia;
import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLessonMediaComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpecificLessonMediaCommentRepository extends JpaRepository<SpecificLessonMediaComment, Long> {
    Optional<List<SpecificLessonMediaComment>> findSpecificLessonMediaCommentByUserId(long id);
}
