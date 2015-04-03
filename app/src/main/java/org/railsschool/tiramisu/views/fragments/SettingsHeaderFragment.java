package org.railsschool.tiramisu.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.views.events.SettingsHeaderBackEvent;
import org.railsschool.tiramisu.views.helpers.AnimationHelper;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * @class SettingsHeaderFragment
 * @brief
 */
public class SettingsHeaderFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(R.layout.fragment_settings_header, container, false);
        ButterKnife.inject(this, fragment);

        return fragment;
    }

    @OnClick(R.id.fragment_settings_header_back)
    public void onBack(View view) {
        AnimationHelper.pressed(view);
        EventBus.getDefault().post(new SettingsHeaderBackEvent());
    }
}
