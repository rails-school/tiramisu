package org.railsschool.tiramisu.models.bll.structs;

import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.beans.User;

/**
 * @class LessonTeacherPair
 * @brief
 */
public class LessonTeacherPair {
    public Lesson lesson;
    public User   teacher;

    public LessonTeacherPair(Lesson lesson, User teacher) {
        this.lesson = lesson;
        this.teacher = teacher;
    }
}
