package org.railsschool.tiramisu.models.bll;

import android.content.Context;

import com.coshx.chocolatine.utils.actions.Action;

import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.bll.interfaces.ILessonBusiness;
import org.railsschool.tiramisu.models.bll.interfaces.IUserBusiness;
import org.railsschool.tiramisu.models.bll.remote.interfaces.IRailsSchoolAPIOutlet;
import org.railsschool.tiramisu.models.bll.structs.LessonTeacherPair;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @class LessonBusiness
 * @brief
 */
class LessonBusiness extends BaseBusiness implements ILessonBusiness {
    private IUserBusiness _userBusiness;

    public LessonBusiness(Context context, IRailsSchoolAPIOutlet outlet,
                          IUserBusiness userBusiness) {
        super(context, outlet);

        this._userBusiness = userBusiness;
    }

    private void _pair(
        List<LessonTeacherPair> outcome,
        List<Lesson> lessons,
        int cursor,
        int size,
        Action<List<LessonTeacherPair>> success,
        Action<String> failure) {
        final Lesson l;

        if (cursor == size) {
            success.run(outcome);
            return;
        }

        l = lessons.get(cursor);
        tryConnecting(
            (api) -> {
                api.getUser(
                    l.teacherId,
                    new Callback<User>() {
                        @Override
                        public void success(User user, Response response) {
                            outcome.add(new LessonTeacherPair(l, user));
                            _pair(
                                outcome,
                                lessons,
                                cursor + 1,
                                size,
                                success,
                                failure
                            );
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            processError(error, failure);
                        }
                    }
                );
            },
            failure
        );
    }

    private void _pair(List<Lesson> lessons, Action<List<LessonTeacherPair>> success,
                       Action<String> failure) {
        _pair(
            new ArrayList<LessonTeacherPair>(),
            lessons,
            0,
            lessons.size(),
            success,
            failure
        );
    }

    @Override
    public void sortByDate(Action<List<LessonTeacherPair>> success, Action<String> failure) {
        tryConnecting(
            (api) -> {
                api.getLessons(
                    new Callback<List<Lesson>>() {
                        @Override
                        public void success(List<Lesson> lessons, Response response) {
                            _pair(lessons, success, failure);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            processError(error, failure);
                        }
                    }
                );
            },
            failure
        );
    }
}
