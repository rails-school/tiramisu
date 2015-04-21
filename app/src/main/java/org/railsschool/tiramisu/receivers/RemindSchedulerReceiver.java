package org.railsschool.tiramisu.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.bll.BusinessFactory;

/**
 * @class RemindSchedulerReceiver
 * @brief
 */
public class RemindSchedulerReceiver extends BroadcastReceiver {
    /**
     * Gets time when user should be notified
     *
     * @param lesson
     * @param hours
     * @return
     */
    private long _getDelayInMilli(Lesson lesson, Hours hours) {
        return new DateTime(lesson.getStartTime())
            .minusHours(hours.getHours())
            .getMillis();
    }

    private void _scheduleAlarm(
        Context context,
        int processId,
        Class receiver,
        Lesson lesson,
        Hours date) {

        AlarmManager alarmManager =
            (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent;

        alarmIntent = PendingIntent.getBroadcast(
            context,
            processId,
            new Intent(context, receiver),
            0
        );

        try {
            alarmManager.cancel(alarmIntent);
        } catch (Exception e) {

        } finally {
            alarmManager.set(
                AlarmManager.RTC,
                _getDelayInMilli(lesson, date),
                alarmIntent
            );
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        BusinessFactory
            .provideLesson(context)
            .engineAlarms(
                (lesson) -> {
                    _scheduleAlarm(context, 100, TwoHourReminderReceiver.class, lesson,
                                   Hours.TWO);
                },
                (lesson) -> {
                    _scheduleAlarm(context, 200, DayReminderReceiver.class, lesson,
                                   Hours.hours(24));
                }
            );
    }
}
