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
@Table(name = "specific_lesson_medias")
public class SpecificLessonMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "user_id", nullable = false)
    private long userId;
    @Column(name = "specific_lesson_id", nullable = false)
    private long specificLessonId;
    @Column(name = "file_id", nullable = false)
    private long fileId;

}
