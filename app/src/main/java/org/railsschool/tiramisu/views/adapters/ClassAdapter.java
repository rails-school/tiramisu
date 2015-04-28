package org.railsschool.tiramisu.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coshx.chocolatine.helpers.ViewHelper;
import com.coshx.chocolatine.widgets.SmartAdapter;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.bll.BusinessFactory;
import org.railsschool.tiramisu.views.events.ClassDetailsRequestedEvent;
import org.railsschool.tiramisu.views.events.ErrorEvent;
import org.railsschool.tiramisu.views.helpers.DateHelper;
import org.railsschool.tiramisu.views.helpers.UserHelper;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @class ClassAdapter
 * @brief
 */
public class ClassAdapter extends SmartAdapter<String> {
    public ClassAdapter(List<String> items, Context context) {
        super(items, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View adapter, separator;
        TextView headline, digest, teacherName, date, location;
        String lessonSlug;

        adapter = recycle(convertView, R.layout.adapter_class, parent);
        headline = ViewHelper.findById(adapter, R.id.adapter_class_headline);
        digest = ViewHelper.findById(adapter, R.id.adapter_class_digest);
        teacherName = ViewHelper.findById(adapter, R.id.adapter_class_teacher);
        date = ViewHelper.findById(adapter, R.id.adapter_class_date);
        location = ViewHelper.findById(adapter, R.id.adapter_class_location);

        separator = ViewHelper.findById(adapter, R.id.adapter_class_separator);

        lessonSlug = itemAt(position);

        BusinessFactory
            .provideLesson(getContext())
            .getTuple(
                lessonSlug,
                (lesson, teacher, venue) -> {
                    headline.setText(lesson.getTitle());
                    digest.setText(lesson.getSummary());
                    date.setText(DateHelper.makeFriendly(lesson.getStartTime()));

                    teacherName.setText(UserHelper.getDisplayedName(teacher));
                    location.setText(venue.getName());
                },
                (error) -> {
                    EventBus.getDefault().post(new ErrorEvent(error));
                }
            );

        adapter.setOnClickListener(
            (v) -> {
                EventBus.getDefault().post(new ClassDetailsRequestedEvent(lessonSlug));
            }
        );

        if (position == 0) {
            // Show separator only for upcoming class
            separator.setVisibility(View.VISIBLE);
        } else {
            separator.setVisibility(View.GONE);
        }

        return adapter;
    }
}
