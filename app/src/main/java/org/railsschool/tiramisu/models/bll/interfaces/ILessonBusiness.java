package org.railsschool.tiramisu.models.bll.interfaces;

import com.coshx.chocolatine.utils.actions.Action;

import org.railsschool.tiramisu.models.bll.structs.LessonTeacherPair;

import java.util.List;

/**
 * @class ILessonBusiness
 * @brief
 */
public interface ILessonBusiness {
    public void sortByDate(Action<List<LessonTeacherPair>> success, Action<String> failure);
}
