package org.railsschool.tiramisu.models.bll;

import android.content.Context;

import com.coshx.chocolatine.utils.actions.Action;

import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.bll.interfaces.IUserBusiness;
import org.railsschool.tiramisu.models.bll.remote.interfaces.IRailsSchoolAPIOutlet;
import org.railsschool.tiramisu.models.dao.interfaces.IUserDAO;

import retrofit.client.Response;

/**
 * @class UserBusiness
 * @brief
 */
class UserBusiness extends BaseBusiness implements IUserBusiness {
    private IUserDAO _userDAO;

    public UserBusiness(Context context, IRailsSchoolAPIOutlet outlet, IUserDAO userDAO) {
        super(context, outlet);

        this._userDAO = userDAO;
    }


    @Override
    public void find(int id, Action<User> success, Action<User> refresh, Action<String> failure) {
        if (_userDAO.exists(id)) {
            // Already an entry, use local data first
            success.run(_userDAO.find(id));

            // Then refresh them
            tryConnecting(
                (api) -> {
                    api.getUser(
                        id,
                        new BLLCallback<User>(failure) {
                            @Override
                            public void success(User user, Response response) {
                                refresh.run(user);
                                _userDAO.update(user);
                            }
                        }
                    );
                },
                failure
            );
        } else {
            // No entry in local storage, force to pull
            tryConnecting(
                (api) -> {
                    api.getUser(
                        id,
                        new BLLCallback<User>(failure) {
                            @Override
                            public void success(User user, Response response) {
                                success.run(user);
                                _userDAO.create(user);
                            }
                        }
                    );
                },
                failure
            );
        }
    }
}
