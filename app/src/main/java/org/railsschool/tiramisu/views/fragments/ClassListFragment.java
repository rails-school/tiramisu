package org.railsschool.tiramisu.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.railsschool.tiramisu.R;

import butterknife.ButterKnife;

/**
 * @class ClassListFragment
 * @brief
 */
public class ClassListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(R.layout.fragment_class_list, container);
        ButterKnife.inject(this, fragment);

        return fragment;
    }
}
