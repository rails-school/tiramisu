package org.railsschool.tiramisu.models.bll;

import android.content.Context;
import android.os.Handler;

import com.coshx.chocolatine.utils.actions.Action;
import com.coshx.chocolatine.utils.actions.Action0;

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
    public void find(int id, Action<User> success, Action<String> failure) {
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
                                _userDAO.save(user);
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
                                _userDAO.save(user);
                            }
                        }
                    );
                },
                failure
            );
        }
    }

    @Override
    public void isCurrentUserAttendingTo(String lessonSlug, Action<Boolean> success, Action<String> failure) {
        //TODO
        success.run(false);
    }

    @Override
    public void toggleAttendance(String lessonSlug, boolean isAttending, Action0 success, Action<String> failure) {
        //TODO
        new Handler().postDelayed(() -> success.run(), 2000);
    }
}
