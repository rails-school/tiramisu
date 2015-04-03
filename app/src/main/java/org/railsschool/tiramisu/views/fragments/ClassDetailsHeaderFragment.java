package org.railsschool.tiramisu.views.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.bll.BusinessFactory;
import org.railsschool.tiramisu.views.events.ClassDetailsHeaderBackEvent;
import org.railsschool.tiramisu.views.events.ClassDetailsHeaderInitEvent;
import org.railsschool.tiramisu.views.helpers.AnimationHelper;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * @class ClassDetailsHeaderFragment
 * @brief
 */
public class ClassDetailsHeaderFragment extends BaseFragment {
    private ClassDetailsHeaderInitEvent _initEvent;

    private void _engineShareAction(Lesson lesson, int itemId) {
        String msg = "Hey, I am going to that class: " + lesson.getTitle() + ". Join me!";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url = getString(R.string.api_endpoint) + "/l/" + lesson.getSlug();

        if (itemId == R.id.dialog_share_text) {
            intent.setData(Uri.parse("sms:"));

            if (msg.length() > 150) {
                msg = msg.substring(0, 150);
            }

            intent.putExtra("sms_body", msg);
        } else if (itemId == R.id.dialog_share_email) {
            intent.setData(
                Uri.parse(
                    "mailto:?subject=" +
                    getString(R.string.app_name) +
                    "&body=" + msg + "\n\n" + url
                )
            );
        } else if (itemId == R.id.dialog_share_facebook) {
            intent.setData(
                Uri.parse("https://www.facebook.com/sharer/sharer.php?u=" + url)
            );
        } else if (itemId == R.id.dialog_share_twitter) {
            intent.setData(
                Uri.parse(
                    "https://twitter.com/intent/tweet?text=" + lesson.getTitle() + url
                )
            );
        }

        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(
            R.layout.fragment_class_details_header, container,
            false
        );
        ButterKnife.inject(this, fragment);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (_initEvent != null) {
            EventBus.getDefault().postSticky(_initEvent);
        }
    }

    public void onEventMainThread(ClassDetailsHeaderInitEvent event) {
        _initEvent = event;
    }

    @OnClick(R.id.fragment_class_details_header_back)
    public void onBack(View view) {
        AnimationHelper.pressed(view);
        EventBus.getDefault().post(new ClassDetailsHeaderBackEvent());
    }

    @OnClick(R.id.fragment_class_details_header_share)
    public void onShare(View view) {
        if (_initEvent == null) {
            return; // Prevent null exception
        }

        AnimationHelper.pressed(view);

        new DialogPlus.Builder(getActivity())
            .setContentHolder(new ViewHolder(R.layout.dialog_share))
            .setGravity(DialogPlus.Gravity.CENTER)
            .setOnClickListener(
                (dialogPlus, item) -> {
                    BusinessFactory
                        .provideLesson(getActivity())
                        .get(
                            _initEvent.getLessonSlug(),
                            (lesson) -> {
                                _engineShareAction(lesson, item.getId());
                                dialogPlus.dismiss();
                            },
                            (error) -> {
                                publishError(error);
                                dialogPlus.dismiss();
                            }
                        );
                }
            )
            .create()
            .show();
    }
}
