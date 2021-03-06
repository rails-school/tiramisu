package org.railsschool.tiramisu.views.helpers;

import org.railsschool.tiramisu.models.beans.User;

/**
 * @class UserHelper
 * @brief
 */
public class UserHelper {

    /**
     * Hides last name if requested
     *
     * @param user
     * @return
     */
    public static String getDisplayedName(User user) {
        if (user.getHideLastName()) {
            String[] a = user.getName().trim().split(" ");

            if (a.length > 0) { // Hide last name
                return a[0];
            } else { // No last name
                return user.getName();
            }
        } else {
            return user.getName();
        }
    }
}
