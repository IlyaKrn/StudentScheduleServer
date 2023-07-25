package com.ilyakrn.studentscheduleserver.data.tablemodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lesson_templates")
public class LessonTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "schedule_template_id")
    private long scheduleTemplateId;
    @Column(name = "group_id")
    private long groupId;
    @Column(name = "lesson_id")
    private long lessonId;
    @Column(name = "time")
    private long time;

}
