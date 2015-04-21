package org.railsschool.tiramisu.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.coshx.chocolatine.helpers.DeviceHelper;
import com.coshx.chocolatine.utils.actions.Action0;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.bll.BusinessFactory;
import org.railsschool.tiramisu.models.dao.DayNotificationPreference;
import org.railsschool.tiramisu.models.dao.TwoHourNotificationPreference;
import org.railsschool.tiramisu.views.events.ConfirmationEvent;
import org.railsschool.tiramisu.views.events.InformationEvent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * @class SettingsFragment
 * @brief
 */
public class SettingsFragment extends BaseFragment {

    private static class RestoreCredentialInputEvent {
        private String _username;
        private String _password;

        public RestoreCredentialInputEvent(String username, String password) {
            this._username = username;
            this._password = password;
        }

        public String getUsername() {
            return _username;
        }

        public String getPassword() {
            return _password;
        }
    }

    @InjectView(R.id.fragment_settings_username)
    EditText _usernameField;

    @InjectView(R.id.fragment_settings_password)
    EditText _passwordField;

    @InjectView(R.id.fragment_settings_submit_credentials)
    Button _submitButton;

    @InjectView(R.id.fragment_settings_two_day_reminder)
    Spinner _twoHourReminderSpinner;

    @InjectView(R.id.fragment_settings_day_reminder)
    Spinner _dayReminderSpinner;

    // These booleans prevent database update when resuming fragment
    private boolean _twoHourReminderManuallySet;
    private boolean _dayReminderManuallySet;

    // Avoids asynchronous conflicts (spinner touched again before
    // callback has been triggered)
    private boolean _isCurrentlySettingTwoHourReminder;
    private boolean _isCurrentlySettingDayReminder;

    private boolean _isProcessingCredentials;

    private void _setTwoHourReminderSpinner() {
        TwoHourNotificationPreference pref =
            BusinessFactory.providePreference(getActivity())
                           .getTwoHourReminderPreference();

        _twoHourReminderManuallySet = true;
        _twoHourReminderSpinner.setSelection(pref.toInt());
    }

    private void _setDayReminderSpinner() {
        DayNotificationPreference pref =
            BusinessFactory.providePreference(getActivity()).getDayReminderPreference();

        _dayReminderManuallySet = true;
        _dayReminderSpinner.setSelection(pref.toInt());
    }

    private void _setCredentials() {
        if (BusinessFactory.provideUser(getActivity()).isSignedIn()) {
            _usernameField.setText(
                BusinessFactory.provideUser(getActivity()).getCurrentUsername()
            );

            _passwordField.setText(getString(R.string.app_name));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.inject(this, fragment);

        // Set two hour spinner listener
        _twoHourReminderSpinner.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (_twoHourReminderManuallySet) {
                        // Event has been triggered for value has been set manually.
                        // Do not update DB
                        _twoHourReminderManuallySet = false;
                        return;
                    }

                    if (_isCurrentlySettingTwoHourReminder) {
                        // Prevent similar operations
                        // Possible for asynchronous operation needed below
                        return;
                    }

                    DeviceHelper.lockOrientation(getActivity());
                    _isCurrentlySettingTwoHourReminder = true;
                    BusinessFactory
                        .providePreference(getActivity())
                        .updateTwoHourReminderPreference(
                            TwoHourNotificationPreference
                                .fromInt(position)
                        );
                    DeviceHelper.unlockOrientation(getActivity());
                    _isCurrentlySettingTwoHourReminder = false;

                    EventBus.getDefault().post(
                        new ConfirmationEvent(getString(R.string.updated_preference))
                    );
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Nothing selected, nothing to do
                }
            }
        );

        _dayReminderSpinner.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (_dayReminderManuallySet) {
                        // Event has been triggered for value has been set manually.
                        // Do not update DB
                        _dayReminderManuallySet = false;
                        return;
                    }

                    if (_isCurrentlySettingDayReminder) {
                        // Prevent similar operations
                        // Possible for asynchronous operation needed below
                        return;
                    }

                    DeviceHelper.lockOrientation(getActivity());
                    _isCurrentlySettingDayReminder = true;
                    BusinessFactory
                        .providePreference(getActivity())
                        .updateDayReminderPreference(
                            DayNotificationPreference
                                .fromInt(position)
                        );
                    DeviceHelper.unlockOrientation(getActivity());
                    _isCurrentlySettingDayReminder = false;

                    EventBus.getDefault().post(
                        new ConfirmationEvent(getString(R.string.updated_preference))
                    );
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Nothing selected, nothing to do
                }
            }
        );

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        _twoHourReminderManuallySet = false;
        _dayReminderManuallySet = false;

        _isCurrentlySettingTwoHourReminder = false;
        _isCurrentlySettingDayReminder = false;

        // On resume, set spinner values from existing value in DB
        _setTwoHourReminderSpinner();
        _setDayReminderSpinner();

        _setCredentials();

        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onPause() {
        String username, password;
        super.onPause();

        EventBus.getDefault().unregister(this);

        // Before pausing, save input if any
        username = _usernameField.getText().toString();
        password = _passwordField.getText().toString();
        if (username != null && !username.isEmpty() && password != null &&
            !password.isEmpty()) {
            EventBus.getDefault().postSticky(
                new RestoreCredentialInputEvent(
                    username,
                    password
                )
            );
        }
    }

    public void onEventMainThread(RestoreCredentialInputEvent event) {
        _usernameField.setText(event.getUsername());
        _passwordField.setText(event.getPassword());
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
            DeviceHelper.unlockOrientation(getActivity());
        };

        DeviceHelper.lockOrientation(getActivity());
        BusinessFactory
            .provideUser(getActivity())
            .checkCredentials(
                _usernameField.getText().toString(),
                _passwordField.getText().toString(),
                () -> {
                    EventBus.getDefault().post(
                        new ConfirmationEvent(getString(R.string.saved_confirmation))
                    );

                    finallyCallback.run();
                },
                (error) -> {
                    publishError(error);
                    finallyCallback.run();
                }
            );
    }
}
