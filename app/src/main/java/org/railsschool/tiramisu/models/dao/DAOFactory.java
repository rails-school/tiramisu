package org.railsschool.tiramisu.models.dao;

import org.railsschool.tiramisu.models.dao.interfaces.ILessonDAO;
import org.railsschool.tiramisu.models.dao.interfaces.IUserDAO;

/**
 * @class DAOFactory
 * @brief
 */
public class DAOFactory {
    public static IUserDAO provideUser() {
        return null;
    }

    public static ILessonDAO provideLesson() {
        return null;
    }
}
