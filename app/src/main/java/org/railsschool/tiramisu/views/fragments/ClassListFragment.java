package org.railsschool.tiramisu.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.bll.BusinessFactory;
import org.railsschool.tiramisu.views.adapters.ClassAdapter;
import org.railsschool.tiramisu.views.events.RefreshClassListEvent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * @class ClassListFragment
 * @brief
 */
public class ClassListFragment extends BaseFragment {
    @InjectView(R.id.fragment_class_list_no_class)
    TextView _noClassMsg;

    @InjectView(R.id.fragment_class_list)
    ListView _list;

    private void _setContent() {
        fork();
        BusinessFactory
            .provideLesson(getActivity())
            .sortFutureSlugsByDate(
                (slugs) -> {
                    done();
                    if (!isAdded()) {
                        return; // Prevent asynchronous conflicts
                    }

                    if (slugs.size() == 0) {
                        _noClassMsg.setVisibility(View.VISIBLE);
                        _list.setVisibility(View.GONE);
                    } else {
                        _noClassMsg.setVisibility(View.GONE);
                        _list.setVisibility(View.VISIBLE);

                        _list.setAdapter(new ClassAdapter(slugs, getActivity()));
                        YoYo
                            .with(Techniques.FadeIn)
                            .duration(500)
                            .playOn(_list);
                    }
                },
                this::publishError
            );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(R.layout.fragment_class_list, container, false);
        ButterKnife.inject(this, fragment);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        _setContent();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(RefreshClassListEvent event) {
        _setContent();
    }
}
