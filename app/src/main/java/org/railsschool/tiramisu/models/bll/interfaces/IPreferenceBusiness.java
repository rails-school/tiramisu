package org.railsschool.tiramisu.models.bll.interfaces;

import org.railsschool.tiramisu.models.dao.DayNotificationPreference;
import org.railsschool.tiramisu.models.dao.TwoHourNotificationPreference;

/**
 * @class IPreferenceBusiness
 * @brief
 */
public interface IPreferenceBusiness {
    void updateTwoHourReminderPreference(TwoHourNotificationPreference preference);

    public TwoHourNotificationPreference getTwoHourReminderPreference();

    public void updateDayReminderPreference(DayNotificationPreference preference);

    public DayNotificationPreference getDayReminderPreference();
}
