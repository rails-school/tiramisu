package org.railsschool.tiramisu.models.bll.remote.interfaces;

import com.coshx.chocolatine.utils.actions.Action;
import com.coshx.chocolatine.utils.actions.Action0;

import org.railsschool.tiramisu.models.bll.remote.IRailsSchoolAPI;

/**
 * @class IRailsSchoolAPIOutlet
 * @brief
 */
public interface IRailsSchoolAPIOutlet {
    void connect(Action<IRailsSchoolAPI> success, Action0 failure);

    void connect(String cookieAuthentication, Action<IRailsSchoolAPI> success,
                 Action0 failure);
}
