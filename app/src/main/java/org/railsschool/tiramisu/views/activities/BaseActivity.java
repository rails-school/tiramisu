package org.railsschool.tiramisu.views.activities;

import android.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

import java.util.Map;

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

    public void setFragment(int resourceId, Fragment fragment) {
        getFragmentManager()
            .beginTransaction()
            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
            .replace(resourceId, fragment)
            .addToBackStack(fragment.getClass().getSimpleName())
            .commit();
    }

    public void setFragments(Map<Integer, Fragment> fragments) {
        FragmentTransaction t = getFragmentManager().beginTransaction();

        t.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        for (Map.Entry<Integer, Fragment> e : fragments.entrySet()) {
            t.replace(e.getKey().intValue(), e.getValue());
        }

        t.addToBackStack(null).commit();
    }
}
