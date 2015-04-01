package org.railsschool.tiramisu.models.bll.structs;

import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.beans.User;

import java.util.List;

/**
 * @class SchoolClass
 * @brief
 */
public class SchoolClass {
    private Lesson     _lesson;
    private List<User> _students;

    public Lesson getLesson() {
        return _lesson;
    }

    public SchoolClass setLesson(Lesson value) {
        _lesson = value;
        return this;
    }

    public List<User> getStudents() {
        return _students;
    }

    public SchoolClass setStudents(List<User> value) {
        _students = value;
        return this;
    }
}
