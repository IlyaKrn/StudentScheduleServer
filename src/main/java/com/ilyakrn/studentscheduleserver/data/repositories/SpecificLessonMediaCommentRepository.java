package com.ilyakrn.studentscheduleserver.data.repositories;


import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLessonMedia;
import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLessonMediaComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecificLessonMediaCommentRepository extends JpaRepository<SpecificLessonMediaComment, Long> {
}
