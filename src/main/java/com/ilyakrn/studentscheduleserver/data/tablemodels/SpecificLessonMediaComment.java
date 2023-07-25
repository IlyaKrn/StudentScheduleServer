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
    @Column(name = "id")
    private long id;
    @Column(name = "text")
    private String text;
    @Column(name = "author_id")
    private long authorId;
    @Column(name = "media_id")
    private long mediaId;
    @Column(name = "question_comment_id")
    private long questionCommentId;
}
