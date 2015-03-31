package org.railsschool.tiramisu.models.bll.remote;

import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.beans.SchoolClass;
import org.railsschool.tiramisu.models.beans.User;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * @class IRailsSchoolAPI
 * @brief
 */
public interface IRailsSchoolAPI {
    public final static String LESSON_ROOT = "/lessons";
    public final static String USER_ROOT   = "/users";

    public final static String FORMAT = ".json";

    @GET(LESSON_ROOT + FORMAT)
    public void getLessons(Callback<List<Lesson>> callback);

    @GET(LESSON_ROOT + "/{id}" + FORMAT)
    public void getLesson(@Path("id") int id, Callback<SchoolClass> callback);

    @GET(USER_ROOT + "/{id}" + FORMAT)
    public void getUser(@Path("id") int id, Callback<User> callback);
}
