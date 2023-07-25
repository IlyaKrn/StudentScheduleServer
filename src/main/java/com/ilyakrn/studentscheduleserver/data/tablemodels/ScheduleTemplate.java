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
    @Column(name = "id")
    private long id;
    @Column(name = "group_id")
    private long groupId;
    @Column(name = "name")
    private String name;
    @Column(name = "time_stop")
    private long timeStart;
    @Column(name = "time_stop")
    private long timeStop;
    @ElementCollection
    private List<LessonTemplate> lessonTemplates;

}
