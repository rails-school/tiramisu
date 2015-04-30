package org.railsschool.tiramisu.views.patterns;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.dao.DayNotificationPreference;
import org.railsschool.tiramisu.models.dao.TwoHourNotificationPreference;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @class ReminderSeekbarLabelPattern
 * @brief
 */
public class ReminderSeekbarLabelPattern {
    private Context _context;

    @InjectView(R.id.pattern_reminder_seekbar_always)
    TextView _always;

    @InjectView(R.id.pattern_reminder_seekbar_attending)
    TextView _onlyIfAttending;

    @InjectView(R.id.pattern_reminder_seekbar_never)
    TextView _never;

    public ReminderSeekbarLabelPattern(Context context, View pattern) {
        this._context = context;
        ButterKnife.inject(this, pattern);
    }

    private void _setCurrent(int value) {
        int softGray = _context.getResources().getColor(R.color.soft_gray),
            gray = _context.getResources().getColor(R.color.gray);

        _always.setTextColor(softGray);
        _onlyIfAttending.setTextColor(softGray);
        _never.setTextColor(softGray);

        if (value == 0) {
            _always.setTextColor(gray);
        } else if (value == 2) {
            _never.setTextColor(gray);
        } else {
            _onlyIfAttending.setTextColor(gray);
        }
    }

    public void update(TwoHourNotificationPreference preference) {
        _setCurrent(preference.toInt());
    }

    public void update(DayNotificationPreference preference) {
        _setCurrent(preference.toInt());
    }
}
