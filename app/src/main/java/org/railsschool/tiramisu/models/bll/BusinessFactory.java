package org.railsschool.tiramisu.models.bll;

import android.content.Context;

import org.railsschool.tiramisu.models.bll.interfaces.ILessonBusiness;
import org.railsschool.tiramisu.models.bll.interfaces.IUserBusiness;
import org.railsschool.tiramisu.models.bll.remote.RailsSchoolAPIOutletFactory;
import org.railsschool.tiramisu.models.dao.DAOFactory;

/**
 * @class BusinessFactory
 * @brief
 */
public class BusinessFactory {
    private static IUserBusiness   _user;
    private static ILessonBusiness _lesson;

    public static IUserBusiness provideUser(Context context) {
        if (_user == null) {
            _user = new UserBusiness(
                context,
                RailsSchoolAPIOutletFactory.provide(context),
                DAOFactory.provideUser()
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
                DAOFactory.provideLesson()
            );
        }

        return _lesson;
    }
}
