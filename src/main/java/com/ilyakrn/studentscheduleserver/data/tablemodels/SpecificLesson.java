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
    @Column(name = "group_id")
    private long groupId;
    @Column(name = "lesson_id")
    private long lessonId;
    @Column(name = "time")
    private long time;
    @Column(name = "canceled")
    private boolean canceled;
  //  @ElementCollection
  //  private List<SpecificLessonMedia> specificLessonMedias;

}
