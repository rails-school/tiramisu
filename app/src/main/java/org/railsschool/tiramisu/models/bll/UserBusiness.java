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
                    if (isAttending) {
                        api.attend(
                            lessonId,
                            new BLLCallback<Void>(failure) {
                                @Override
                                public void success(Void aVoid, Response response) {
                                    success.run();
                                }
                            }
                        );
                    } else {
                        api.removeAttendance(
                            lessonId,
                            new BLLCallback<Void>(failure) {
                                @Override
                                public void success(Void aVoid, Response response) {
                                    success.run();
                                }
                            }
                        );
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
                    new Callback<Void>() {
                        @Override
                        public void success(Void aVoid, Response response) {
                            String authenticationCookie = null;

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
                                    } else {
                                        Log.e(
                                            UserBusiness.class.getSimpleName(),
                                            "Cookies were all present but expected one"
                                        );
                                        failure.run(getDefaultErrorMsg());

                                        return;
                                    }
                                }
                            }

                            if (authenticationCookie != null) {
                                _userDAO.setCurrentUserEmail(email);
                                _userDAO.setCurrentUserToken(authenticationCookie);
                                success.run();
                            } else {
                                Log.e(
                                    UserBusiness.class.getSimpleName(),
                                    "No cookie"
                                );
                                failure.run(getDefaultErrorMsg());
                            }
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
    public String getCurrentUserEmail() {
        return _userDAO.getCurrentUserEmail();
    }
}
