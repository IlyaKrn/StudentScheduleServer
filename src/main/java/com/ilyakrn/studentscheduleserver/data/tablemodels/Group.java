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
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "chat_id")
    private long chatId;
    @Column(name = "name")
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
