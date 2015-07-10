package org.railsschool.tiramisu.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.railsschool.tiramisu.models.bll.BusinessFactory;

/**
 * @class DataCleanerReceiver
 * @brief
 */
public class DataCleanerReceiver extends BroadcastReceiver {
    public final static long PERIOD_IN_MILLI = 1000 * 60 * 60 * 24 * 7;

    @Override
    public void onReceive(Context context, Intent intent) {
        BusinessFactory.provideLesson(context).cleanDatabase();
        BusinessFactory.provideUser(context).cleanDatabase();
        BusinessFactory.provideVenue(context).cleanDatabase();
    }
}
