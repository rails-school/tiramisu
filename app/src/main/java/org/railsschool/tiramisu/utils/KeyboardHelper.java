package org.railsschool.tiramisu.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @class KeyboardHelper
 * @brief
 */
public class KeyboardHelper {
    public static void hide(Activity activity) {
        View focusedView = activity.getCurrentFocus();

        if (focusedView != null) {
            InputMethodManager inputManager =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                focusedView.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS
            );
        }
    }
}
