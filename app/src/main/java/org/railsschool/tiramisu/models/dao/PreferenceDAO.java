package org.railsschool.tiramisu.models.dao;

import android.content.Context;
import android.content.SharedPreferences;

import org.railsschool.tiramisu.models.dao.interfaces.IPreferenceDAO;

/**
 * @class PreferenceDAO
 * @brief
 */
class PreferenceDAO implements IPreferenceDAO {
    private final static String FILE_NAME = "preferences",
        TWO_HOUR_NOTIFICATION_KEY         = "two_hour_notification",
        DAY_NOTIFICATION_KEY              = "day_notification",
        LESSON_ALERT_KEY                  = "lesson_alert";

    private SharedPreferences _dal;

    public PreferenceDAO(Context context) {
        this._dal = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public TwoHourNotificationPreference getTwoHourNotificationPreference() {
        int pref = _dal.getInt(TWO_HOUR_NOTIFICATION_KEY, -1);

        return TwoHourNotificationPreference.fromInt(pref);
    }

    @Override
    public void setTwoHourNotificationPreference(TwoHourNotificationPreference value) {
        _dal.edit().putInt(TWO_HOUR_NOTIFICATION_KEY, value.toInt()).commit();
    }

    @Override
    public DayNotificationPreference getDayNotificationPreference() {
        int pref = _dal.getInt(DAY_NOTIFICATION_KEY, -1);

        return DayNotificationPreference.fromInt(pref);
    }

    @Override
    public void setDayNotificationPreference(DayNotificationPreference value) {
        _dal.edit().putInt(DAY_NOTIFICATION_KEY, value.toInt()).commit();
    }

    @Override
    public boolean getLessonAlertPreference() {
        return _dal.getBoolean(LESSON_ALERT_KEY, true);
    }

    @Override
    public void setLessonAlertPreference(boolean value) {
        _dal.edit().putBoolean(LESSON_ALERT_KEY, value).commit();
    }
}
