package org.railsschool.tiramisu.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.bll.BusinessFactory;
import org.railsschool.tiramisu.models.bll.serializers.LessonSerializer;
import org.railsschool.tiramisu.utils.PushNotificationSystem;
import org.railsschool.tiramisu.views.activities.MainActivity;

import java.net.URISyntaxException;

/**
 * @class SocketListenerService
 * @brief
 */
public class SocketListenerService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public SocketListenerService() {
        super("SocketListenerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Socket socket = IO.socket(getString(R.string.socket_address));

            socket.on(
                getString(R.string.socket_event),
                (args) -> {
                    Lesson lesson;
                    Gson gson;

                    if (!BusinessFactory.providePreference(getApplicationContext())
                                        .getLessonAlertPreference()) {
                        // User disabled alerts
                        return;
                    }

                    gson = new GsonBuilder()
                        .registerTypeAdapter(Lesson.class, new LessonSerializer())
                        .create();

                    lesson = gson.fromJson(
                        args[0].toString(), new TypeToken<Lesson>() {
                        }.getType()
                    );

                    new PushNotificationSystem(getApplicationContext())
                        .notify(
                            lesson.getTitle(),
                            lesson.getSummary(),
                            MainActivity.startOnClassDetails(
                                getApplicationContext(),
                                lesson.getSlug()
                            )
                        );
                }
            );

            socket.connect();
        } catch (URISyntaxException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
    }
}
