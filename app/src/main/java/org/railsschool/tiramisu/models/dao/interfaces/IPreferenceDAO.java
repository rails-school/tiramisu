package org.railsschool.tiramisu.models.dao.interfaces;

import org.railsschool.tiramisu.models.dao.DayNotificationPreference;
import org.railsschool.tiramisu.models.dao.TwoHourNotificationPreference;

/**
 * @class IPreferenceDAO
 * @brief
 */
public interface IPreferenceDAO {
    TwoHourNotificationPreference getTwoHourNotificationPreference();

    void setTwoHourNotificationPreference(TwoHourNotificationPreference value);

    DayNotificationPreference getDayNotificationPreference();

    void setDayNotificationPreference(DayNotificationPreference value);
}
