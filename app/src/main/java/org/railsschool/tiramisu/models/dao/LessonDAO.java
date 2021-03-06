package org.railsschool.tiramisu.models.dao;

import org.joda.time.DateTime;
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

    @Override
    public void truncateTable() {
        getDAL().where(Lesson.class).findAll().clear();
    }

    public void create(Lesson lesson) {
        getDAL().executeTransaction(
            (dal) -> {
                lesson.setUpdateDate(DateTime.now().toDate());
                dal.copyToRealm(lesson);
            }
        );
    }

    public void update(Lesson lesson) {
        getDAL().executeTransaction(
            (dal) -> {
                Lesson entry = find(lesson.getSlug());

                entry.setTitle(lesson.getTitle());
                entry.setSummary(lesson.getSummary());
                entry.setDescription(lesson.getDescription());
                entry.setStartTime(lesson.getStartTime());
                entry.setEndTime(lesson.getEndTime());
                entry.setTeacherId(lesson.getTeacherId());
                entry.setVenueId(lesson.getVenueId());
                entry.setUpdateDate(DateTime.now().toDate());
            }
        );
    }
}
