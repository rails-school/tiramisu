package org.railsschool.tiramisu.views.fragments;

import android.app.Fragment;

import org.railsschool.tiramisu.views.activities.ProgressActivity;
import org.railsschool.tiramisu.views.events.ErrorEvent;
import org.railsschool.tiramisu.views.events.ProgressDoneEvent;
import org.railsschool.tiramisu.views.events.ProgressForkEvent;

import de.greenrobot.event.EventBus;

/**
 * @class BaseFragment
 * @brief
 */
public abstract class BaseFragment extends Fragment {
    public void publishError(String error) {
        if (!isAdded()) {
            return; // Prevent async conflicts
        }
        EventBus.getDefault().post(new ErrorEvent(error));
        done();
    }

    public void fork() {
        ProgressActivity.getBus().post(new ProgressForkEvent());
    }

    public void done() {
        ProgressActivity.getBus().post(new ProgressDoneEvent());
    }
}
