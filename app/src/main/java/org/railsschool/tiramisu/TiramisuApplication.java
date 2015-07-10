package org.railsschool.tiramisu;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.railsschool.tiramisu.receivers.DataCleanerReceiver;
import org.railsschool.tiramisu.receivers.RemindSchedulerReceiver;
import org.railsschool.tiramisu.services.SocketListenerService;

/**
 * @class TiramisuApplication
 * @brief
 */
public class TiramisuApplication extends Application {

    @Override
    public void onCreate() {
        Intent socketIntent;
        PendingIntent remindSchedulerIntent, dataCleanerIntent;
        AlarmManager alarmManager;

        super.onCreate();

        // Set up CRON tasks
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // First reminder scheduler. Engines every period if reminders should be
        // created or no
        remindSchedulerIntent = PendingIntent.getBroadcast(
            this,
            0,
            new Intent(this, RemindSchedulerReceiver.class),
            0
        );
        // Inexact accuracy is fine
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME,
            0,
            RemindSchedulerReceiver.PULLER_PERIOD,
            remindSchedulerIntent
        );

        dataCleanerIntent = PendingIntent.getBroadcast(
            this,
            1,
            new Intent(this, DataCleanerReceiver.class),
            0
        );
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME,
            DataCleanerReceiver.PERIOD_IN_MILLI,
            DataCleanerReceiver.PERIOD_IN_MILLI,
            dataCleanerIntent
        );

        // Start socket service
        socketIntent = new Intent(getApplicationContext(), SocketListenerService.class);
        getApplicationContext().startService(socketIntent);
    }
}
