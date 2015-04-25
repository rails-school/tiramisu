package org.railsschool.tiramisu.models.bll.remote;

import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.beans.Venue;
import org.railsschool.tiramisu.models.bll.structs.CheckCredentialsRequest;
import org.railsschool.tiramisu.models.bll.structs.SchoolClass;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * @class IRailsSchoolAPI
 * @brief
 */
public interface IRailsSchoolAPI {
    public final static String LESSON_ROOT     = "/l";
    public final static String USER_ROOT       = "/users";
    public final static String VENUE_ROOT      = "/venues";
    public final static String ATTENDANCE_ROOT = "/attendances";

    public final static String FORMAT = ".json";

    //region Lessons

    @GET(LESSON_ROOT + "/future/slugs" + FORMAT)
    public void getFutureLessonSlugs(Callback<List<String>> callback);

    @GET(LESSON_ROOT + "/{slug}" + FORMAT)
    public void getLesson(@Path("slug") String slug, Callback<Lesson> callback);

    @GET(LESSON_ROOT + "/{slug}" + FORMAT)
    public void getSchoolClass(@Path("slug") String slug, Callback<SchoolClass> callback);

    @GET(LESSON_ROOT + "/upcoming" + FORMAT)
    public void getUpcomingLesson(Callback<Lesson> callback);

    //endregion

    //region Users

    @GET(USER_ROOT + "/{id}" + FORMAT)
    public void getUser(@Path("id") int id, Callback<User> callback);

    @POST(USER_ROOT + "/sign_in" + FORMAT)
    @FormUrlEncoded
    public void checkCredentials(
        @Body CheckCredentialsRequest request,
        Callback<Void> callback);

    //endregion

    //region Venues

    @GET(VENUE_ROOT + "/{id}" + FORMAT)
    public void getVenue(@Path("id") int id, Callback<Venue> callback);

    //endregion

    //region Attendances

    @PUT(ATTENDANCE_ROOT + FORMAT)
    @FormUrlEncoded
    public void toggleAttendance(
        @Field("lessonSlug") String lessonSlug,
        @Field("username") String username,
        @Field("userToken") String userToken,
        @Field("attendance") boolean attendance,
        Callback<Void> callback);

    // TODO: Change route
    @GET(ATTENDANCE_ROOT + "/{lessonSlug}/{username}/{userToken}" + FORMAT)
    public void isAttending(
        @Path("lessonSlug") String lessonSlug,
        @Path("username") String username,
        @Path("userToken") String userToken,
        Callback<Boolean> callback);

    //endregion
}
