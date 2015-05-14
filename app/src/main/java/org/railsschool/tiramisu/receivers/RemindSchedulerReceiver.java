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
 * @brief Schedules alarms
 */
public class RemindSchedulerReceiver extends BroadcastReceiver {

    public final static long PULLER_PERIOD = AlarmManager.INTERVAL_HALF_HOUR;

    /**
     * Gets duration before user should be notified
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

    /**
     * Schedules alarm
     *
     * @param context
     * @param processId Alarm process id (should be unique)
     * @param receiver  Alarm receiver
     * @param lesson
     * @param date      Delay before triggering class
     */
    private void _scheduleAlarm(
        Context context,
        int processId,
        Class<? extends BroadcastReceiver> receiver,
        Lesson lesson,
        Hours date) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent;

        alarmIntent = PendingIntent.getBroadcast(
            context,
            processId,
            new Intent(context, receiver),
            0
        );

        try {
            // Cancel existing similar alarm, then schedule it again
            alarmManager.cancel(alarmIntent);
        } catch (Exception e) {
            // No alarm before, does not matter
        } finally {
            // Sets alarm, wake up device if needed
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                _getDelayInMilli(lesson, date),
                alarmIntent
            );
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Woken up periodically. Schedules alarms
        BusinessFactory
            .provideLesson(context)
            .engineAlarms(
                new Long(PULLER_PERIOD).intValue(),
                (lesson) -> {
                    _scheduleAlarm(
                        context, 100, TwoHourReminderReceiver.class, lesson,
                        Hours.TWO
                    );
                },
                (lesson) -> {
                    _scheduleAlarm(
                        context, 200, DayReminderReceiver.class, lesson,
                        Hours.hours(24)
                    );
                }
            );
    }
}
