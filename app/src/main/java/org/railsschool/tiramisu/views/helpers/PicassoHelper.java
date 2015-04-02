package org.railsschool.tiramisu.views.helpers;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.models.beans.User;

import fr.tkeunebr.gravatar.Gravatar;

/**
 * @class PicassoHelper
 * @brief
 */
public class PicassoHelper {
    public static void loadAvatar(Context context, User user, ImageView target) {
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            String avatarUrl = Gravatar.init()
                                       .with(user.getEmail())
                                       .size(Gravatar.MAX_IMAGE_SIZE_PIXEL)
                                       .build();

            if (avatarUrl != null) {
                Picasso
                    .with(context)
                    .load(avatarUrl)
                    .placeholder(R.drawable.default_avatar)
                    .fit()
                    .centerCrop()
                    .into(target);
                return;
            }
        }

        Picasso
            .with(context)
            .load(R.drawable.default_avatar)
            .fit()
            .centerCrop()
            .into(target);
    }
}
