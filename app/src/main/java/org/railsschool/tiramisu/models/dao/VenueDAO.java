package org.railsschool.tiramisu.models.dao;

import org.joda.time.DateTime;
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

    @Override
    public void truncateTable() {
        getDAL().where(Venue.class).findAll().clear();
    }

    public void create(Venue venue) {
        getDAL().executeTransaction(
            (dal) -> {
                venue.setUpdateDate(DateTime.now().toDate());
                dal.copyToRealm(venue);
            }
        );
    }

    public void update(Venue venue) {
        getDAL().executeTransaction(
            (dal) -> {
                Venue entry = find(venue.getId());

                entry.setZip(venue.getZip());
                entry.setLatitude(venue.getLatitude());
                entry.setLongitude(venue.getLongitude());
                entry.setName(venue.getName());
                entry.setAddress(venue.getAddress());
                entry.setCity(venue.getCity());
                entry.setState(venue.getState());
                entry.setCountry(venue.getCountry());
                entry.setUpdateDate(DateTime.now().toDate());
            }
        );
    }
}
