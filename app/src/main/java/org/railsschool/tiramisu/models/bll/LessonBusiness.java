package org.railsschool.tiramisu.models.bll;

import android.content.Context;

import org.railsschool.tiramisu.models.bll.interfaces.ILessonBusiness;
import org.railsschool.tiramisu.models.bll.remote.interfaces.IRailsSchoolAPIOutlet;

/**
 * @class LessonBusiness
 * @brief
 */
class LessonBusiness extends BaseBusiness implements ILessonBusiness {
    public LessonBusiness(Context context, IRailsSchoolAPIOutlet outlet) {
        super(context, outlet);
    }
}
