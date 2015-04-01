package org.railsschool.tiramisu.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coshx.chocolatine.helpers.ViewHelper;
import com.coshx.chocolatine.widgets.SmartAdapter;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.railsschool.tiramisu.R;
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

    private void _refreshContent(TextView textView, String value) {
        if (!textView.getText().equals(value)) {
            textView.setVisibility(View.INVISIBLE);
            textView.setText(value);
            textView.setVisibility(View.VISIBLE);
            YoYo
                .with(Techniques.FadeIn)
                .duration(500)
                .playOn(textView);
        }
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
                    teacherIntro.setText(teacher.getDisplayName());
                },
                (newLesson) -> {
                    _refreshContent(headline, newLesson.getTitle());
                    _refreshContent(digest, newLesson.getSummary());
                },
                (newUser) -> {
                    _refreshContent(teacherIntro, newUser.getDisplayName());
                },
                (error) -> {
                    EventBus.getDefault().post(new ErrorEvent(error));
                }
            );

        return adapter;
    }
}
