package org.railsschool.tiramisu.models.bll.remote;

import android.content.Context;

import org.railsschool.tiramisu.models.bll.remote.interfaces.IRailsSchoolAPIOutlet;

/**
 * @class RailsSchoolAPIOutletFactory
 * @brief
 */
public class RailsSchoolAPIOutletFactory {
    private static IRailsSchoolAPIOutlet _outlet;

    public static IRailsSchoolAPIOutlet build(Context context) {
        if (_outlet == null) {
            _outlet = new RailsSchoolAPIOutlet(context);
        }

        return _outlet;
    }
}
