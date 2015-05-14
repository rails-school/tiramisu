package org.railsschool.tiramisu.models.dao;

import android.content.Context;
import android.content.SharedPreferences;

import org.joda.time.DateTime;
import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.dao.interfaces.IUserDAO;

import io.realm.Realm;

/**
 * @class UserDAO
 * @brief
 */
class UserDAO extends BaseDAO implements IUserDAO {
    private final static Object _saveLock = new Object();

    /**
     * Token and username are both stored in preferences
     */
    private final static String FILE_NAME = "current_user",
        EMAIL_KEY                         = "email",
        SCHOOL_ID_KEY                     = "school_id",
        TOKEN_KEY                         = "token";
    private SharedPreferences _preferenceDAL;

    public UserDAO(Realm dal, Context context) {
        super(dal);
        this._preferenceDAL = context.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        );
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

    @Override
    public String getCurrentUserEmail() {
        return _preferenceDAL.getString(EMAIL_KEY, null);
    }

    @Override
    public void setCurrentUserEmail(String value) {
        _preferenceDAL.edit().putString(EMAIL_KEY, value).commit();
    }

    @Override
    public String getCurrentUserToken() {
        return _preferenceDAL.getString(TOKEN_KEY, null);
    }

    @Override
    public void setCurrentUserToken(String value) {
        _preferenceDAL.edit().putString(TOKEN_KEY, value).commit();
    }

    @Override
    public int getCurrentUserSchoolId() {
        return _preferenceDAL.getInt(SCHOOL_ID_KEY, 0);
    }

    @Override
    public void setCurrentUserSchoolId(int value) {
        _preferenceDAL.edit().putInt(SCHOOL_ID_KEY, value).commit();
    }

    @Override
    public boolean hasCurrentUser() {
        return getCurrentUserEmail() != null && getCurrentUserToken() != null;
    }

    public void create(User user) {
        getDAL().executeTransaction(
            (dal) -> {
                user.setUpdateDate(DateTime.now().toDate());
                dal.copyToRealm(user);
            }
        );
    }

    public void update(User user) {
        getDAL().executeTransaction(
            (dal) -> {
                User entry = find(user.getId());

                // Optional fields from server. If they are blank, do not update local entry.
                if (user.getName() != null && !user.getName().isEmpty()) {
                    entry.setName(user.getName());
                }
                if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                    entry.setEmail(user.getEmail());
                }

                entry.setHideLastName(user.getHideLastName());
                entry.setUpdateDate(DateTime.now().toDate());
            }
        );
    }
}
