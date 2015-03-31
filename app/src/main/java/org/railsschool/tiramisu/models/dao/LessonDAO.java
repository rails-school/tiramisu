package org.railsschool.tiramisu.models.dao;

import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.dao.interfaces.ILessonDAO;

import io.realm.Realm;

/**
 * @class LessonDAO
 * @brief
 */
class LessonDAO extends BaseDAO implements ILessonDAO {
    public LessonDAO(Realm dal) {
        super(dal);
    }

    @Override
    public boolean exists(int id) {
        return find(id) != null;
    }

    @Override
    public Lesson find(int id) {
        return getDAL().where(Lesson.class).equalTo("id", id).findFirst();
    }

    @Override
    public void create(Lesson lesson) {
        getDAL().executeTransaction(
            (dal) -> {
                Lesson entity = getDAL().copyToRealm(lesson);
            }
        );
    }

    @Override
    public void update(Lesson lesson) {
        getDAL().executeTransaction(
            (dal) -> {
                Lesson entity = getDAL().copyToRealm(lesson);
            }
        );
    }
}
