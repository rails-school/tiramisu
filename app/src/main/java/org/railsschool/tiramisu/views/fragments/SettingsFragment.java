package org.railsschool.tiramisu.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;

import com.coshx.chocolatine.utils.actions.Action0;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.bll.BusinessFactory;
import org.railsschool.tiramisu.models.dao.DayNotificationPreference;
import org.railsschool.tiramisu.models.dao.TwoHourNotificationPreference;
import org.railsschool.tiramisu.views.events.ConfirmationEvent;
import org.railsschool.tiramisu.views.events.InformationEvent;
import org.railsschool.tiramisu.views.helpers.KeyboardHelper;
import org.railsschool.tiramisu.views.patterns.ReminderSeekbarLabelPattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * @class SettingsFragment
 * @brief
 */
public class SettingsFragment extends BaseFragment {

    @InjectView(R.id.fragment_settings_email)
    EditText _emailField;

    @InjectView(R.id.fragment_settings_password)
    EditText _passwordField;

    @InjectView(R.id.fragment_settings_submit_credentials)
    Button _submitButton;

    @InjectView(R.id.fragment_settings_log_out)
    Button _logOutButton;

    @InjectView(R.id.fragment_settings_two_hour_reminder)
    SeekBar _twoHourReminderBar;

    @InjectView(R.id.fragment_settings_day_reminder)
    SeekBar _dayReminderBar;

    @InjectView(R.id.fragment_settings_lesson_alert)
    Switch _lessonAlertSwitch;

    // Avoids asynchronous conflicts (spinner touched again before
    // callback has been triggered)
    private boolean _isCurrentlySettingTwoHourReminder;
    private boolean _isCurrentlySettingDayReminder;
    private boolean _isCurrentlySettingLessonAlert;

    private boolean _isProcessingCredentials;

    private ReminderSeekbarLabelPattern _twoHourSeekbarLabels;
    private ReminderSeekbarLabelPattern _daySeekbarLabels;

    private void _setTwoHourReminderSeekBar() {
        TwoHourNotificationPreference pref =
            BusinessFactory.providePreference(getActivity())
                           .getTwoHourReminderPreference();

        _twoHourReminderBar.setProgress(pref.toInt());
        _twoHourSeekbarLabels.update(pref);
    }

    private void _setDayReminderSeekBar() {
        DayNotificationPreference pref =
            BusinessFactory.providePreference(getActivity()).getDayReminderPreference();

        _dayReminderBar.setProgress(pref.toInt());
        _daySeekbarLabels.update(pref);
    }

    private void _setLessonAlertSwitch() {
        boolean pref = BusinessFactory.providePreference(getActivity()).getLessonAlertPreference();

        _lessonAlertSwitch.setChecked(pref);
    }

