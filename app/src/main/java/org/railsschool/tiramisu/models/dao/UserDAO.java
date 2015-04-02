package org.railsschool.tiramisu.models.dao;

import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.dao.interfaces.IUserDAO;

import io.realm.Realm;

/**
 * @class UserDAO
 * @brief
 */
class UserDAO extends BaseDAO implements IUserDAO {
    private final static Object _saveLock = new Object();

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
    public void save(User user) {
        synchronized (_saveLock) {
            if (exists(user.getId())) {
                update(user);
            } else {
                create(user);
            }
        }
    }

    public void create(User user) {
        getDAL().executeTransaction(
            (dal) -> {
                dal.copyToRealm(user);
            }
        );
    }

    public void update(User user) {
        getDAL().executeTransaction(
            (dal) -> {
                User entry = find(user.getId());

                entry.setName(user.getName());
                entry.setEmail(user.getEmail());
                entry.setTeacher(user.getTeacher());
                entry.setHideLastName(user.getHideLastName());
            }
        );
    }

    public void delete(User user) {
        getDAL().executeTransaction(
            (dal) -> {
                find(user.getId()).removeFromRealm();
            }
        );
    }
}
