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

import java.util.List;

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
                teacher.name
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
                    headline.setText(lesson.title);
                    digest.setText(lesson.summary);
                    teacherIntro.setText(_getTeacherIntro(teacher));
                },
                (newLesson) -> {
                    if (!headline.getText().equals(newLesson.title)) {
                        headline.setText(newLesson.title);
                    }

                    if (!digest.getText().equals(newLesson.summary)) {
                        headline.setText(newLesson.summary);
                    }
                },
                (newUser) -> {
                    if (!teacherIntro.getText().equals(newUser.name)) {
                        teacherIntro.setText(_getTeacherIntro(newUser));
                    }
                },
                (error) -> {

                }
            );

        return adapter;
    }
}
