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
    @Column(name = "id")
    private long id;
    @Column(name = "chat_id", nullable = false)
    private long chatId;
    @Column(name = "ava_id", nullable = false)
    private long avaId;
    @Column(name = "name", nullable = false)
    private String name;

}
