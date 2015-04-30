package org.railsschool.tiramisu;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.railsschool.tiramisu.receivers.RemindSchedulerReceiver;
import org.railsschool.tiramisu.services.PubnubListenerService;

/**
 * @class TiramisuApplication
 * @brief
 */
public class TiramisuApplication extends Application {

    @Override
    public void onCreate() {
        Intent pubnubIntent;

        super.onCreate();

        // Defines a CRON task for the reminder system
        AlarmManager alarmManager =
            (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        PendingIntent alarmIntent;

        alarmIntent = PendingIntent.getBroadcast(
            this,
            0,
            new Intent(this, RemindSchedulerReceiver.class),
            0
        );

        // Inexact accuracy is fine
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME,
            0,
            AlarmManager.INTERVAL_HALF_HOUR,
            alarmIntent
        );

        // Start pubnub service
        pubnubIntent = new Intent(getApplicationContext(), PubnubListenerService.class);
        getApplicationContext().startService(pubnubIntent);
    }
}
