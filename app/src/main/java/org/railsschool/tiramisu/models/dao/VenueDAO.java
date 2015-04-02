package org.railsschool.tiramisu.models.dao;

import org.railsschool.tiramisu.models.beans.Venue;
import org.railsschool.tiramisu.models.dao.interfaces.IVenueDAO;

import io.realm.Realm;

/**
 * @class VenueDAO
 * @brief
 */
class VenueDAO extends BaseDAO implements IVenueDAO {
    private final static Object _saveLock = new Object();

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
    public void save(Venue venue) {
        synchronized (_saveLock) {
            if (exists(venue.getId())) {
                update(venue);
            } else {
                create(venue);
            }
        }
    }

    public void create(Venue venue) {
        getDAL().executeTransaction(
            (dal) -> {
                dal.copyToRealm(venue);
            }
        );
    }

    public void update(Venue venue) {
        getDAL().executeTransaction(
            (dal) -> {
                find(venue.getId()).removeFromRealm();
                dal.copyToRealm(venue);
            }
        );
    }

    public void delete(Venue venue) {
        getDAL().executeTransaction(
            (dal) -> {
                find(venue.getId()).removeFromRealm();
            }
        );
    }
}
