package org.railsschool.tiramisu.models.bll.interfaces;

import org.railsschool.tiramisu.models.dao.DayNotificationPreference;
import org.railsschool.tiramisu.models.dao.TwoHourNotificationPreference;

/**
 * @class IPreferenceBusiness
 * @brief
 */
public interface IPreferenceBusiness {
    void updateTwoHourReminderPreference(TwoHourNotificationPreference preference);

    TwoHourNotificationPreference getTwoHourReminderPreference();

    void updateDayReminderPreference(DayNotificationPreference preference);

    DayNotificationPreference getDayReminderPreference();
}
