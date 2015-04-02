package org.railsschool.tiramisu.models.dao.interfaces;

import org.railsschool.tiramisu.models.beans.Venue;

/**
 * @class IVenueDAO
 * @brief
 */
public interface IVenueDAO {
    public boolean exists(int id);

    public Venue find(int id);

    public void create(Venue venue);

    public void update(Venue venue);
}
