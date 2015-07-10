package org.railsschool.tiramisu.models.bll;

import android.content.Context;
import android.util.Log;

import com.coshx.chocolatine.utils.actions.Action;
import com.coshx.chocolatine.utils.actions.Action0;

import org.joda.time.DateTime;
import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.bll.interfaces.IUserBusiness;
import org.railsschool.tiramisu.models.bll.remote.interfaces.IRailsSchoolAPIOutlet;
import org.railsschool.tiramisu.models.bll.structs.CheckCredentialsRequest;
import org.railsschool.tiramisu.models.dao.interfaces.IUserDAO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

/**
 * @class UserBusiness
 * @brief
 */
class UserBusiness extends BaseBusiness implements IUserBusiness {
    /**
     * Cooldown before refreshing user again
     */
    private final static int COOLDOWN_MS = 5 * 60 * 1000;

    private IUserDAO _userDAO;

    public UserBusiness(Context context, IRailsSchoolAPIOutlet outlet, IUserDAO userDAO) {
        super(context, outlet);

        this._userDAO = userDAO;
    }

    @Override
    public void get(int id, Action<User> success, Action<String> failure) {
        if (_userDAO.exists(id)) {
            User u = _userDAO.find(id);

            // Already an entry, use local data first
            success.run(u);

            if (DateTime.now().minus(u.getUpdateDate().getTime()).getMillis() >=
                COOLDOWN_MS) {
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
            }
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
                _userDAO.getCurrentUserToken(),
                (api) -> {
                    api.isAttending(
                        lessonSlug,
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
    public void toggleAttendance(int lessonId, boolean isAttending, Action0 success,
                                 Action<String> failure) {
        if (!isSignedIn()) {
            failure.run(getContext().getString(R.string.error_not_signed_in));
        } else {
            tryConnecting(
                _userDAO.getCurrentUserToken(),
                (api) -> {
                    BLLCallback<Void> callback = new BLLCallback<Void>(failure) {
                        @Override
                        public void success(Void aVoid, Response response) {
                            success.run();
                        }
                    };

                    if (isAttending) {
                        api.attend(lessonId, callback);
                    } else {
                        api.removeAttendance(lessonId, callback);
                    }
                },
                failure
            );
        }
    }

    @Override
    public void checkCredentials(String email, String password, Action0 success,
                                 Action<String> failure) {
        tryConnecting(
            (api) -> {
                api.checkCredentials(
                    new CheckCredentialsRequest(
                        email,
                        password // Unencrypted password for HTTPS connection
                    ),
                    new Callback<User>() {
                        @Override
                        public void success(User user, Response response) {
                            String authenticationCookie = null;

                            // Let's find the authentication cookie among headers
                            for (int i = 0, size = response.getHeaders().size();
                                 i < size && authenticationCookie == null; i++) {
                                Header h = response.getHeaders().get(i);

                                if (h.getName() != null && h.getName().equals("Set-Cookie")) {
                                    Pattern p = Pattern.compile(
                                        "remember_user_token=(.+)"
                                    );
                                    Matcher m = p.matcher(h.getValue());

                                    if (m.matches()) {
                                        authenticationCookie = m.group(1);
                                    }
                                }
                            }

                            if (authenticationCookie != null) {
                                _userDAO.setCurrentUserEmail(email);
                                _userDAO.setCurrentUserToken(authenticationCookie);
                                _userDAO.setCurrentUserSchoolId(user.getSchoolId());
                                success.run();
                            } else {
                                Log.e(
                                    UserBusiness.class.getSimpleName(),
                                    "Expected cookie was not found"
                                );
                                failure.run(getDefaultErrorMsg());
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            if (error.getResponse() != null &&
                                error.getResponse().getStatus() == 401) {
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

    public void logOut(Action0 success, Action<String> failure) {
        tryConnecting(
            (api) -> {
                BLLCallback<Void> callback = new BLLCallback<Void>(failure) {
                    @Override
                    public void success(Void aVoid, Response response) {
                        _userDAO.logOut();
                        success.run();
                    }
                };
                api.logOut(callback);
            },
            failure
        );
    }

    @Override
    public boolean isSignedIn() {
        return _userDAO.hasCurrentUser();
    }

    @Override
    public String getCurrentUserEmail() {
        return _userDAO.getCurrentUserEmail();
    }

    @Override
    public int getCurrentUserSchoolId() {
        return _userDAO.getCurrentUserSchoolId();
    }

    @Override
    public void cleanDatabase() {
        _userDAO.truncateTable();
    }
}
