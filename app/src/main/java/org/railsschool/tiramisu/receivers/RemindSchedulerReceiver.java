package org.railsschool.tiramisu.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.bll.BusinessFactory;

/**
 * @class RemindSchedulerReceiver
 * @brief
 */
public class RemindSchedulerReceiver extends BroadcastReceiver {

    /**
     * Returns true if user is currently in notification interval
     *
     * @param lesson
     * @param hours
     * @return
     */
    private boolean _shouldBeNotified(Lesson lesson, Hours hours) {
        DateTime startTime = new DateTime(lesson.getStartTime());

        return !new Interval(
            startTime.minusHours(hours.getHours()),
            startTime
        ).containsNow();
    }

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

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmManager =
            (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        BusinessFactory
            .provideLesson(context)
            .getUpcoming(
                (lesson) -> {
                    if (lesson == null) {
                        // No upcoming lesson, do nothing
                        return;
                    }
                    
                    // 2 hours before notification
                    if (_shouldBeNotified(lesson, Hours.TWO)) {
                        PendingIntent twoHourIntent;

                        twoHourIntent = PendingIntent.getBroadcast(
                            context,
                            100,
                            new Intent(context, TwoHourReminderReceiver.class),
                            0
                        );

                        try {
                            alarmManager.cancel(twoHourIntent);
                        } catch (Exception e) {

                        } finally {
                            alarmManager.set(
                                AlarmManager.RTC,
                                _getDelayInMilli(lesson, Hours.TWO),
                                twoHourIntent
                            );
                        }
                    }

                    // Previous day notification
                    if (_shouldBeNotified(lesson, Hours.hours(24))) {
                        PendingIntent dayIntent;

                        dayIntent = PendingIntent.getBroadcast(
                            context,
                            200,
                            new Intent(context, DayReminderReceiver.class),
                            0
                        );

                        try {
                            alarmManager.cancel(dayIntent);
                        } catch (Exception e) {

                        } finally {
                            alarmManager.set(
                                AlarmManager.RTC,
                                _getDelayInMilli(lesson, Hours.hours(24)),
                                dayIntent
                            );
                        }
                    }
                }
            );
    }
}
