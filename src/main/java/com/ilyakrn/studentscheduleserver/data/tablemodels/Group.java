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
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long chatId;
    private String name;
    @ElementCollection
    private List<Member> members;
    @ElementCollection
    private List<ScheduleTemplate> scheduleTemplates;
    @ElementCollection
    private List<SpecificLesson> specificLessons;
    @ElementCollection
    private List<CustomLesson> customLessons;

}
