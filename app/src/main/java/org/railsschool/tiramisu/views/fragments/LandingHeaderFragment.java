package org.railsschool.tiramisu.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.views.events.RefreshClassListEvent;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * @class LandingHeaderFragment
 * @brief
 */
public class LandingHeaderFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(R.layout.fragment_landing_header, container, false);
        ButterKnife.inject(this, fragment);

        return fragment;
    }

    @OnClick(R.id.fragment_landing_header_refresh)
    public void onRefresh(View view) {
        EventBus.getDefault().post(new RefreshClassListEvent());
    }

    @OnClick(R.id.fragment_landing_header_menu)
    public void onMenuSelected(View view) {

    }
}
