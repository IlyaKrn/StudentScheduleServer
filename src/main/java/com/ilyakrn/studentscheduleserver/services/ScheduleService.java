package com.ilyakrn.studentscheduleserver.services;

import com.ilyakrn.studentscheduleserver.data.repositories.*;
import com.ilyakrn.studentscheduleserver.data.tablemodels.LessonTemplate;
import com.ilyakrn.studentscheduleserver.data.tablemodels.ScheduleTemplate;
import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLesson;
import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLessonMedia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class ScheduleService {
    private static final long WEEK_LENGTH = 604800000L;
    private static final SimpleDateFormat format = new SimpleDateFormat("dd HH:mm");


    @Autowired
    private LessonTemplateRepository lessonTemplateRepository;
    @Autowired
    private SpecificLessonRepository specificLessonRepository;
    @Autowired
    private ScheduleTemplateRepository scheduleTemplateRepository;
    @Autowired
    private SpecificLessonMediaRepository specificLessonMediaRepository;
    @Autowired
    private SpecificLessonMediaCommentRepository specificLessonMediaCommentRepository;

    private ArrayList<SpecificLesson> scheduleLessons(long startTimestamp, long endTimestamp, List<LessonTemplate> schedule, long groupId) {
        final Calendar weekStartCalendar = Calendar.getInstance();
        weekStartCalendar.setTimeInMillis(startTimestamp);
        weekStartCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        weekStartCalendar.set(Calendar.HOUR, 0);
        weekStartCalendar.set(Calendar.MINUTE, 0);
        weekStartCalendar.set(Calendar.SECOND, 0);
        weekStartCalendar.set(Calendar.MILLISECOND, 0);
        final long weekStartTime = weekStartCalendar.getTimeInMillis() - 86400000L/2;
        int week = 0;
        ArrayList<SpecificLesson> generatedLessons = new ArrayList<>();
        while ((weekStartTime + WEEK_LENGTH*week) < endTimestamp) {
            for (LessonTemplate lesson: schedule) {
                long lessonTime = weekStartTime + week*WEEK_LENGTH + lesson.getTime();
                if (lessonTime < startTimestamp) continue;
                if (lessonTime > endTimestamp) break;
                SpecificLesson newLesson = new SpecificLesson(0, groupId, lesson.getLessonId(),
                        lessonTime, false, lesson.getComment());
                generatedLessons.add(newLesson);
            }
            week++;
        }
        return generatedLessons;
    }

    public void updateSchedule(long scheduleId){
        ScheduleTemplate st = scheduleTemplateRepository.findById(scheduleId).get();
        ArrayList<LessonTemplate> lts = (ArrayList<LessonTemplate>) lessonTemplateRepository.findLessonTemplateByScheduleTemplateId(st.getId()).get();
        ArrayList<SpecificLesson> sls = scheduleLessons(st.getTimeStart(), st.getTimeStop(), lts, st.getGroupId());
        ArrayList<SpecificLesson> slsold = (ArrayList<SpecificLesson>) specificLessonRepository.findSpecificLessonByGroupId(st.getGroupId()).get();
        for (SpecificLesson sl : slsold){
            if(sl.getTime() > System.currentTimeMillis()) {
                specificLessonRepository.deleteById(sl.getId());
                ArrayList<SpecificLessonMedia> slms = (ArrayList<SpecificLessonMedia>) specificLessonMediaRepository.findSpecificLessonMediaBySpecificLessonId(sl.getId()).get();
                for (SpecificLessonMedia slm : slms){
                    specificLessonMediaCommentRepository.deleteByMediaId(slm.getId());
                }
                specificLessonMediaRepository.deleteSpecificLessonMediaBySpecificLessonId(sl.getId());
            }
        }
        for (SpecificLesson sl : sls){
            specificLessonRepository.save(sl);
        }
    }

}
