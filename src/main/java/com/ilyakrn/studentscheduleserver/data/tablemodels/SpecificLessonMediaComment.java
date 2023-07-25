package com.ilyakrn.studentscheduleserver.data.tablemodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SpecificLessonMediaComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String text;
    private long authorId;
    @Column(name = "media_id")
    private long mediaId;
    private long questionCommentId;
}
