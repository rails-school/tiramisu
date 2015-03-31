package org.railsschool.tiramisu.models.bll.interfaces;

import com.coshx.chocolatine.utils.actions.Action;

import org.railsschool.tiramisu.models.beans.User;

/**
 * @class IUserBusiness
 * @brief
 */
public interface IUserBusiness {
    public void find(int id, Action<User> success, Action<User> refresh,
                     Action<String> failure);
}
