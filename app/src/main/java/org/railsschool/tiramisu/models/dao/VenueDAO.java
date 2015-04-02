package org.railsschool.tiramisu.models.dao;

import org.railsschool.tiramisu.models.beans.Venue;
import org.railsschool.tiramisu.models.dao.interfaces.IVenueDAO;

import io.realm.Realm;

/**
 * @class VenueDAO
 * @brief
 */
public class VenueDAO extends BaseDAO implements IVenueDAO {
    public VenueDAO(Realm dal) {
        super(dal);
    }

    @Override
    public boolean exists(int id) {
        return find(id) != null;
    }

    @Override
    public Venue find(int id) {
        return getDAL().where(Venue.class).equalTo("id", id).findFirst();
    }

    @Override
    public void create(Venue venue) {
        getDAL().executeTransaction(
            (dal) -> {
                dal.copyToRealm(venue);
            }
        );
    }

    @Override
    public void update(Venue venue) {
        delete(venue);
        create(venue);
    }

    public void delete(Venue venue) {
        getDAL().executeTransaction(
            (dal) -> {
                find(venue.getId()).removeFromRealm();
            }
        );
    }
}
