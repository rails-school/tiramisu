package org.railsschool.tiramisu.models.bll;

import android.content.Context;

import com.coshx.chocolatine.utils.actions.Action;
import com.coshx.chocolatine.utils.actions.Action3;

import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.beans.Venue;
import org.railsschool.tiramisu.models.bll.interfaces.ILessonBusiness;
import org.railsschool.tiramisu.models.bll.interfaces.IUserBusiness;
import org.railsschool.tiramisu.models.bll.interfaces.IVenueBusiness;
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

    private IUserBusiness  _userBusiness;
    private IVenueBusiness _venueBusiness;
    private ILessonDAO     _lessonDAO;

    public LessonBusiness(Context context, IRailsSchoolAPIOutlet outlet,
                          IUserBusiness userBusiness,
                          IVenueBusiness venueBusiness,
                          ILessonDAO lessonDAO) {
        super(context, outlet);

        this._userBusiness = userBusiness;
        this._venueBusiness = venueBusiness;
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
        String lessonSlug,
        Action3<Lesson, User, Venue> success,
        Action<String> failure) {

        Action<Lesson> getTuple = (lesson) -> {
            _userBusiness.find(
                lesson.getTeacherId(),
                (teacher) -> {
                    _venueBusiness.find(
                        lesson.getVenueId(),
                        (venue) -> {
                            success.run(lesson, teacher, venue);
                        },
                        failure
                    );
                },
                failure
            );
        };

        if (_lessonDAO.exists(lessonSlug)) {
            // Lesson already in local storage, run callback
            getTuple.run(_lessonDAO.find(lessonSlug));

            // Refresh lesson details
            tryConnecting(
                (api) -> {
                    api.getLesson(
                        lessonSlug,
                        new BLLCallback<Lesson>(failure) {
                            @Override
                            public void success(Lesson lesson, Response response) {
                                _lessonDAO.save(lesson);
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
                                getTuple.run(lesson);
                                _lessonDAO.save(lesson);
                            }
                        }
                    );
                },
                failure
            );
        }
    }

    @Override
    public void getSchoolClassPair(
        String lessonSlug,
        Action3<SchoolClass, User, Venue> success,
        Action<String> failure) {

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
                                    _venueBusiness.find(
                                        schoolClass.getLesson().getVenueId(),
                                        (venue) -> {
                                            success.run(schoolClass, teacher, venue);
                                        },
                                        failure
                                    );
                                },
                                failure
                            );

                            _lessonDAO.save(schoolClass.getLesson());
                        }
                    }
                );
            },
            failure
        );
    }
}
