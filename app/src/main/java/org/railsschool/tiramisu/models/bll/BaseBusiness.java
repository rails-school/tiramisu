package org.railsschool.tiramisu.models.bll;

import android.content.Context;
import android.util.Log;

import com.coshx.chocolatine.utils.actions.Action;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.bll.remote.IRailsSchoolAPI;
import org.railsschool.tiramisu.models.bll.remote.interfaces.IRailsSchoolAPIOutlet;

import retrofit.RetrofitError;

/**
 * @class BaseBusiness
 * @brief
 */
abstract class BaseBusiness {
    private Context               _context;
    private IRailsSchoolAPIOutlet _outlet;

    public BaseBusiness(Context context, IRailsSchoolAPIOutlet outlet) {
        this._context = context;
        this._outlet = outlet;
    }

    public Context getContext() {
        return _context;
    }

    public void tryConnecting(Action<IRailsSchoolAPI> success, Action<String> failure) {
        _outlet.connect(
            (api) -> {
                success.run(api);
            },
            () -> failure.run(_context.getString(R.string.error_no_connection))
        );
    }

    public void processError(RetrofitError error, Action<String> failure) {
        Log.e(getClass().getSimpleName(), error.getMessage(), error);
        failure.run(_context.getString(R.string.error_default));
    }
}
