package org.railsschool.tiramisu.models.dao.interfaces;

import org.railsschool.tiramisu.models.beans.Venue;

/**
 * @class IVenueDAO
 * @brief
 */
public interface IVenueDAO {
    boolean exists(int id);

    Venue find(int id);

    void save(Venue venue);

    void truncateTable();
}
