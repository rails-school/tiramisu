package org.railsschool.tiramisu.views.activities;

import android.os.Bundle;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.views.events.ErrorEvent;
import org.railsschool.tiramisu.views.fragments.ClassListFragment;
import org.railsschool.tiramisu.views.fragments.LandingHeaderFragment;

import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class LandingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        ButterKnife.inject(this);

        setFragment(R.id.landing_activity_header, new LandingHeaderFragment());
        setFragment(R.id.landing_activity_body, new ClassListFragment());
    }

    public void onEventMainThread(ErrorEvent event) {
        Crouton
            .makeText(this, event.getMessage(), Style.ALERT)
            .show();
    }
}
