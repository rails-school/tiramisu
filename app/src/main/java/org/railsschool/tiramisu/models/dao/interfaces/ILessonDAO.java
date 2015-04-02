package org.railsschool.tiramisu.models.dao.interfaces;

import org.railsschool.tiramisu.models.beans.Lesson;

/**
 * @class ILessonDAO
 * @brief
 */
public interface ILessonDAO {
    public boolean exists(String slug);

    public Lesson find(String slug);

    public void save(Lesson lesson);
}
