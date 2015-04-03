package org.railsschool.tiramisu.models.bll.remote;

import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.beans.Venue;
import org.railsschool.tiramisu.models.bll.structs.SchoolClass;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * @class IRailsSchoolAPI
 * @brief
 */
public interface IRailsSchoolAPI {
    public final static String LESSON_ROOT = "/l";
    public final static String USER_ROOT   = "/users";
    public final static String VENUE_ROOT  = "/venues";

    public final static String FORMAT = ".json";

    @GET(LESSON_ROOT + "/slugs" + FORMAT)
    public void getLessonSlugs(Callback<List<String>> callback);

    @GET(LESSON_ROOT + "/{slug}" + FORMAT)
    public void getLesson(@Path("slug") String slug, Callback<Lesson> callback);

    @GET(LESSON_ROOT + "/{slug}" + FORMAT)
    public void getSchoolClass(@Path("slug") String slug, Callback<SchoolClass> callback);

    @GET(LESSON_ROOT + "/next" + FORMAT)
    public void getNextLesson(Callback<Lesson> callback);

    @GET(USER_ROOT + "/{id}" + FORMAT)
    public void getUser(@Path("id") int id, Callback<User> callback);

    @GET(VENUE_ROOT + "/{id}" + FORMAT)
    public void getVenue(@Path("id") int id, Callback<Venue> callback);
}
