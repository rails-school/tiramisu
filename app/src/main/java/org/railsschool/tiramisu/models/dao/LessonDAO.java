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
    public boolean exists(String slug) {
        return find(slug) != null;
    }

    @Override
    public Lesson find(String slug) {
        return getDAL().where(Lesson.class).equalTo("slug", slug).findFirst();
    }

    @Override
    public void create(Lesson lesson) {
        getDAL().executeTransaction(
            (dal) -> {
                dal.copyToRealm(lesson);
            }
        );
    }

    @Override
    public void update(Lesson lesson) {
        delete(lesson);
        create(lesson);
    }

    public void delete(Lesson lesson) {
        getDAL().executeTransaction(
            (dal) -> {
                find(lesson.getSlug()).removeFromRealm();
            }
        );
    }
}
