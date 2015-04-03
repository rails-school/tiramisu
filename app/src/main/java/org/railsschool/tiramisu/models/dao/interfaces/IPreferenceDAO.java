package org.railsschool.tiramisu.models.dao.interfaces;

import org.railsschool.tiramisu.models.dao.DayNotificationPreference;
import org.railsschool.tiramisu.models.dao.TwoHourNotificationPreference;

/**
 * @class IPreferenceDAO
 * @brief
 */
public interface IPreferenceDAO {
    public TwoHourNotificationPreference getTwoHourNotificationPreference();

    public void setTwoHourNotificationPreference(TwoHourNotificationPreference value);

    public DayNotificationPreference getDayNotificationPreference();

    public void setDayNotificationPreference(DayNotificationPreference value);
}
