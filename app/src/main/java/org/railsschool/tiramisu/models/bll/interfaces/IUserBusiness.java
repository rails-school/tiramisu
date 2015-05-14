package org.railsschool.tiramisu.models.bll.interfaces;

import com.coshx.chocolatine.utils.actions.Action;
import com.coshx.chocolatine.utils.actions.Action0;

import org.railsschool.tiramisu.models.beans.User;

/**
 * @class IUserBusiness
 * @brief
 */
public interface IUserBusiness {

    /**
     * Finds user using its id
     *
     * @param id
     * @param success
     * @param failure
     */
    void get(int id, Action<User> success, Action<String> failure);

    /**
     * Returns true if current user attends to lesson
     *
     * @param lessonSlug
     * @param isAttending
     * @param needToSignIn Triggered if no user has signed in yet
     * @param failure
     */
    void isCurrentUserAttendingTo(String lessonSlug, Action<Boolean> isAttending,
                                  Action0 needToSignIn,
                                  Action<String> failure);

    /**
     * Toggles attendance for current user
     *
     * @param lessonId
     * @param isAttending New value to set
     * @param success
     * @param failure
     */
    void toggleAttendance(int lessonId, boolean isAttending,
                          Action0 success, Action<String> failure);

    /**
     * Try to sign in user
     *
     * @param email
     * @param password Unencrypted password
     * @param success
     * @param failure
     */
    void checkCredentials(String email, String password, Action0 success,
                          Action<String> failure);

    /**
     * Returns true if user is currently signed in
     *
     * @return
     */
    boolean isSignedIn();

    /**
     * Gets stored email
     *
     * @return
     */
    String getCurrentUserEmail();

    int getCurrentUserSchoolId();
}
