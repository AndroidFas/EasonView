package eason.rxsomthing.photoPick.matisse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import eason.rxsomthing.R;
import eason.rxsomthing.photoPick.PhotoCallback;
import eason.rxsomthing.photoPick.PhotoUtils;
import eason.rxsomthing.photoPick.matisse.source.Matisse;
import eason.rxsomthing.photoPick.matisse.source.MimeType;
import eason.rxsomthing.photoPick.matisse.source.engine.impl.GlideEngine;
import eason.rxsomthing.photoPick.matisse.source.internal.entity.CaptureStrategy;

/**
 * 三方图库管理
 */
public class MaPhotoManager {
    public static final int REQUEST_CODE_CHOOSE = 9080;
    public static float SCALE = 0.85f;
    private PhotoCallback mPhotoCallback;

    public MaPhotoManager(PhotoCallback photoCallback) {
        mPhotoCallback = photoCallback;
    }


    public static void takePhoto(Activity context, int max) {
        Matisse.from(context)
                .choose(MimeType.ofAll(), false)
                .countable(true)
                .capture(true) //使用拍照功能
                .captureStrategy(new CaptureStrategy(false, PhotoUtils.getFileProviderName(context))) //是否拍照功能，并设置拍照后图片的保存路径[创建文件已改成photoManager 这边设置无效]
                .maxSelectable(max)
                .gridExpectedSize(
                        context.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(SCALE)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }


    public static void pickPhoto(Activity context, int max) {
        Matisse.from(context)
                .choose(MimeType.ofAll(), false) // 选择 mime 的类型
                .countable(true)
                .maxSelectable(max) // 图片选择的最多数量
                .gridExpectedSize(context.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(SCALE) // 缩略图的比例
                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
    }

    public static void onActivityResult(Context context, PhotoCallback mPhotoCallback, int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == -1) {
            List<Uri> uris = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + uris);
            if (null != mPhotoCallback) {
                mPhotoCallback.callBackUris(uris);
                List<File> files = new ArrayList<>();
                for (Uri uri : uris) {
                    files.add(new File(PhotoUtils.getPath(uri, context)));
                }
                mPhotoCallback.callBackFiles(files);
            }

        }
    }

}
