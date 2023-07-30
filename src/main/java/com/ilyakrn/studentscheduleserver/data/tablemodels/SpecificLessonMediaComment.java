package com.ilyakrn.studentscheduleserver.data.tablemodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "specific_lesson_media_comments")
public class SpecificLessonMediaComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "text", nullable = false)
    private String text;
    @Column(name = "author_id", nullable = false)
    private long userId;
    @Column(name = "media_id", nullable = false)
    private long mediaId;
    @Column(name = "question_comment_id")
    private long questionCommentId;
}
