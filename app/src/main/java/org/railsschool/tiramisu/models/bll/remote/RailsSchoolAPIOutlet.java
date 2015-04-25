package org.railsschool.tiramisu.models.bll.remote;

import android.content.Context;
import android.util.Log;

import com.coshx.chocolatine.utils.actions.Action;
import com.coshx.chocolatine.utils.actions.Action0;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.beans.Venue;
import org.railsschool.tiramisu.models.bll.remote.interfaces.IRailsSchoolAPIOutlet;
import org.railsschool.tiramisu.models.bll.serializers.CheckCredentialsRequestSerializer;
import org.railsschool.tiramisu.models.bll.serializers.LessonSerializer;
import org.railsschool.tiramisu.models.bll.serializers.SchoolClassSerializer;
import org.railsschool.tiramisu.models.bll.serializers.UserSerializer;
import org.railsschool.tiramisu.models.bll.serializers.VenueSerializer;
import org.railsschool.tiramisu.models.bll.structs.CheckCredentialsRequest;
import org.railsschool.tiramisu.models.bll.structs.SchoolClass;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * @class RailsSchoolAPIOutlet
 * @brief
 */
class RailsSchoolAPIOutlet implements IRailsSchoolAPIOutlet {
    private Context             _context;
    private RestAdapter.Builder _adapterBuilder;

    public RailsSchoolAPIOutlet(Context context) {
        this._context = context;

        Gson gson = new GsonBuilder()
            .registerTypeAdapter(User.class, new UserSerializer())
            .registerTypeAdapter(Lesson.class, new LessonSerializer())
            .registerTypeAdapter(SchoolClass.class, new SchoolClassSerializer())
            .registerTypeAdapter(Venue.class, new VenueSerializer())
            .registerTypeAdapter(
                CheckCredentialsRequest.class,
                new CheckCredentialsRequestSerializer()
            )
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

        this._adapterBuilder = new RestAdapter.Builder()
            //.setLogLevel(RestAdapter.LogLevel.FULL)
            .setEndpoint(this._context.getString(R.string.api_endpoint))
            .setConverter(new GsonConverter(gson));
    }

    @Override
    public void connect(Action<IRailsSchoolAPI> success, Action0 failure) {
        try {
            IRailsSchoolAPI api = _adapterBuilder.build().create(IRailsSchoolAPI.class);

            success.run(api);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Failed to connect to remote server", e);
            failure.run();
        }
    }
}
