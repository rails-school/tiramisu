package org.railsschool.tiramisu.views.activities;

import android.app.Activity;
import android.app.Fragment;

import de.keyboardsurfer.android.widget.crouton.Crouton;

/**
 * @class BaseActivity
 * @brief
 */
public abstract class BaseActivity extends Activity {
    @Override
    protected void onDestroy() {
        super.onDestroy();

        Crouton.cancelAllCroutons();
    }

    public void setFragment(int id, Fragment fragment) {
        getFragmentManager()
            .beginTransaction()
            .add(id, fragment)
            .commit();
    }
}
