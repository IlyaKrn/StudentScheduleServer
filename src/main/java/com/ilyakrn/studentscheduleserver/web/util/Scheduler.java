package com.ilyakrn.studentscheduleserver.web.util;

import com.ilyakrn.studentscheduleserver.data.repositories.*;
import com.ilyakrn.studentscheduleserver.data.tablemodels.LessonTemplate;
import com.ilyakrn.studentscheduleserver.data.tablemodels.ScheduleTemplate;
import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLesson;
import com.ilyakrn.studentscheduleserver.data.tablemodels.SpecificLessonMedia;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Scheduler {
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

    private ArrayList<SpecificLesson> scheduleLessons(long endTimestamp, List<LessonTemplate> schedule, long groupId) {
        final Calendar weekStartCalendar = Calendar.getInstance();
        weekStartCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        weekStartCalendar.set(Calendar.HOUR, 0);
        weekStartCalendar.set(Calendar.MINUTE, 0);
        weekStartCalendar.set(Calendar.SECOND, 0);
        weekStartCalendar.set(Calendar.MILLISECOND, 0);
        final long currentTime = Calendar.getInstance().getTimeInMillis();
        final long weekStartTime = weekStartCalendar.getTimeInMillis() - 86400000L/2;
        int week = 0;
        ArrayList<SpecificLesson> generatedLessons = new ArrayList<>();
        while ((weekStartTime + WEEK_LENGTH*week) < endTimestamp) {
            for (LessonTemplate lesson: schedule) {
                long lessonTime = weekStartTime + week*WEEK_LENGTH + lesson.getTime();
                if (lessonTime < currentTime) continue;
                if (lessonTime > endTimestamp) break;
                SpecificLesson newLesson = new SpecificLesson(0, groupId, lesson.getLessonId(),
                        lessonTime, false);
                generatedLessons.add(newLesson);
            }
            week++;
        }
        return generatedLessons;
    }

    public void updateSchedule(long scheduleId){
        ScheduleTemplate st = scheduleTemplateRepository.findById(scheduleId).get();
        ArrayList<LessonTemplate> lts = (ArrayList<LessonTemplate>) lessonTemplateRepository.findLessonTemplateByScheduleTemplateId(st.getId()).get();
        ArrayList<SpecificLesson> sls = scheduleLessons(st.getTimeStop(), lts, st.getGroupId());
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

    public static void main(String[] args) {
        ArrayList<LessonTemplate> schedule = new ArrayList<>();
        // Получаем расписание в формате UTC +0
        schedule.add(new LessonTemplate(0, 0L, 1L, 49802000L));
        schedule.add(new LessonTemplate(0, 0L, 2L, 138002000L));
        schedule.add(new LessonTemplate(0, 0L, 3L, 217202000L));
        schedule.add(new LessonTemplate(0, 0L, 4L, 297302000L));
        schedule.add(new LessonTemplate(0, 0L, 5L, 380102000L));

        // Выдаем распределенное расписание в формате UTC +0
        Scheduler s = new Scheduler();
        List<SpecificLesson> result = s.scheduleLessons(1691366342000L, schedule,1L);
        for (SpecificLesson lesson: result) {
            System.out.println(format.format(lesson.getTime()) + " " + lesson.getLessonId());
        }
    }
}
