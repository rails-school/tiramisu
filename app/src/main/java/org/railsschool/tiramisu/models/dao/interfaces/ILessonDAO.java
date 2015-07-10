package org.railsschool.tiramisu.models.dao.interfaces;

import org.railsschool.tiramisu.models.beans.Lesson;

/**
 * @class ILessonDAO
 * @brief
 */
public interface ILessonDAO {
    boolean exists(String slug);

    Lesson find(String slug);

    void save(Lesson lesson);

    void truncateTable();
}
