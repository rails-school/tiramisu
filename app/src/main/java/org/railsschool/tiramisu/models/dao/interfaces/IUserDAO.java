package org.railsschool.tiramisu.models.dao.interfaces;

import org.railsschool.tiramisu.models.beans.User;

/**
 * @class IUserDAO
 * @brief
 */
public interface IUserDAO {
    public boolean exists(int id);

    public User find(int id);

    public void create(User user);

    public void update(User user);
}
