package org.railsschool.tiramisu.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import org.railsschool.tiramisu.R;

/**
 * @class PushNotificationSystem
 * @brief
 */
public class PushNotificationSystem {
    private int     _idCounter;
    private Context _context;

    public PushNotificationSystem(Context context) {
        this._idCounter = 1;
        this._context = context;
    }

    /**
     * Sends a push notification to user
     *
     * @param title   Desired title
     * @param message Inner message
     * @param intent  Intent to start once notification is clicked
     */
    public void notify(String title, String message, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(_context);
        NotificationManager manager;

        builder
            .setSmallIcon(R.drawable.ic_notification_pin)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    _context.getResources(),
                    R.mipmap.ic_launcher
                )
            )
            // Vibrate and produce sound if possible. Auto-remove notification when clicked
            .setDefaults(
                Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND |
                Notification.VISIBILITY_PUBLIC | Notification.FLAG_AUTO_CANCEL
            )
            .setContentIntent(
                PendingIntent.getActivity(
                    _context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .setContentTitle(title)
            .setContentText(message)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true);

        manager =
            (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(_idCounter++, builder.build());
    }
}
