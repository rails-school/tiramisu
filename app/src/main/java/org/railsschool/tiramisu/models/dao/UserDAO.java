package org.railsschool.tiramisu.models.dao;

import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.dao.interfaces.IUserDAO;

import io.realm.Realm;

/**
 * @class UserDAO
 * @brief
 */
class UserDAO extends BaseDAO implements IUserDAO {
    public UserDAO(Realm dal) {
        super(dal);
    }

    @Override
    public boolean exists(int id) {
        return find(id) != null;
    }

    @Override
    public User find(int id) {
        return getDAL().where(User.class).equalTo("id", id).findFirst();
    }

    @Override
    public void create(User user) {
        getDAL().executeTransaction(
            (dal) -> {
                User entity = dal.copyToRealm(user);
            }
        );
    }

    @Override
    public void update(User user) {
        getDAL().executeTransaction(
            (dal) -> {
                User entity = dal.copyToRealm(user);
            }
        );
    }
}
