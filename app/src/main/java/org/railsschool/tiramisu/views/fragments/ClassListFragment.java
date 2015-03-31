package org.railsschool.tiramisu.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.bll.BusinessFactory;
import org.railsschool.tiramisu.views.adapters.ClassAdapter;
import org.railsschool.tiramisu.views.events.ErrorEvent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * @class ClassListFragment
 * @brief
 */
public class ClassListFragment extends Fragment {
    @InjectView(R.id.fragment_class_list)
    ListView _list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(R.layout.fragment_class_list, container, false);
        ButterKnife.inject(this, fragment);

        BusinessFactory
            .provideLesson(getActivity())
            .sortIdsByDate(
                (ids) -> {
                    _list.setAdapter(new ClassAdapter(ids, getActivity()));
                },
                (error) -> {
                    EventBus.getDefault().post(new ErrorEvent(error));
                }
            );

        return fragment;
    }
}
