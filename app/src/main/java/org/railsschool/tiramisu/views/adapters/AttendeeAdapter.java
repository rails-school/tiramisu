package org.railsschool.tiramisu.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.coshx.chocolatine.helpers.ViewHelper;
import com.coshx.chocolatine.widgets.SmartAdapter;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.views.utils.PicassoHelper;

import java.util.List;

/**
 * @class AttendeeAdapter
 * @brief
 */
public class AttendeeAdapter extends SmartAdapter<User> {
    public AttendeeAdapter(List<User> items, Context context) {
        super(items, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View adapter;
        ImageView avatar;
        User attendee = itemAt(position);

        adapter = recycle(convertView, R.layout.adapter_attendee, parent);
        avatar = ViewHelper.findById(adapter, R.id.adapter_attendee);
        PicassoHelper.loadAvatar(getContext(), attendee, avatar);

        return adapter;
    }
}
