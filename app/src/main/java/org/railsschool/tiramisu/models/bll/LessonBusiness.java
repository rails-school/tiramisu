package org.railsschool.tiramisu.models.bll;

import android.content.Context;

import com.coshx.chocolatine.utils.actions.Action;
import com.coshx.chocolatine.utils.actions.Action2;

import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.bll.interfaces.ILessonBusiness;
import org.railsschool.tiramisu.models.bll.interfaces.IUserBusiness;
import org.railsschool.tiramisu.models.bll.remote.interfaces.IRailsSchoolAPIOutlet;
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
    public void sortIdsByDate(Action<List<Integer>> success, Action<String> failure) {
        tryConnecting(
            (api) -> {
                api.getLessonIds(
                    new BLLCallback<List<Integer>>(failure) {
                        @Override
                        public void success(List<Integer> lessons, Response response) {
                            success.run(lessons);
                        }
                    }
                );
            },
            failure
        );
    }

    @Override
    public void getPair(
        int lessonId, Action2<Lesson, User> success,
        Action<Lesson> lessonRefresh, Action<User> teacherRefresh,
        Action<String> failure) {

        Action<Lesson> getTeacher = (lesson) -> {
            _userBusiness.find(
                lesson.teacherId,
                (teacher) -> {
                    success.run(lesson, teacher);
                },
                teacherRefresh,
                failure
            );
        };

        if (_lessonDAO.exists(lessonId)) {
            // Lesson already in local storage, run callback
            getTeacher.run(_lessonDAO.find(lessonId));

            // Refresh lesson details
            tryConnecting(
                (api) -> {
                    api.getLesson(
                        lessonId,
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
                        lessonId,
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
}
