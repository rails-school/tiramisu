package org.railsschool.tiramisu.models.bll.interfaces;

import com.coshx.chocolatine.utils.actions.Action;

import org.railsschool.tiramisu.models.beans.Venue;

/**
 * @class IVenueBusiness
 * @brief
 */
public interface IVenueBusiness {
    void get(int id, Action<Venue> success, Action<String> failure);

    void cleanDatabase();
}
