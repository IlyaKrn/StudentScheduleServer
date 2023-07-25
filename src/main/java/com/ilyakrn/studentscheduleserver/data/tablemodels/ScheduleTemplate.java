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
public class ScheduleTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private long timeStart;
    private long timeStop;
    @ElementCollection
    private List<LessonTemplate> lessonTemplates;

}
