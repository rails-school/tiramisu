package org.railsschool.tiramisu.views.activities;

import android.os.Bundle;
import android.util.Log;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.views.events.ClassDetailsHeaderBackEvent;
import org.railsschool.tiramisu.views.events.ClassDetailsInitEvent;
import org.railsschool.tiramisu.views.events.ClassDetailsRequestedEvent;
import org.railsschool.tiramisu.views.events.ErrorEvent;
import org.railsschool.tiramisu.views.fragments.ClassDetailsFragment;
import org.railsschool.tiramisu.views.fragments.ClassDetailsHeaderFragment;
import org.railsschool.tiramisu.views.fragments.ClassListFragment;
import org.railsschool.tiramisu.views.fragments.LandingHeaderFragment;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.realm.Realm;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            .makeText(this, event.getMessage(), Style.ALERT)
            .show();
    }

    public void onEventMainThread(ClassDetailsRequestedEvent event) {
        setFragment(R.id.main_activity_header, new ClassDetailsHeaderFragment());
        setFragment(R.id.main_activity_body, new ClassDetailsFragment());

        EventBus.getDefault().postSticky(new ClassDetailsInitEvent(event.getLessonId()));
    }

    public void onEventMainThread(ClassDetailsHeaderBackEvent event) {
        setFragment(R.id.main_activity_header, new LandingHeaderFragment());
        setFragment(R.id.main_activity_body, new ClassListFragment());
    }
}
