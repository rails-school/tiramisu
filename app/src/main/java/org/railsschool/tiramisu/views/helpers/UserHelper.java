package org.railsschool.tiramisu.views.helpers;

import org.railsschool.tiramisu.models.beans.User;

/**
 * @class UserHelper
 * @brief
 */
public class UserHelper {

    public static String getDisplayedName(User user) {
        if (user.getHideLastName()) {
            String[] a = user.getName().trim().split(" ");

            if (a.length > 0) {
                return a[0];
            } else {
                return user.getName();
            }
        } else {
            return user.getName();
        }
    }
}
