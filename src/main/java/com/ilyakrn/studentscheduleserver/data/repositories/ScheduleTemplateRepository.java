package com.ilyakrn.studentscheduleserver.data.repositories;


import com.ilyakrn.studentscheduleserver.data.tablemodels.Group;
import com.ilyakrn.studentscheduleserver.data.tablemodels.ScheduleTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleTemplateRepository extends JpaRepository<ScheduleTemplate, Long> {
    Optional<List<ScheduleTemplate>> findScheduleTemplateByGroupId(long id);
}