    private void _setCredentials() {
        if (BusinessFactory.provideUser(getActivity()).isSignedIn()) {
            // Sets "display" credentials if user has already signed in
            _emailField.setText(
                BusinessFactory.provideUser(getActivity()).getCurrentUserEmail()
            );
            _passwordField.setText(getString(R.string.app_name));
            _submitButton.setVisibility(View.GONE);
            _logOutButton.setVisibility(View.VISIBLE);
        } else {
            _submitButton.setVisibility(View.VISIBLE);
            _logOutButton.setVisibility(View.GONE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.inject(this, fragment);

        _twoHourSeekbarLabels = new ReminderSeekbarLabelPattern(
            getActivity(), fragment.findViewById(R.id.fragment_settings_two_hour_reminder_labels)
        );
        _daySeekbarLabels = new ReminderSeekbarLabelPattern(
            getActivity(), fragment.findViewById(R.id.fragment_settings_day_reminder_labels)
        );

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        _isCurrentlySettingTwoHourReminder = false;
        _isCurrentlySettingDayReminder = false;
        _isCurrentlySettingLessonAlert = false;

        // On resume, set spinner values from existing value in DB
        _setTwoHourReminderSeekBar();
        _setDayReminderSeekBar();
        _setLessonAlertSwitch();

        _setCredentials();

        // Set two hour spinner listener
        _twoHourReminderBar.setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    TwoHourNotificationPreference pref;

                    if (_isCurrentlySettingTwoHourReminder) {
                        // Prevent similar operations
                        // Possible for asynchronous operation needed below
                        return;
                    }

                    _isCurrentlySettingTwoHourReminder = true;
                    pref = TwoHourNotificationPreference.fromInt(seekBar.getProgress());
                    _twoHourSeekbarLabels.update(pref);
                    BusinessFactory
                        .providePreference(getActivity())
                        .updateTwoHourReminderPreference(pref);

                    _isCurrentlySettingTwoHourReminder = false;
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            }
        );

        _dayReminderBar.setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    DayNotificationPreference pref;

                    if (_isCurrentlySettingDayReminder) {
                        // Prevent similar operations
                        // Possible for asynchronous operation needed below
                        return;
                    }

                    _isCurrentlySettingDayReminder = true;
                    pref = DayNotificationPreference.fromInt(seekBar.getProgress());
                    _daySeekbarLabels.update(pref);
                    BusinessFactory
                        .providePreference(getActivity())
                        .updateDayReminderPreference(pref);

                    _isCurrentlySettingDayReminder = false;
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            }
        );

        _lessonAlertSwitch.setOnCheckedChangeListener(
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (_isCurrentlySettingLessonAlert) {
                        // Prevent similar operations
                        // Possible for asynchronous operation needed below
                        return;
                    }

                    _isCurrentlySettingLessonAlert = true;
                    BusinessFactory
                        .providePreference(getActivity())
                        .updateLessonAlertPreference(buttonView.isChecked());
                    _isCurrentlySettingLessonAlert = false;
                }
            }
        );
    }

    @Override
    public void onPause() {
        String email, password;
        super.onPause();

        _twoHourReminderBar.setOnSeekBarChangeListener(null);
        _dayReminderBar.setOnSeekBarChangeListener(null);
        _lessonAlertSwitch.setOnCheckedChangeListener(null);
    }

    @OnClick(R.id.fragment_settings_submit_credentials)
    public void onCredentialsSave(View view) {
        Action0 finallyCallback;

        if (_isProcessingCredentials) { // Operation already in progress
            return;
        }

        _isProcessingCredentials = true;
        EventBus.getDefault().post(new InformationEvent(getString(R.string.processing)));
        _submitButton.setBackgroundColor(getResources().getColor(R.color.white));
        _submitButton.setTextColor(getResources().getColor(R.color.green));

        finallyCallback = () -> {
            _isProcessingCredentials = false;
            _submitButton.setBackgroundColor(getResources().getColor(R.color.green));
            _submitButton.setTextColor(getResources().getColor(R.color.white));
        };

        BusinessFactory
            .provideUser(getActivity())
            .checkCredentials(
                _emailField.getText().toString(),
                _passwordField.getText().toString(),
                () -> {
                    EventBus.getDefault().post(
                        new ConfirmationEvent(getString(R.string.saved_confirmation))
                    );
                    KeyboardHelper.hide(getActivity());
                    _submitButton.setVisibility(View.GONE);
                    _logOutButton.setVisibility(View.VISIBLE);

                    finallyCallback.run();
                },
                (error) -> {
                    publishError(error);
                    finallyCallback.run();
                }
            );
    }

    @OnClick(R.id.fragment_settings_log_out)
    public void onLogout(View view) {
        Action0 finallyCallback;

        if (_isProcessingCredentials) { // Operation already in progress
            return;
        }

        _isProcessingCredentials = true;
        EventBus.getDefault().post(new InformationEvent(getString(R.string.processing)));
        _logOutButton.setBackgroundColor(getResources().getColor(R.color.white));
        _logOutButton.setTextColor(getResources().getColor(R.color.red));

        finallyCallback = () -> {
            _isProcessingCredentials = false;
            _logOutButton.setBackgroundColor(getResources().getColor(R.color.red));
            _logOutButton.setTextColor(getResources().getColor(R.color.white));
        };

        BusinessFactory
            .provideUser(getActivity())
            .logOut(
                () -> {
                    EventBus.getDefault().post(
                        new ConfirmationEvent(getString(R.string.log_out_confirmation))
                    );
                    _submitButton.setVisibility(View.VISIBLE);
                    _logOutButton.setVisibility(View.GONE);
                    _emailField.setText(null);
                    _passwordField.setText(null);

                    finallyCallback.run();
                },
                (error) -> {
                    publishError(error);
                    finallyCallback.run();
                }
            );
    }
}
