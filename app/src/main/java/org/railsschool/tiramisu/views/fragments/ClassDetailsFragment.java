package org.railsschool.tiramisu.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.bll.BusinessFactory;
import org.railsschool.tiramisu.models.bll.structs.SchoolClass;
import org.railsschool.tiramisu.views.events.ClassDetailsInitEvent;
import org.railsschool.tiramisu.views.events.ErrorEvent;
import org.railsschool.tiramisu.views.utils.PicassoHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * @class ClassDetailsFragment
 * @brief
 */
public class ClassDetailsFragment extends Fragment {

    private ClassDetailsInitEvent _initArgs;

    @InjectView(R.id.fragment_class_details_headline)
    TextView _headline;

    @InjectView(R.id.fragment_class_details_summary)
    TextView _summary;

    @InjectView(R.id.fragment_class_details_avatar)
    ImageView _avatar;

    @InjectView(R.id.fragment_class_details_teacher)
    TextView _teacher;

    @InjectView(R.id.fragment_class_details_attendees)
    TextView _attendees;

    @InjectView(R.id.fragment_class_details_description)
    TextView _description;

    private void _refreshContent(TextView target, String value) {
        if (!target.getText().toString().trim().equals(value.trim())) {
            target.setVisibility(View.INVISIBLE);
            target.setText(value);
            target.setVisibility(View.VISIBLE);
            YoYo
                .with(Techniques.FadeIn)
                .duration(500)
                .playOn(target);
        }
    }

    private void _setAttendeeNumber(SchoolClass schoolClass) {
        int attendeeNb = schoolClass.getStudents().size();

        _attendees.setVisibility(View.INVISIBLE);
        if (attendeeNb == 0) {
            _attendees.setText(getString(R.string.no_attendee));
        } else if (attendeeNb == 1) {
            _attendees.setText(getString(R.string.one_attendee));
        } else {
            _attendees.setText(
                String.format(getString(R.string.multi_attendees), attendeeNb)
            );
        }
        YoYo
            .with(Techniques.FadeIn)
            .duration(500)
            .playOn(_attendees);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(R.layout.fragment_class_details, container, false);
        ButterKnife.inject(this, fragment);
        EventBus.getDefault().registerSticky(this);

        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().postSticky(_initArgs);
    }

    public void onEventMainThread(ClassDetailsInitEvent event) {
        _initArgs = event;

        BusinessFactory
            .provideLesson(getActivity())
            .getSchoolClassPair(
                event.getLessonId(),
                (schoolClass, teacher) -> {
                    _headline.setText(schoolClass.getLesson().getTitle());
                    _summary.setText(schoolClass.getLesson().getSummary());

                    PicassoHelper.loadAvatar(getActivity(), teacher, _avatar);
                    _teacher.setText(teacher.getDisplayName());

                    _setAttendeeNumber(schoolClass);
                    _description.setText(schoolClass.getLesson().getDescription());
                },
                (newTeacher) -> {
                    _refreshContent(_teacher, newTeacher.getDisplayName());
                    PicassoHelper.loadAvatar(getActivity(), newTeacher, _avatar);
                },
                (error) -> {
                    EventBus.getDefault().post(new ErrorEvent(error));
                }
            );
    }
}
