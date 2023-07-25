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
public class SpecificLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long lessonId;
    private long time;
    private boolean canceled;
    @ElementCollection
    private List<SpecificLessonMedia> specificLessonMedias;

}
