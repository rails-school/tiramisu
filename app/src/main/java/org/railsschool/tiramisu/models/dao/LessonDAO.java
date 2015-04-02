package org.railsschool.tiramisu.models.dao;

import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.dao.interfaces.ILessonDAO;

import io.realm.Realm;

/**
 * @class LessonDAO
 * @brief
 */
class LessonDAO extends BaseDAO implements ILessonDAO {
    private final static Object _saveLock = new Object();

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
    public void save(Lesson lesson) {
        synchronized (_saveLock) {
            if (exists(lesson.getSlug())) {
                update(lesson);
            } else {
                create(lesson);
            }
        }
    }

    public void create(Lesson lesson) {
        getDAL().executeTransaction(
            (dal) -> {
                dal.copyToRealm(lesson);
            }
        );
    }

    public void update(Lesson lesson) {
        getDAL().executeTransaction(
            (dal) -> {
                find(lesson.getSlug()).removeFromRealm();
                dal.copyToRealm(lesson);
            }
        );
    }

    public void delete(Lesson lesson) {
        getDAL().executeTransaction(
            (dal) -> {
                find(lesson.getSlug()).removeFromRealm();
            }
        );
    }
}
