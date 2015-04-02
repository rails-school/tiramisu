package org.railsschool.tiramisu.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.bll.BusinessFactory;
import org.railsschool.tiramisu.models.bll.structs.SchoolClass;
import org.railsschool.tiramisu.views.events.ClassDetailsInitEvent;
import org.railsschool.tiramisu.views.helpers.UserHelper;
import org.railsschool.tiramisu.views.utils.PicassoHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * @class ClassDetailsFragment
 * @brief
 */
public class ClassDetailsFragment extends BaseFragment {

    private ClassDetailsInitEvent _initArgs;
    private SchoolClass           _currentSchoolClass;
    private boolean               _isAttending;

    @InjectView(R.id.fragment_class_details_headline)
    TextView _headline;

    @InjectView(R.id.fragment_class_details_summary)
    TextView _summary;

    @InjectView(R.id.fragment_class_details_avatar)
    ImageView _avatar;

    @InjectView(R.id.fragment_class_details_teacher)
    TextView _teacher;

    @InjectView(R.id.fragment_class_details_attendee_count)
    TextView _attendeeCount;

    @InjectView(R.id.fragment_class_details_description)
    TextView _description;

    @InjectView(R.id.fragment_class_details_attendance_toggle)
    ViewGroup _toggleButton;

    @InjectView(R.id.fragment_class_details_attendance_toggle_icon)
    ImageView _toggleIcon;

    @InjectView(R.id.fragment_class_details_attendance_toggle_label)
    TextView _toggleLabel;

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

    private void _setAttendees() {
        int attendeeNb = _currentSchoolClass.getStudents().size();

        _attendeeCount.setVisibility(View.INVISIBLE);
        if (attendeeNb == 0) {
            _attendeeCount.setText(getString(R.string.no_attendee));
        } else if (attendeeNb == 1) {
            _attendeeCount.setText(getString(R.string.one_attendee));
        } else {
            _attendeeCount.setText(
                String.format(getString(R.string.multi_attendees), attendeeNb)
            );
        }
        _attendeeCount.setVisibility(View.VISIBLE);
        YoYo
            .with(Techniques.FadeIn)
            .duration(500)
            .playOn(_attendeeCount);
    }

    private void _setAttendanceToggle() {
        if (_isAttending) {
            _toggleButton.setBackgroundColor(getResources().getColor(R.color.red));
            Picasso
                .with(getActivity())
                .load(R.drawable.ic_unrsvp)
                .fit()
                .into(_toggleIcon);
            _toggleLabel.setText(getString(R.string.remove_attendance));
        } else {
            _toggleButton.setBackgroundColor(getResources().getColor(R.color.green));
            Picasso
                .with(getActivity())
                .load(R.drawable.ic_rsvp)
                .fit()
                .into(_toggleIcon);
            _toggleLabel.setText(getString(R.string.attend_class));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(R.layout.fragment_class_details, container, false);
        ButterKnife.inject(this, fragment);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (_initArgs != null) {
            EventBus.getDefault().postSticky(_initArgs);
        }
    }

    public void onEventMainThread(ClassDetailsInitEvent event) {
        _initArgs = event;

        BusinessFactory
            .provideLesson(getActivity())
            .getSchoolClassPair(
                event.getLessonSlug(),
                (schoolClass, teacher, venue) -> {
                    _currentSchoolClass = schoolClass;

                    _headline.setText(schoolClass.getLesson().getTitle());
                    _summary.setText(schoolClass.getLesson().getSummary());

                    PicassoHelper.loadAvatar(getActivity(), teacher, _avatar);
                    _teacher.setText(UserHelper.getDisplayedName(teacher));

                    _setAttendees();
                    _description.setText(schoolClass.getLesson().getDescription());
                },
                (newTeacher) -> {
                    _refreshContent(_teacher, UserHelper.getDisplayedName(newTeacher));
                    PicassoHelper.loadAvatar(getActivity(), newTeacher, _avatar);
                },
                (newVenue) -> {

                },
                this::publishError
            );

        BusinessFactory
            .provideUser(getActivity())
            .isCurrentUserAttendingTo(
                event.getLessonSlug(),
                (isAttending) -> {
                    _isAttending = isAttending;
                    _setAttendanceToggle();
                },
                this::publishError
            );
    }

    @OnClick(R.id.fragment_class_details_attendance_toggle)
    public void onAttendanceToggle(View view) {
        if (_currentSchoolClass == null) { // Prevent null pointer
            return;
        }
        _toggleIcon.setVisibility(View.GONE);
        _toggleLabel.setText(getString(R.string.processing));

        //TODO: error handling
        BusinessFactory
            .provideUser(getActivity())
            .toggleAttendance(
                _currentSchoolClass.getLesson().getSlug(),
                _isAttending,
                () -> {
                    _toggleIcon.setVisibility(View.VISIBLE);
                    _isAttending = !_isAttending;
                    _setAttendanceToggle();
                    _setAttendees();
                },
                this::publishError
            );
    }
}
