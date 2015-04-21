package org.railsschool.tiramisu.views.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coshx.chocolatine.helpers.DeviceHelper;
import com.coshx.chocolatine.utils.actions.Action0;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.beans.Venue;
import org.railsschool.tiramisu.models.bll.BusinessFactory;
import org.railsschool.tiramisu.models.bll.structs.SchoolClass;
import org.railsschool.tiramisu.views.events.ClassDetailsInitEvent;
import org.railsschool.tiramisu.views.events.InformationEvent;
import org.railsschool.tiramisu.views.helpers.DateHelper;
import org.railsschool.tiramisu.views.helpers.PicassoHelper;
import org.railsschool.tiramisu.views.helpers.UserHelper;

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
    private Venue                 _currentVenue;
    private boolean               _isAttending;
    private boolean               _isTogglingAttendance;
    private boolean               _canToggleAttendance;

    @InjectView(R.id.fragment_class_details_headline)
    TextView _headline;

    @InjectView(R.id.fragment_class_details_summary)
    TextView _summary;

    @InjectView(R.id.fragment_class_details_calendar_label)
    TextView _date;

    @InjectView(R.id.fragment_class_details_location_label)
    TextView _location;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _isTogglingAttendance = false;
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
                    if (!isAdded()) {
                        return; // Prevent asynchronous conflicts
                    }

                    _currentSchoolClass = schoolClass;

                    _headline.setText(schoolClass.getLesson().getTitle());
                    _summary.setText(schoolClass.getLesson().getSummary());
                    _date.setText(
                        DateHelper.makeFriendly(schoolClass.getLesson().getStartTime())
                    );

                    _currentVenue = venue;
                    _location.setText(venue.getName());

                    PicassoHelper.loadAvatar(getActivity(), teacher, _avatar);
                    _teacher.setText(UserHelper.getDisplayedName(teacher));

                    _setAttendees();
                    _description.setText(schoolClass.getLesson().getDescription());
                },
                this::publishError
            );

        BusinessFactory
            .provideUser(getActivity())
            .isCurrentUserAttendingTo(
                event.getLessonSlug(),
                (isAttending) -> {
                    if (!isAdded()) {
                        return; // Prevent asynchronous conflicts
                    }

                    _isAttending = isAttending;
                    _canToggleAttendance = true;
                    _setAttendanceToggle();
                },
                () -> {
                    _canToggleAttendance = false;
                    _isAttending = false;
                    _setAttendanceToggle();
                    _toggleButton.setBackgroundColor(getResources().getColor(R.color.gray));
                },
                (error) -> {
                    publishError(error);
                    _isAttending = false;
                    _setAttendanceToggle();
                    _toggleButton.setBackgroundColor(getResources().getColor(R.color.gray));
                }
            );
    }

    @OnClick(R.id.fragment_class_details_attendance_toggle)
    public void onAttendanceToggle(View view) {
        final Action0 finallyAction;

        if (!_canToggleAttendance) { // Not signed in
            EventBus.getDefault()
                    .post(new InformationEvent(getString(R.string.error_not_signed_in)));
            return;
        }

        if (_currentSchoolClass == null) { // Prevent null pointer
            return;
        }

        if (_isTogglingAttendance) {
            return; // Lock similar operations when already performing
        }

        DeviceHelper.lockOrientation(getActivity());
        _isTogglingAttendance = true;
        finallyAction = () -> {
            _isTogglingAttendance = false;
            _toggleIcon.setVisibility(View.VISIBLE);
            _setAttendanceToggle();
            DeviceHelper.unlockOrientation(getActivity());
        };

        _toggleIcon.setVisibility(View.GONE);
        _toggleLabel.setText(getString(R.string.processing));

        BusinessFactory
            .provideUser(getActivity())
            .toggleAttendance(
                _currentSchoolClass.getLesson().getSlug(),
                _isAttending,
                () -> {
                    _isAttending = !_isAttending;
                    finallyAction.run();
                    _setAttendees();
                },
                (error) -> {
                    publishError(error);
                    finallyAction.run();
                }
            );
    }

    @OnClick(R.id.fragment_class_details_calendar)
    public void onAddToCalendar(View view) {
        Intent intent;

        if (_currentSchoolClass == null || _currentVenue == null) {
            return; // Prevent null exceptions
        }

        intent = new Intent(Intent.ACTION_EDIT);

        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(
            CalendarContract.EXTRA_EVENT_BEGIN_TIME,
            DateHelper.inMilliseconds(_currentSchoolClass.getLesson().getStartTime())
        );
        intent.putExtra(
            CalendarContract.EXTRA_EVENT_END_TIME,
            DateHelper.inMilliseconds(_currentSchoolClass.getLesson().getEndTime())
        );
        intent.putExtra(
            CalendarContract.Events.TITLE,
            _currentSchoolClass.getLesson().getTitle()
        );
        intent.putExtra(
            CalendarContract.Events.EVENT_LOCATION,
            _currentVenue.getName()
        );

        startActivity(intent);
    }

    @OnClick(R.id.fragment_class_details_location)
    public void onDirectionsRequested(View view) {
        Intent intent;

        if (_currentVenue == null) {
            return; // Prevent null exceptions
        }

        intent = new Intent(
            Intent.ACTION_VIEW,
            Uri.parse(
                "http://maps.google.com/maps?q=" +
                _currentVenue.getLatitude() + "," +
                _currentVenue.getLongitude()
            )
        );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
