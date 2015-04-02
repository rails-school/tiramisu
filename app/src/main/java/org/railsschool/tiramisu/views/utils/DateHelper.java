package org.railsschool.tiramisu.views.utils;

import android.content.Context;
import android.text.format.DateUtils;

import org.joda.time.DateTime;

/**
 * @class DateHelper
 * @brief
 */
public class DateHelper {

    public static String makeFriendly(Context context, String rawDate) {
        DateTime date = new DateTime(rawDate);

        return DateUtils
            .getRelativeTimeSpanString(
                date.toDate().getTime(),
                DateTime.now().toDate().getTime(),
                DateUtils.SECOND_IN_MILLIS
            )
            .toString();
    }
}
