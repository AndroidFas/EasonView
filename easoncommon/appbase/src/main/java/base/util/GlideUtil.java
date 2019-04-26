package base.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * glide图片加载封装
 */
public class GlideUtil {
    public static void load(Context context, ImageView imageView, int placeholder, String url) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholder);//这里可自己添加占位图
        Glide.with(context)
                .asBitmap()  // some .jpeg files are actually gif
                .load(url)
                .apply(options)
                .into(imageView);
    }

    public static void load(Context context, ImageView imageView, int placeholder, File file) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholder);//这里可自己添加占位图
        Glide.with(context)
                .asBitmap()  // some .jpeg files are actually gif
                .load(file)
                .apply(options)
                .into(imageView);
    }
}
