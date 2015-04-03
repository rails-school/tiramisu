package org.railsschool.tiramisu.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.bll.BusinessFactory;
import org.railsschool.tiramisu.utils.PushNotificationSystem;
import org.railsschool.tiramisu.views.activities.MainActivity;

/**
 * @class DayReminderReceiver
 * @brief
 */
public class DayReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        BusinessFactory
            .provideLesson(context)
            .getUpcoming(
                (lesson) -> {
                    new PushNotificationSystem(context).notify(
                        context.getString(R.string.reminder_next_day),
                        lesson.getTitle(),
                        MainActivity.class
                    );
                }
            );
    }
}
