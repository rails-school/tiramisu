package org.railsschool.tiramisu.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubException;

import org.railsschool.tiramisu.R;

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
                "channel", new Callback() {
                    @Override
                    public void successCallback(String channel, Object message) {
                        Object o = message;

                        Log.d("FOO", o.toString());
                    }
                }
            );
        } catch (PubnubException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
    }
}
