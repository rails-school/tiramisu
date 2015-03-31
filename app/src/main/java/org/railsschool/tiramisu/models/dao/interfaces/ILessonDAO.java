package org.railsschool.tiramisu.models.dao.interfaces;

import org.railsschool.tiramisu.models.beans.Lesson;

/**
 * @class ILessonDAO
 * @brief
 */
public interface ILessonDAO {
    public boolean exists(int id);

    public Lesson find(int id);

    public void create(Lesson lesson);

    public void update(Lesson lesson);
}
