package org.railsschool.tiramisu.models.bll.remote.interfaces;

import com.coshx.chocolatine.utils.actions.Action;
import com.coshx.chocolatine.utils.actions.Action0;

import org.railsschool.tiramisu.models.bll.remote.IRailsSchoolAPI;

/**
 * @class IRailsSchoolAPIOutlet
 * @brief Handles connections to remote API
 */
public interface IRailsSchoolAPIOutlet {
    void connect(Action<IRailsSchoolAPI> success, Action0 failure);

    /**
     * Connects API using authentication token
     *
     * @param authenticationCookie
     * @param success
     * @param failure
     */
    void connect(String authenticationCookie, Action<IRailsSchoolAPI> success,
                 Action0 failure);
}
