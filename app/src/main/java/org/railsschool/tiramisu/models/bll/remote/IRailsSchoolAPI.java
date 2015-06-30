package org.railsschool.tiramisu.models.bll.remote;

import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.beans.Venue;
import org.railsschool.tiramisu.models.bll.structs.CheckCredentialsRequest;
import org.railsschool.tiramisu.models.bll.structs.SchoolClass;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * @class IRailsSchoolAPI
 * @brief API offered by RailsSchool
 */
public interface IRailsSchoolAPI {
    String LESSON_ROOT = "/l";
    String USER_ROOT   = "/users";
    String VENUE_ROOT  = "/venues";

    String FORMAT = ".json";

    //region Lessons

    @GET(LESSON_ROOT + "/future/slugs" + FORMAT)
    void getFutureLessonSlugs(Callback<List<String>> callback);

    @GET(LESSON_ROOT + "/future/slugs/{school}" + FORMAT)
    void getFutureLessonSlugs(@Path("school") int schoolId, Callback<List<String>> callback);

    @GET(LESSON_ROOT + "/{slug}" + FORMAT)
    void getLesson(@Path("slug") String slug, Callback<Lesson> callback);

    @GET(LESSON_ROOT + "/{slug}" + FORMAT)
    void getSchoolClass(@Path("slug") String slug, Callback<SchoolClass> callback);

    @GET(LESSON_ROOT + "/upcoming" + FORMAT)
    void getUpcomingLesson(Callback<Lesson> callback);

    @GET(LESSON_ROOT + "/upcoming/{school}" + FORMAT)
    void getUpcomingLesson(@Path("school") int schoolId, Callback<Lesson> callback);

    //endregion

    //region Users

    @GET(USER_ROOT + "/{id}" + FORMAT)
    void getUser(@Path("id") int id, Callback<User> callback);

    @POST(USER_ROOT + "/sign_in" + FORMAT)
    void checkCredentials(
        @Body CheckCredentialsRequest request,
        Callback<User> callback);

    @DELETE(USER_ROOT + "/sign_out" + FORMAT)
    void logOut(Callback<Void> callback);

    //endregion

    //region Venues

    @GET(VENUE_ROOT + "/{id}" + FORMAT)
    void getVenue(@Path("id") int id, Callback<Venue> callback);

    //endregion

    //region Attendances

    @GET("/attending_lesson/{slug}" + FORMAT)
    void isAttending(@Path("slug") String lessonSlug, Callback<Boolean> callback);

    @POST("/rsvp/{id}" + FORMAT)
    void attend(@Path("id") int lessonId, Callback<Void> callback);

    @POST("/rsvp/{id}/delete" + FORMAT)
    void removeAttendance(@Path("id") int lessonId, Callback<Void> callback);

    //endregion
}
