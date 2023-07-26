package com.ilyakrn.studentscheduleserver.data.repositories;


import com.ilyakrn.studentscheduleserver.data.tablemodels.Group;
import com.ilyakrn.studentscheduleserver.data.tablemodels.ScheduleTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleTemplateRepository extends JpaRepository<ScheduleTemplate, Long> {
}
