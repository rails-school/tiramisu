package org.railsschool.tiramisu.models.bll.interfaces;

import com.coshx.chocolatine.utils.actions.Action;
import com.coshx.chocolatine.utils.actions.Action0;

import org.railsschool.tiramisu.models.beans.User;

/**
 * @class IUserBusiness
 * @brief
 */
public interface IUserBusiness {
    public void get(int id, Action<User> success, Action<String> failure);

    public void isCurrentUserAttendingTo(String lessonSlug, Action<Boolean> isAttending,
                                         Action0 needToSignIn,
                                         Action<String> failure);

    public void toggleAttendance(String lessonSlug, boolean isAttending,
                                 Action0 success, Action<String> failure);

    public void checkCredentials(String username, String password, Action0 success,
                                 Action<String> failure);

    public boolean isSignedIn();

    public String getCurrentUsername();
}
