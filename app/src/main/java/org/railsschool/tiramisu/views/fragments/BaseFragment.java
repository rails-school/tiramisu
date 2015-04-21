package org.railsschool.tiramisu.views.fragments;

import android.app.Fragment;

import org.railsschool.tiramisu.views.events.ErrorEvent;

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
    }
}
