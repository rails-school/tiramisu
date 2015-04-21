package org.railsschool.tiramisu.views.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.views.events.ClassDetailsHeaderBackEvent;
import org.railsschool.tiramisu.views.events.ClassDetailsHeaderInitEvent;
import org.railsschool.tiramisu.views.events.ClassDetailsInitEvent;
import org.railsschool.tiramisu.views.events.ClassDetailsRequestedEvent;
import org.railsschool.tiramisu.views.events.ConfirmationEvent;
import org.railsschool.tiramisu.views.events.ErrorEvent;
import org.railsschool.tiramisu.views.events.InformationEvent;
import org.railsschool.tiramisu.views.events.SettingsHeaderBackEvent;
import org.railsschool.tiramisu.views.events.SettingsRequestedEvent;
import org.railsschool.tiramisu.views.fragments.ClassDetailsFragment;
import org.railsschool.tiramisu.views.fragments.ClassDetailsHeaderFragment;
import org.railsschool.tiramisu.views.fragments.ClassListFragment;
import org.railsschool.tiramisu.views.fragments.LandingHeaderFragment;
import org.railsschool.tiramisu.views.fragments.SettingsFragment;
import org.railsschool.tiramisu.views.fragments.SettingsHeaderFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends BaseActivity {
    private final static String INTENT_LESSON_SLUG = "lesson_slug";

    private enum Display {
        LANDING,
        CLASS_DETAILS,
        SETTINGS
    }

    private static class RestoreDisplayEvent {
        private Display _previousDisplay;

        public RestoreDisplayEvent(Display display) {
            this._previousDisplay = display;
        }

        public Display getPreviousDisplay() {
            return _previousDisplay;
        }
    }

    private Display _currentDisplay;

    private void _setLandingContent() {
        Map<Integer, Fragment> fragmentMap;

        if (_currentDisplay == Display.LANDING) {
            return;
        }

        _currentDisplay = Display.LANDING;
        fragmentMap = new HashMap<>();

        fragmentMap.put(R.id.main_activity_header, new LandingHeaderFragment());
        fragmentMap.put(R.id.main_activity_body, new ClassListFragment());
        setFragments(fragmentMap, false);
    }

    private void _setClassDetailsContent() {
        Map<Integer, Fragment> fragmentMap;

        if (_currentDisplay == Display.CLASS_DETAILS) {
            return;
        }

        _currentDisplay = Display.CLASS_DETAILS;
        fragmentMap = new HashMap<>();

        fragmentMap.put(R.id.main_activity_header, new ClassDetailsHeaderFragment());
        fragmentMap.put(R.id.main_activity_body, new ClassDetailsFragment());
        setFragments(fragmentMap);
    }

    private void _setSettingsContent() {
        Map<Integer, Fragment> fragmentMap;

        if (_currentDisplay == Display.SETTINGS) {
            return;
        }

        _currentDisplay = Display.SETTINGS;
        fragmentMap = new HashMap<>();

        fragmentMap.put(R.id.main_activity_header, new SettingsHeaderFragment());
        fragmentMap.put(R.id.main_activity_body, new SettingsFragment());
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
    protected void onResume() {
        Bundle extras;

        super.onResume();

        extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(INTENT_LESSON_SLUG)) {
            String slug = extras.getString(INTENT_LESSON_SLUG);

            getIntent().getExtras().remove(INTENT_LESSON_SLUG);
            EventBus.getDefault().post(new ClassDetailsRequestedEvent(slug));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
        EventBus.getDefault().postSticky(new RestoreDisplayEvent(_currentDisplay));
    }

    public void onEventMainThread(RestoreDisplayEvent event) {
        if (event.getPreviousDisplay() == Display.LANDING) {
            _setLandingContent();
        } else if (event.getPreviousDisplay() == Display.SETTINGS) {
            _setSettingsContent();
        } else {
            _setClassDetailsContent();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (_currentDisplay == Display.CLASS_DETAILS || _currentDisplay == Display.SETTINGS) {
            _currentDisplay = Display.LANDING;
        }
    }

    public void onEventMainThread(ErrorEvent event) {
        Crouton.clearCroutonsForActivity(this);
        Crouton
            .makeText(this, event.getMessage(), Style.ALERT)
            .show();
    }

    public void onEventMainThread(ConfirmationEvent event) {
        Crouton.clearCroutonsForActivity(this);
        Crouton
            .makeText(this, event.getMessage(), Style.CONFIRM)
            .show();
    }

    public void onEventMainThread(InformationEvent event) {
        Crouton.clearCroutonsForActivity(this);
        Crouton
            .makeText(this, event.getMessage(), Style.INFO)
            .show();
    }

    public void onEventMainThread(ClassDetailsRequestedEvent event) {
        _setClassDetailsContent();

        // Send lesson slug to fragments
        EventBus.getDefault()
                .postSticky(new ClassDetailsInitEvent(event.getLessonSlug()));

        EventBus.getDefault()
                .postSticky(new ClassDetailsHeaderInitEvent(event.getLessonSlug()));
    }

    public void onEventMainThread(ClassDetailsHeaderBackEvent event) {
        _setLandingContent();
    }

    public void onEventMainThread(SettingsHeaderBackEvent event) {
        _setLandingContent();
    }

    public void onEventMainThread(SettingsRequestedEvent event) {
        _setSettingsContent();
    }

    public static Intent startOnClassDetails(Context context, String slug) {
        Intent intent = new Intent(context, MainActivity.class);

        intent.putExtra(INTENT_LESSON_SLUG, slug);

        return intent;
    }
}
