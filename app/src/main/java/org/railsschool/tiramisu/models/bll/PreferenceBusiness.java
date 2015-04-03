package org.railsschool.tiramisu.models.bll;

import org.railsschool.tiramisu.models.bll.interfaces.IPreferenceBusiness;
import org.railsschool.tiramisu.models.dao.DayNotificationPreference;
import org.railsschool.tiramisu.models.dao.TwoHourNotificationPreference;
import org.railsschool.tiramisu.models.dao.interfaces.IPreferenceDAO;

/**
 * @class PreferenceBusiness
 * @brief
 */
class PreferenceBusiness implements IPreferenceBusiness {
    private IPreferenceDAO _prefDAO;

    public PreferenceBusiness(IPreferenceDAO prefDAO) {
        this._prefDAO = prefDAO;
    }

    @Override
    public void updateTwoHourReminderPreference(TwoHourNotificationPreference preference) {
        _prefDAO.setTwoHourNotificationPreference(preference);
    }

    @Override
    public TwoHourNotificationPreference getTwoHourReminderPreference() {
        return _prefDAO.getTwoHourNotificationPreference();
    }

    @Override
    public void updateDayReminderPreference(DayNotificationPreference preference) {
        _prefDAO.setDayNotificationPreference(preference);
    }

    @Override
    public DayNotificationPreference getDayReminderPreference() {
        return _prefDAO.getDayNotificationPreference();
    }
}
