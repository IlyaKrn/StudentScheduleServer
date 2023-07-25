package com.ilyakrn.studentscheduleserver.data.tablemodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class LessonTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "schedule_template_id")
    private long ScheduleTemplateId;
    private long groupId;
    private long lessonId;
    private long time;

}
