package org.railsschool.tiramisu.models.bll.structs;

import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.beans.User;

import java.util.List;

/**
 * @class SchoolClass
 * @brief
 */
public class SchoolClass extends Lesson {
    public List<User> students;
}
