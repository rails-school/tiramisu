package org.railsschool.tiramisu.models.bll;

import android.content.Context;

import com.coshx.chocolatine.utils.actions.Action;

import org.railsschool.tiramisu.models.beans.Venue;
import org.railsschool.tiramisu.models.bll.interfaces.IVenueBusiness;
import org.railsschool.tiramisu.models.bll.remote.interfaces.IRailsSchoolAPIOutlet;
import org.railsschool.tiramisu.models.dao.interfaces.IVenueDAO;

import retrofit.client.Response;

/**
 * @class VenueBusiness
 * @brief
 */
class VenueBusiness extends BaseBusiness implements IVenueBusiness {
    private IVenueDAO _venueDAO;

    public VenueBusiness(Context context, IRailsSchoolAPIOutlet outlet,
                         IVenueDAO venueDAO) {
        super(context, outlet);

        this._venueDAO = venueDAO;
    }

    @Override
    public void find(int id, Action<Venue> success, Action<String> failure) {
        if (_venueDAO.exists(id)) {
            success.run(_venueDAO.find(id));

            tryConnecting(
                (api) -> {
                    api.getVenue(
                        id,
                        new BLLCallback<Venue>(failure) {
                            @Override
                            public void success(Venue venue, Response response) {
                                _venueDAO.save(venue);
                            }
                        }
                    );
                },
                failure
            );
        } else {
            tryConnecting(
                (api) -> {
                    api.getVenue(
                        id,
                        new BLLCallback<Venue>(failure) {
                            @Override
                            public void success(Venue venue, Response response) {
                                success.run(venue);
                                _venueDAO.save(venue);
                            }
                        }
                    );
                },
                failure
            );
        }
    }
}
