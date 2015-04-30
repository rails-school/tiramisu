package org.railsschool.tiramisu.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.bll.serializers.LessonSerializer;
import org.railsschool.tiramisu.utils.PushNotificationSystem;
import org.railsschool.tiramisu.views.activities.MainActivity;

/**
 * @class PubnubListenerService
 * @brief
 */
public class PubnubListenerService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public PubnubListenerService() {
        super("PubnubListenerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Pubnub pubnub = new Pubnub(
            getString(R.string.pubnub_publish_key),
            getString(R.string.pubnub_subcribe_key)
        );

        try {
            pubnub.subscribe(
                getString(R.string.pubnub_channel),
                new Callback() {
                    @Override
                    public void successCallback(String channel, Object message) {
                        Lesson lesson;
                        Gson gson;

                        gson = new GsonBuilder()
                            .registerTypeAdapter(Lesson.class, new LessonSerializer())
                            .create();

                        lesson = gson.fromJson(
                            message.toString(), new TypeToken<Lesson>() {
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

                    @Override
                    public void errorCallback(String channel, PubnubError error) {
                        Log.e(getClass().getSimpleName(), error.getErrorString());
                    }
                }
            );
        } catch (PubnubException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
    }
}
