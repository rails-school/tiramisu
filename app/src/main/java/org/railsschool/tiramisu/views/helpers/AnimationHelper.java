package org.railsschool.tiramisu.views.helpers;

import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * @class AnimationHelper
 * @brief
 */
public class AnimationHelper {

    public static void pressed(View view) {
        YoYo
            .with(Techniques.Pulse)
            .duration(500)
            .playOn(view);
    }
}
