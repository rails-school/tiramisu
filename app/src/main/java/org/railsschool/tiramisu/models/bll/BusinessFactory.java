package org.railsschool.tiramisu.models.bll;

import android.content.Context;

import org.railsschool.tiramisu.models.bll.interfaces.ILessonBusiness;
import org.railsschool.tiramisu.models.bll.interfaces.IPreferenceBusiness;
import org.railsschool.tiramisu.models.bll.interfaces.IUserBusiness;
import org.railsschool.tiramisu.models.bll.interfaces.IVenueBusiness;
import org.railsschool.tiramisu.models.bll.remote.RailsSchoolAPIOutletFactory;
import org.railsschool.tiramisu.models.dao.DAOFactory;

/**
 * @class BusinessFactory
 * @brief
 */
public class BusinessFactory {
    private static IUserBusiness       _user;
    private static ILessonBusiness     _lesson;
    private static IVenueBusiness      _venue;
    private static IPreferenceBusiness _preference;

    public static IUserBusiness provideUser(Context context) {
        if (_user == null) {
            _user = new UserBusiness(
                context,
                RailsSchoolAPIOutletFactory.provide(context),
                DAOFactory.provideUser(context)
            );
        }

        return _user;
    }

    public static ILessonBusiness provideLesson(Context context) {
        if (_lesson == null) {
            _lesson = new LessonBusiness(
                context,
                RailsSchoolAPIOutletFactory.provide(context),
                provideUser(context),
                provideVenue(context),
                DAOFactory.provideLesson(context)
            );
        }

        return _lesson;
    }

    public static IVenueBusiness provideVenue(Context context) {
        if (_venue == null) {
            _venue = new VenueBusiness(
                context,
                RailsSchoolAPIOutletFactory.provide(context),
                DAOFactory.provideVenue(context)
            );
        }

        return _venue;
    }

    public static IPreferenceBusiness providePreference(Context context) {
        if (_preference == null) {
            _preference = new PreferenceBusiness(DAOFactory.providePreference(context));
        }

        return _preference;
    }
}
