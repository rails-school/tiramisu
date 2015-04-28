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

    /**
     * Changes a single fragment
     *
     * @param resourceId
     * @param fragment
     */
    public void setFragment(int resourceId, Fragment fragment) {
        getFragmentManager()
            .beginTransaction()
            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
            .replace(resourceId, fragment)
            .addToBackStack(fragment.getClass().getSimpleName())
            .commit();
    }

    /**
     * Changes multiple fragments in a same commit
     *
     * @param fragments
     * @param keepInStack
     */
    public void setFragments(Map<Integer, Fragment> fragments, boolean keepInStack) {
        FragmentTransaction t = getFragmentManager().beginTransaction();

        t.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        for (Map.Entry<Integer, Fragment> e : fragments.entrySet()) {
            t.replace(e.getKey().intValue(), e.getValue());
        }

        if (keepInStack) {
            t.addToBackStack(null);
        }

        t.commit();
    }

    public void setFragments(Map<Integer, Fragment> fragments) {
        setFragments(fragments, true);
    }
}
