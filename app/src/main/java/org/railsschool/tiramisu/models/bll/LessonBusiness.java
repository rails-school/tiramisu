package org.railsschool.tiramisu.models.bll;

import android.content.Context;
import android.util.Log;

import com.coshx.chocolatine.utils.actions.Action;
import com.coshx.chocolatine.utils.actions.Action3;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.beans.Venue;
import org.railsschool.tiramisu.models.bll.interfaces.ILessonBusiness;
import org.railsschool.tiramisu.models.bll.interfaces.IPreferenceBusiness;
import org.railsschool.tiramisu.models.bll.interfaces.IUserBusiness;
import org.railsschool.tiramisu.models.bll.interfaces.IVenueBusiness;
import org.railsschool.tiramisu.models.bll.remote.interfaces.IRailsSchoolAPIOutlet;
import org.railsschool.tiramisu.models.bll.structs.SchoolClass;
import org.railsschool.tiramisu.models.dao.DayNotificationPreference;
import org.railsschool.tiramisu.models.dao.TwoHourNotificationPreference;
import org.railsschool.tiramisu.models.dao.interfaces.ILessonDAO;

import java.util.List;

import retrofit.client.Response;

/**
 * @class LessonBusiness
 * @brief
 */
class LessonBusiness extends BaseBusiness implements ILessonBusiness {
    /**
     * Cooldown before pulling lesson again
     */
    private final static int COOLDOWN_MS = 5 * 60 * 1000;

    private IUserBusiness       _userBusiness;
    private IVenueBusiness      _venueBusiness;
    private IPreferenceBusiness _preferenceBusiness;
    private ILessonDAO          _lessonDAO;

    public LessonBusiness(Context context, IRailsSchoolAPIOutlet outlet,
                          IUserBusiness userBusiness,
                          IVenueBusiness venueBusiness,
                          IPreferenceBusiness preferenceBusiness,
                          ILessonDAO lessonDAO) {
        super(context, outlet);

        this._userBusiness = userBusiness;
        this._venueBusiness = venueBusiness;
        this._preferenceBusiness = preferenceBusiness;
        this._lessonDAO = lessonDAO;
    }

    /**
     * Returns true if lesson is currently in notification interval.
     *
     * @param periodMilli
     * @param lesson
     * @param hours
     * @return
     */
    private boolean _shouldBeNotified(int periodMilli, Lesson lesson, Hours hours) {
        DateTime startTime = new DateTime(lesson.getStartTime());
        // Alarm should only occur if it is the final countdown before lesson
        boolean isInCountdownInterval = new Interval(
            startTime.minusHours(hours.getHours()),
            startTime
        ).containsNow();
        // Alarm should only occur once. If puller's period is lower than alarm's period, then
        // ignore other alarms
        boolean isFirstNotification = new Interval(
            startTime.minusHours(hours.getHours()),
            startTime.minusHours(hours.getHours()).plusMillis(periodMilli)
        ).containsNow();

        return isInCountdownInterval && isFirstNotification;
    }

    @Override
    public void sortFutureSlugsByDate(Action<List<String>> success, Action<String> failure) {
        tryConnecting(
            (api) -> {
                BLLCallback callback = new BLLCallback<List<String>>(failure) {
                    @Override
                    public void success(List<String> lessonSlugs, Response response) {
                        success.run(lessonSlugs);
                    }
                };

                if (_userBusiness.isSignedIn()) {
                    api.getFutureLessonSlugs(
                        _userBusiness.getCurrentUserSchoolId(),
                        callback
                    );
                } else {
                    api.getFutureLessonSlugs(callback);
                }
            },
            failure
        );
    }

    @Override
    public void get(String lessonSlug, Action<Lesson> success, Action<String> failure) {
        if (_lessonDAO.exists(lessonSlug)) {
            Lesson l = _lessonDAO.find(lessonSlug);

            // Lesson already in local storage, run callback
            success.run(l);

            if (DateTime.now().minus(l.getUpdateDate().getTime()).getMillis() >=
                COOLDOWN_MS) {
                // Refresh lesson details
                tryConnecting(
                    (api) -> {
                        api.getLesson(
                            lessonSlug,
                            new BLLCallback<Lesson>(failure) {
                                @Override
                                public void success(Lesson lesson, Response response) {
                                    _lessonDAO.save(lesson);
                                }
                            }
                        );
                    },
                    failure
                );
            }
        } else {
            // No entry in local storage, forced to pull
            tryConnecting(
                (api) -> {
                    api.getLesson(
                        lessonSlug,
                        new BLLCallback<Lesson>(failure) {
                            @Override
                            public void success(Lesson lesson, Response response) {
                                success.run(lesson);
                                _lessonDAO.save(lesson);
                            }
                        }
                    );
                },
                failure
            );
        }
    }

