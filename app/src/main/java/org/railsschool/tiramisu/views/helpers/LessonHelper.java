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
        // TODO: improve this method by removing all tags
        String description = lesson.getDescription();

        // Remove links
        description = description.replaceAll("\\[(.+?)\\]\\((.+?)\\)", "$1");
        // Remove emphasis
        description = description.replaceAll("[_\\*]{1,2}(.+?)[_\\*]{1,2}", "$1");

        return description;
    }
}
