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
        User u;

        getDAL().beginTransaction();
        u = getDAL().where(User.class).equalTo("id", id).findFirst();
        getDAL().commitTransaction();

        return u;
    }

    @Override
    public void create(User user) {
        getDAL().executeTransaction(
            (dal) -> {
                dal.copyToRealm(user);
            }
        );
    }

    @Override
    public void update(User user) {
        delete(user);
        create(user);
    }

    public void delete(User user) {
        getDAL().executeTransaction(
            (dal) -> {
                find(user.getId()).removeFromRealm();
            }
        );
    }
}
