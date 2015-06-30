package org.railsschool.tiramisu.views.helpers;

import org.railsschool.tiramisu.models.beans.Lesson;

/**
 * @class LessonHelper
 * @brief
 */
public class LessonHelper {

    /**
     * Removes MD tags from lesson's description
     *
     * @param lesson
     * @return
     */
    public static String removeMarkdown(Lesson lesson) {
        // TODO: improve this method
        // Currently, only links are removed from the string
        String description = lesson.getDescription();

        description = description.replaceAll("\\[(.+?)\\]\\((.+?)\\)", "$1");

        return description;
    }
}
