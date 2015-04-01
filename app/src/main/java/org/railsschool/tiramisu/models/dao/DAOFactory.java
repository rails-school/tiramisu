package org.railsschool.tiramisu.models.dao;

import android.content.Context;

import org.railsschool.tiramisu.models.dao.interfaces.ILessonDAO;
import org.railsschool.tiramisu.models.dao.interfaces.IUserDAO;

import io.realm.Realm;

/**
 * @class DAOFactory
 * @brief
 */
public class DAOFactory {
    private static final Object _userLock   = new Object();
    private static final Object _lessonLock = new Object();
    private static IUserDAO   _user;
    private static ILessonDAO _lesson;

    public static IUserDAO provideUser(Context context) {
        if (_user == null) {
            synchronized (_userLock) {
                if (_user == null) {
                    _user = new UserDAO(Realm.getInstance(context));
                }
            }
        }

        return _user;
    }

    public static ILessonDAO provideLesson(Context context) {
        if (_lesson == null) {
            synchronized (_lessonLock) {
                if (_lesson == null) {
                    _lesson = new LessonDAO(Realm.getInstance(context));
                }
            }
        }

        return _lesson;
    }
}