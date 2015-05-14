package org.railsschool.tiramisu.models.bll.interfaces;

import com.coshx.chocolatine.utils.actions.Action;
import com.coshx.chocolatine.utils.actions.Action3;

import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.beans.Venue;
import org.railsschool.tiramisu.models.bll.structs.SchoolClass;

import java.util.List;

/**
 * @class ILessonBusiness
 * @brief
 */
public interface ILessonBusiness {

    /**
     * Sorts slugs of lessons by start time asc
     *
     * @param success
     * @param failure
     */
    void sortFutureSlugsByDate(Action<List<String>> success, Action<String> failure);

    /**
     * Gets a lesson by slug
     *
     * @param lessonSlug
     * @param success
     * @param failure
     */
    void get(String lessonSlug, Action<Lesson> success, Action<String> failure);

    /**
     * Gets lesson tuple (lesson, teacher and venue) using slug
     *
     * @param lessonSlug
     * @param success
     * @param failure
     */
    void getTuple(String lessonSlug,
                  Action3<Lesson, User, Venue> success,
                  Action<String> failure);

    /**
     * Gets school class tuple (school class, teacher and venue) using slug
     *
     * @param lessonSlug
     * @param success
     * @param failure
     */
    void getSchoolClassTuple(String lessonSlug,
                             Action3<SchoolClass, User, Venue> success,
                             Action<String> failure);

    /**
     * Gets upcoming lesson
     *
     * @param success
     */
    void getUpcoming(Action<Lesson> success);

    /**
     * Engines upcoming lesson and triggers alarm initializers if needed
     *
     * @param periodMilli  Period of puller in milliseconds
     * @param twoHourAlarm
     * @param dayAlarm
     */
    void engineAlarms(int periodMilli, Action<Lesson> twoHourAlarm, Action<Lesson> dayAlarm);
}