    @Override
    public void getTuple(
        String lessonSlug,
        Action3<Lesson, User, Venue> success,
        Action<String> failure) {

        get(
            lessonSlug,
            (lesson) -> {
                _userBusiness.get(
                    lesson.getTeacherId(),
                    (teacher) -> {
                        _venueBusiness.get(
                            lesson.getVenueId(),
                            (venue) -> {
                                success.run(lesson, teacher, venue);
                            },
                            failure
                        );
                    },
                    failure
                );
            },
            failure
        );
    }

    @Override
    public void getSchoolClassTuple(
        String lessonSlug,
        Action3<SchoolClass, User, Venue> success,
        Action<String> failure) {

        tryConnecting(
            (api) -> {
                api.getSchoolClass(
                    lessonSlug,
                    new BLLCallback<SchoolClass>(failure) {
                        @Override
                        public void success(SchoolClass schoolClass, Response response) {
                            _userBusiness.get(
                                schoolClass.getLesson().getTeacherId(),
                                (teacher) -> {
                                    _venueBusiness.get(
                                        schoolClass.getLesson().getVenueId(),
                                        (venue) -> {
                                            success.run(schoolClass, teacher, venue);
                                        },
                                        failure
                                    );
                                },
                                failure
                            );

                            _lessonDAO.save(schoolClass.getLesson());
                        }
                    }
                );
            },
            failure
        );
    }

    @Override
    public void getUpcoming(Action<Lesson> success) {
        Action<String> failure = (error) -> {
            Log.e(getClass().getSimpleName(), error);
        };

        tryConnecting(
            (api) -> {
                BLLCallback<Lesson> callback = new BLLCallback<Lesson>(failure) {
                    @Override
                    public void success(Lesson lesson, Response response) {
                        success.run(lesson);
                    }
                };

                if (_userBusiness.isSignedIn()) {
                    api.getUpcomingLesson(
                        _userBusiness.getCurrentUserSchoolId(),
                        callback
                    );
                } else {
                    api.getUpcomingLesson(callback);
                }
            },
            failure
        );
    }

    @Override
    public void engineAlarms(int periodMilli, Action<Lesson> twoHourAlarm, Action<Lesson> dayAlarm) {
        getUpcoming(
            (lesson) -> {
                if (lesson == null) {
                    return; // No upcoming lesson
                }

                if (_shouldBeNotified(periodMilli, lesson, Hours.TWO)) {
                    TwoHourNotificationPreference pref = _preferenceBusiness.getTwoHourReminderPreference();

                    switch (pref) {
                        case ALWAYS:
                            twoHourAlarm.run(lesson);
                            break;
                        case IF_ATTENDING:
                            _userBusiness.isCurrentUserAttendingTo(
                                lesson.getSlug(),
                                (isAttending) -> {
                                    if (isAttending) {
                                        twoHourAlarm.run(lesson);
                                    }
                                },
                                () -> {
                                    // User not signed in, ignore preference
                                },
                                (error) -> {
                                    Log.e(getClass().getSimpleName(), error);
                                }
                            );
                            break;
                        default:
                            // No alarm
                            break;
                    }
                }

                if (_shouldBeNotified(periodMilli, lesson, Hours.hours(24))) {
                    DayNotificationPreference pref = _preferenceBusiness
                        .getDayReminderPreference();

                    switch (pref) {
                        case ALWAYS:
                            dayAlarm.run(lesson);
                            break;
                        case IF_ATTENDING:
                            _userBusiness.isCurrentUserAttendingTo(
                                lesson.getSlug(),
                                (isAttending) -> {
                                    if (isAttending) {
                                        dayAlarm.run(lesson);
                                    }
                                },
                                () -> {
                                    // User not signed in, ignore preference
                                },
                                (error) -> {
                                    Log.e(getClass().getSimpleName(), error);
                                }
                            );
                            break;
                        default:
                            // No alarm
                            break;
                    }
                }
            }
        );
    }

    @Override
    public void cleanDatabase() {
        _lessonDAO.truncateTable();
    }
}
