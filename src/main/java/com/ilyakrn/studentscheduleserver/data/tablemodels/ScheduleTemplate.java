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
@Table(name = "schedule_templates")
public class ScheduleTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "group_id", nullable = false)
    private long groupId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "time_start", nullable = false)
    private long timeStart;
    @Column(name = "time_stop", nullable = false)
    private long timeStop;

}
