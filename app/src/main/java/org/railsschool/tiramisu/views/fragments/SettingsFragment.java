package org.railsschool.tiramisu.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.bll.BusinessFactory;
import org.railsschool.tiramisu.models.dao.DayNotificationPreference;
import org.railsschool.tiramisu.models.dao.TwoHourNotificationPreference;
import org.railsschool.tiramisu.views.events.ConfirmationEvent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * @class SettingsFragment
 * @brief
 */
public class SettingsFragment extends BaseFragment {

    @InjectView(R.id.fragment_settings_two_day_reminder)
    Spinner _twoHourReminder;

    @InjectView(R.id.fragment_settings_day_reminder)
    Spinner _dayReminder;

    private boolean _twoHourReminderManuallySet;
    private boolean _dayReminderManuallySet;

    private void _setTwoHourReminderSpinner() {
        TwoHourNotificationPreference pref =
            BusinessFactory.providePreference(getActivity())
                           .getTwoHourReminderPreference();

        _twoHourReminderManuallySet = true;
        _twoHourReminder.setSelection(pref.toInt());
    }

    private void _setDayReminderSpinner() {
        DayNotificationPreference pref =
            BusinessFactory.providePreference(getActivity()).getDayReminderPreference();

        _dayReminderManuallySet = true;
        _dayReminder.setSelection(pref.toInt());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.inject(this, fragment);

        _twoHourReminderManuallySet = false;
        _dayReminderManuallySet = false;

        _twoHourReminder.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (_twoHourReminderManuallySet) {
                        _twoHourReminderManuallySet = false;
                        return;
                    }

                    BusinessFactory
                        .providePreference(getActivity())
                        .updateTwoHourReminderPreference(
                            TwoHourNotificationPreference
                                .fromInt(position)
                        );

                    EventBus.getDefault().post(
                        new ConfirmationEvent(getString(R.string.updated_preference))
                    );
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            }
        );

        _dayReminder.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (_dayReminderManuallySet) {
                        _dayReminderManuallySet = false;
                        return;
                    }
                    
                    BusinessFactory
                        .providePreference(getActivity())
                        .updateDayReminderPreference(
                            DayNotificationPreference
                                .fromInt(position)
                        );

                    EventBus.getDefault().post(
                        new ConfirmationEvent(getString(R.string.updated_preference))
                    );
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            }
        );

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        _setTwoHourReminderSpinner();
        _setDayReminderSpinner();
    }
}
