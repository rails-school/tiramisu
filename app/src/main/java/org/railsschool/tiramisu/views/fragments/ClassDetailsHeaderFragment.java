package org.railsschool.tiramisu.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.views.events.ClassDetailsHeaderBackEvent;
import org.railsschool.tiramisu.views.utils.AnimationHelper;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * @class ClassDetailsHeaderFragment
 * @brief
 */
public class ClassDetailsHeaderFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(
            R.layout.fragment_class_details_header, container,
            false
        );
        ButterKnife.inject(this, fragment);

        return fragment;
    }

    @OnClick(R.id.fragment_class_details_header_back)
    public void onBack(View view) {
        AnimationHelper.pressed(view);
        EventBus.getDefault().post(new ClassDetailsHeaderBackEvent());
    }

    @OnClick(R.id.fragment_class_details_header_calendar)
    public void onCalendarAdd(View view) {

    }

    @OnClick(R.id.fragment_class_details_header_share)
    public void onShare(View view) {

    }

    @OnClick(R.id.fragment_class_details_header_map)
    public void onDirections(View view) {

    }

    @OnClick(R.id.fragment_class_details_header_attendance)
    public void onAttendanceToggle(View view) {

    }
}
