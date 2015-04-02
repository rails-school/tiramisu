package org.railsschool.tiramisu.models.bll.interfaces;

import com.coshx.chocolatine.utils.actions.Action;
import com.coshx.chocolatine.utils.actions.Action3;

import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.beans.Venue;
import org.railsschool.tiramisu.models.bll.structs.SchoolClass;

import java.util.List;

/**
 * @class ILessonBusiness
 * @brief
 */
public interface ILessonBusiness {
    public void sortIdsByDate(Action<List<String>> success, Action<String> failure);

    public void getPair(String lessonSlug,
                        Action3<Lesson, User, Venue> success,
                        Action<String> failure);

    public void getSchoolClassPair(String lessonSlug,
                                   Action3<SchoolClass, User, Venue> success,
                                   Action<String> failure);
}
