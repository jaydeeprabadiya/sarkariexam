package com.sarkarinaukri.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sarkarinaukri.R;
import com.sarkarinaukri.model.Painting;
import com.sarkarinaukri.model.QuestionList;

public class GlideHelper {

    private GlideHelper() {}

    public static void loadPaintingImage(ImageView image, QuestionList.data painting) {
        Glide.with(image.getContext().getApplicationContext())
                .load(painting.file)
                .placeholder(R.drawable.ic_no_img)
                .skipMemoryCache(true)
                .override(500,300)
                .into(image);
    }

}
