package org.railsschool.tiramisu.views.helpers;

import android.text.format.DateUtils;

import org.joda.time.DateTime;

/**
 * @class DateHelper
 * @brief
 */
public class DateHelper {

    public static String makeFriendly(String rawDate) {
        DateTime date = new DateTime(rawDate);

        return DateUtils
            .getRelativeTimeSpanString(
                date.toDate().getTime(),
                DateTime.now().toDate().getTime(),
                DateUtils.SECOND_IN_MILLIS
            )
            .toString();
    }

    public static long inMilliseconds(String rawDate) {
        return new DateTime(rawDate).toDate().getTime();
    }
}
