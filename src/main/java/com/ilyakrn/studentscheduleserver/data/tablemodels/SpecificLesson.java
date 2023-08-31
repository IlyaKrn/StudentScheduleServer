package com.ilyakrn.studentscheduleserver.data.tablemodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "specific_lessons")
public class SpecificLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "group_id", nullable = false)
    private long groupId;
    @Column(name = "lesson_id", nullable = false)
    private long lessonId;
    @Column(name = "time", nullable = false)
    private long time;
    @Column(name = "canceled", nullable = false)
    private Boolean canceled;
    @Column(name = "comment")
    private String comment;

}
