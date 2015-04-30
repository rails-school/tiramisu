package org.railsschool.tiramisu.models.dao.interfaces;

import org.railsschool.tiramisu.models.beans.User;

/**
 * @class IUserDAO
 * @brief
 */
public interface IUserDAO {
    boolean exists(int id);

    User find(int id);

    void save(User user);

    String getCurrentUserEmail();

    void setCurrentUserEmail(String value);

    String getCurrentUserToken();

    void setCurrentUserToken(String value);

    int getCurrentUserSchoolId();

    void setCurrentUserSchoolId(int value);

    boolean hasCurrentUser();
}
