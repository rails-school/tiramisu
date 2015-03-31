package org.railsschool.tiramisu.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coshx.chocolatine.helpers.ViewHelper;
import com.coshx.chocolatine.widgets.SmartAdapter;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.bll.BusinessFactory;
import org.railsschool.tiramisu.views.events.ErrorEvent;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @class ClassAdapter
 * @brief
 */
public class ClassAdapter extends SmartAdapter<Integer> {
    public ClassAdapter(List<Integer> items, Context context) {
        super(items, context);
    }

    private String _getTeacherIntro(User teacher) {
        return String.format(
            getContext().getString(
                R.string.class_teacher_introduction,
                teacher.getName()
            )
        );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View adapter;
        TextView headline, digest, teacherIntro;

        adapter = recycle(convertView, R.layout.adapter_class, parent);
        headline = ViewHelper.findById(adapter, R.id.adapter_class_headline);
        digest = ViewHelper.findById(adapter, R.id.adapter_class_digest);
        teacherIntro = ViewHelper.findById(adapter, R.id.adapter_class_teacher);

        BusinessFactory
            .provideLesson(getContext())
            .getPair(
                itemAt(position),
                (lesson, teacher) -> {
                    headline.setText(lesson.getTitle());
                    digest.setText(lesson.getSummary());
                    teacherIntro.setText(_getTeacherIntro(teacher));
                },
                (newLesson) -> {
                    if (!headline.getText().equals(newLesson.getTitle())) {
                        headline.setText(newLesson.getTitle());
                    }

                    if (!digest.getText().equals(newLesson.getSummary())) {
                        headline.setText(newLesson.getSummary());
                    }
                },
                (newUser) -> {
                    if (!teacherIntro.getText().equals(newUser.getName())) {
                        teacherIntro.setText(_getTeacherIntro(newUser));
                    }
                },
                (error) -> {
                    EventBus.getDefault().post(new ErrorEvent(error));
                }
            );

        return adapter;
    }
}
