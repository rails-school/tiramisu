package org.railsschool.tiramisu.models.bll;

import android.content.Context;

import com.coshx.chocolatine.utils.actions.Action;
import com.coshx.chocolatine.utils.actions.Action2;

import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.bll.interfaces.ILessonBusiness;
import org.railsschool.tiramisu.models.bll.interfaces.IUserBusiness;
import org.railsschool.tiramisu.models.bll.remote.interfaces.IRailsSchoolAPIOutlet;
import org.railsschool.tiramisu.models.bll.structs.SchoolClass;
import org.railsschool.tiramisu.models.dao.interfaces.ILessonDAO;

import java.util.List;

import retrofit.client.Response;

/**
 * @class LessonBusiness
 * @brief
 */
class LessonBusiness extends BaseBusiness implements ILessonBusiness {

    private IUserBusiness _userBusiness;
    private ILessonDAO    _lessonDAO;

    public LessonBusiness(Context context, IRailsSchoolAPIOutlet outlet,
                          IUserBusiness userBusiness, ILessonDAO lessonDAO) {
        super(context, outlet);

        this._userBusiness = userBusiness;
        this._lessonDAO = lessonDAO;
    }

    @Override
    public void sortIdsByDate(Action<List<String>> success, Action<String> failure) {
        tryConnecting(
            (api) -> {
                api.getLessonSlugs(
                    new BLLCallback<List<String>>(failure) {
                        @Override
                        public void success(List<String> lessonSlugs, Response response) {
                            success.run(lessonSlugs);
                        }
                    }
                );
            },
            failure
        );
    }

    @Override
    public void getPair(
        String lessonSlug, Action2<Lesson, User> success,
        Action<Lesson> lessonRefresh, Action<User> teacherRefresh,
        Action<String> failure) {

        Action<Lesson> getTeacher = (lesson) -> {
            _userBusiness.find(
                lesson.getTeacherId(),
                (teacher) -> {
                    success.run(lesson, teacher);
                },
                teacherRefresh,
                failure
            );
        };

        // Warning: this function assumes there is no duplicate in
        // input list. Otherwise, this method must crash due to
        // DB access multi-threading (creation may happen twice).
        if (_lessonDAO.exists(lessonSlug)) {
            // Lesson already in local storage, run callback
            getTeacher.run(_lessonDAO.find(lessonSlug));

            // Refresh lesson details
            tryConnecting(
                (api) -> {
                    api.getLesson(
                        lessonSlug,
                        new BLLCallback<Lesson>(failure) {
                            @Override
                            public void success(Lesson lesson, Response response) {
                                lessonRefresh.run(lesson);
                                _lessonDAO.update(lesson);
                            }
                        }
                    );
                },
                failure
            );
        } else {
            // No entry in local storage, forced to pull
            tryConnecting(
                (api) -> {
                    api.getLesson(
                        lessonSlug,
                        new BLLCallback<Lesson>(failure) {
                            @Override
                            public void success(Lesson lesson, Response response) {
                                getTeacher.run(lesson);
                                _lessonDAO.create(lesson);
                            }
                        }
                    );
                },
                failure
            );
        }
    }

    @Override
    public void getSchoolClassPair(String lessonSlug, Action2<SchoolClass, User> success, Action<User> teacherRefresh, Action<String> failure) {
        tryConnecting(
            (api) -> {
                api.getSchoolClass(
                    lessonSlug,
                    new BLLCallback<SchoolClass>(failure) {
                        @Override
                        public void success(SchoolClass schoolClass, Response response) {
                            _userBusiness.find(
                                schoolClass.getLesson().getTeacherId(),
                                (teacher) -> {
                                    success.run(schoolClass, teacher);
                                },
                                teacherRefresh,
                                failure
                            );

                            if (_lessonDAO.exists(schoolClass.getLesson().getSlug())) {
                                _lessonDAO.update(schoolClass.getLesson());
                            } else {
                                _lessonDAO.create(schoolClass.getLesson());
                            }
                        }
                    }
                );
            },
            failure
        );
    }
}
