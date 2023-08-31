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
    @Column(name = "schedule_template_id", nullable = false)
    private long scheduleTemplateId;
    @Column(name = "lesson_id", nullable = false)
    private long lessonId;
    @Column(name = "time", nullable = false)
    private long time;
    @Column(name = "comment")
    private String comment;

}
