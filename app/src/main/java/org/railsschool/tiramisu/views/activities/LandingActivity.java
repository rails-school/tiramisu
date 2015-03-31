package org.railsschool.tiramisu.views.activities;

import android.os.Bundle;
import android.util.Log;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.views.events.ErrorEvent;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.realm.Realm;

public class LandingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        ButterKnife.inject(this);

        Log.d("FOO", Realm.getInstance(getApplicationContext()).getPath());

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(ErrorEvent event) {
        Crouton
            .makeText(this, event.message, Style.ALERT)
            .show();
    }
}
