package org.railsschool.tiramisu.models.bll;

import android.content.Context;

import com.coshx.chocolatine.utils.actions.Action;
import com.coshx.chocolatine.utils.actions.Action0;

import org.mindrot.jbcrypt.BCrypt;
import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.bll.interfaces.IUserBusiness;
import org.railsschool.tiramisu.models.bll.remote.interfaces.IRailsSchoolAPIOutlet;
import org.railsschool.tiramisu.models.dao.interfaces.IUserDAO;

import retrofit.Callback;
import retrofit.RetrofitError;
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
    public void get(int id, Action<User> success, Action<String> failure) {
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
    public void isCurrentUserAttendingTo(String lessonSlug, Action<Boolean> isAttending, Action0 needToSignIn, Action<String> failure) {
        if (!isSignedIn()) {
            needToSignIn.run();
        } else {
            tryConnecting(
                (api) -> {
                    api.isAttending(
                        lessonSlug,
                        _userDAO.getCurrentUsername(),
                        _userDAO.getCurrentUserToken(),
                        new BLLCallback<Boolean>(failure) {
                            @Override
                            public void success(Boolean value, Response response) {
                                isAttending.run(value);
                            }
                        }
                    );
                },
                failure
            );
        }

    }

    @Override
    public void toggleAttendance(String lessonSlug, boolean isAttending, Action0 success, Action<String> failure) {
        if (!isSignedIn()) {
            failure.run(getContext().getString(R.string.error_not_signed_in));
        } else {
            tryConnecting(
                (api) -> {
                    api.toggleAttendance(
                        lessonSlug,
                        _userDAO.getCurrentUsername(),
                        _userDAO.getCurrentUserToken(),
                        isAttending,
                        new BLLCallback<Void>(failure) {
                            @Override
                            public void success(Void aVoid, Response response) {
                                success.run();
                            }
                        }
                    );
                },
                failure
            );
        }
    }

    @Override
    public void checkCredentials(String username, String password, Action0 success, Action<String> failure) {
        tryConnecting(
            (api) -> {
                api.checkCredentials(
                    username,
                    BCrypt.hashpw(password, BCrypt.gensalt()), // Encrypt password
                    new Callback<String>() {
                        @Override
                        public void success(String token, Response response) {
                            _userDAO.setCurrentUsername(username);
                            _userDAO.setCurrentUserToken(token);

                            success.run();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            if (error.getResponse().getStatus() == 401) {
                                failure.run(
                                    getContext().getString(
                                        R.string.settings_invalid_credentials
                                    )
                                );
                            } else {
                                processError(error, failure);
                            }
                        }
                    }
                );
            },
            failure
        );
    }

    @Override
    public boolean isSignedIn() {
        return _userDAO.hasCurrentUser();
    }

    @Override
    public String getCurrentUsername() {
        return _userDAO.getCurrentUsername();
    }
}
