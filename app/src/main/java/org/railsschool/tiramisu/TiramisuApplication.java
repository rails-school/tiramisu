package org.railsschool.tiramisu;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.railsschool.tiramisu.receivers.RemindSchedulerReceiver;

/**
 * @class TiramisuApplication
 * @brief
 */
public class TiramisuApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AlarmManager alarmManager =
            (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        PendingIntent alarmIntent;

        alarmIntent = PendingIntent.getBroadcast(
            this,
            0,
            new Intent(this, RemindSchedulerReceiver.class),
            0
        );

        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME,
            0,
            AlarmManager.INTERVAL_HALF_HOUR,
            alarmIntent
        );
    }
}
