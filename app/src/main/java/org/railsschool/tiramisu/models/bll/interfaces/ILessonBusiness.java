package org.railsschool.tiramisu.models.bll.interfaces;

import com.coshx.chocolatine.utils.actions.Action;
import com.coshx.chocolatine.utils.actions.Action2;

import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.beans.User;

import java.util.List;

/**
 * @class ILessonBusiness
 * @brief
 */
public interface ILessonBusiness {
    public void sortIdsByDate(Action<List<Integer>> success, Action<String> failure);

    public void getPair(int lessonId, Action2<Lesson, User> success,
                        Action<Lesson> lessonRefresh, Action<User> teacherRefresh, Action<String> failure);
}
