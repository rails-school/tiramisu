package org.railsschool.tiramisu.views.activities;

import android.app.Fragment;
import android.os.Bundle;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.views.events.ClassDetailsHeaderBackEvent;
import org.railsschool.tiramisu.views.events.ClassDetailsInitEvent;
import org.railsschool.tiramisu.views.events.ClassDetailsRequestedEvent;
import org.railsschool.tiramisu.views.events.ErrorEvent;
import org.railsschool.tiramisu.views.fragments.ClassDetailsFragment;
import org.railsschool.tiramisu.views.fragments.ClassDetailsHeaderFragment;
import org.railsschool.tiramisu.views.fragments.ClassListFragment;
import org.railsschool.tiramisu.views.fragments.LandingHeaderFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends BaseActivity {

    private enum CurrentDisplay {
        LANDING,
        CLASS_DETAILS
    }

    private static class RestoreDisplayEvent {
        private CurrentDisplay _previousDisplay;

        public RestoreDisplayEvent(CurrentDisplay display) {
            this._previousDisplay = display;
        }

        public CurrentDisplay getPreviousDisplay() {
            return _previousDisplay;
        }
    }

    private CurrentDisplay _currentDisplay;

    private void _setLandingContent() {
        Map<Integer, Fragment> fragmentMap = new HashMap<>();

        _currentDisplay = CurrentDisplay.LANDING;

        fragmentMap.put(R.id.main_activity_header, new LandingHeaderFragment());
        fragmentMap.put(R.id.main_activity_body, new ClassListFragment());
        setFragments(fragmentMap, false);
    }

    private void _setClassDetailsContent() {
        Map<Integer, Fragment> fragmentMap = new HashMap<>();

        _currentDisplay = CurrentDisplay.CLASS_DETAILS;

        fragmentMap.put(R.id.main_activity_header, new ClassDetailsHeaderFragment());
        fragmentMap.put(R.id.main_activity_body, new ClassDetailsFragment());
        setFragments(fragmentMap);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        _setLandingContent();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
        EventBus.getDefault().postSticky(new RestoreDisplayEvent(_currentDisplay));
    }

    public void onEventMainThread(RestoreDisplayEvent event) {
        if (event.getPreviousDisplay() == CurrentDisplay.LANDING) {
            _setLandingContent();
        } else {
            _setClassDetailsContent();
        }
    }

    public void onEventMainThread(ErrorEvent event) {
        Crouton
            .makeText(this, event.getMessage(), Style.ALERT)
            .show();
    }

    public void onEventMainThread(ClassDetailsRequestedEvent event) {
        _setClassDetailsContent();
        EventBus.getDefault()
                .postSticky(new ClassDetailsInitEvent(event.getLessonSlug()));
    }

    public void onEventMainThread(ClassDetailsHeaderBackEvent event) {
        _setLandingContent();
    }
}
