package org.railsschool.tiramisu.models.bll;

import android.content.Context;

import com.coshx.chocolatine.utils.actions.Action;

import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.bll.interfaces.IUserBusiness;
import org.railsschool.tiramisu.models.bll.remote.interfaces.IRailsSchoolAPIOutlet;

import retrofit.client.Response;

/**
 * @class UserBusiness
 * @brief
 */
class UserBusiness extends BaseBusiness implements IUserBusiness {
    public UserBusiness(Context context, IRailsSchoolAPIOutlet outlet) {
        super(context, outlet);
    }

    @Override
    public void find(int id, Action<User> success, Action<String> failure) {
        tryConnecting(
            (api) -> {
                api.getUser(
                    id,
                    new BLLCallback<User>(failure) {
                        @Override
                        public void success(User user, Response response) {
                            success.run(user);
                        }
                    }
                );
            },
            failure
        );
    }
}
