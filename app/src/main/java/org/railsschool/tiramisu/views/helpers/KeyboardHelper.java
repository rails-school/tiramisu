package org.railsschool.tiramisu.views.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Snippet from https://stackoverflow.com/q/7696791
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
