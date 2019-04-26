package eason.rxsomthing.photoPick.custom;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import eason.rxsomthing.photoPick.PhotoCallback;
import eason.rxsomthing.photoPick.PhotoUtils;
import eason.rxsomthing.photoPick.matisse.MaPhotoManager;


public class PhotoManager {
    private static final String TAG = "==PhotoManager";

    private static final int TAKE_PHOTO = 9070;//拍照
    private static final int CROP_PHOTO = 9071;//裁剪
    private static final int CHOOSE_PHOTO = 9072;//图库选择
    private static Uri imageUri;
    public static File imgFile;

    private static PhotoCallback mPhotoCallback;

    private static boolean isNeedClip = false;//需要裁剪
    private static Uri cutUri;//裁剪后的图片

    public static void setmPhotoCallback(PhotoCallback mPhotoCallback) {
        PhotoManager.mPhotoCallback = mPhotoCallback;
    }

    //拍照
    public static void takePhoto(Activity context, boolean needClip) {
        isNeedClip = needClip;
        //拍照保存的地方
        getImgPath();
//            imageUri = Uri.fromFile(imgFile);
        //新版文件uri获取方式
//            android:authorities="eason.rxsomthing.fileprovider"
        Log.d(TAG, "filePath:" + imgFile.getAbsolutePath());
//        Log.d(TAG, context.getPackageName() + ".fileProvider");
        imageUri = FileProvider.getUriForFile(context, PhotoUtils.getFileProviderName(context), imgFile);
        // 创建Intent，用于启动手机的照相机拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定输出到文件uri中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        // 启动intent开始拍照
        context.startActivityForResult(intent, TAKE_PHOTO);

    }

    public static File getImgPath() {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "camera_photos/");
        if (!dir.exists())
            dir.mkdir();
        //创建新文件
        imgFile = new File(dir, UUID.randomUUID().toString() + "_take_photo.jpg");
        return imgFile;
    }

    //图库选择
    public static void choosePhoto(Activity context, boolean needClip) {
        isNeedClip = needClip;
        // 创建Intent，用于打开手机本地图库选择图片
        Intent intent1 = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 启动intent打开本地图库
        context.startActivityForResult(intent1, CHOOSE_PHOTO);
    }


    /**
     * 拍照之后，启动裁剪
     *
     * @return
     */
    @NonNull
    private static Intent cutForCamera(Activity activity) {
        //设置裁剪之后的图片路径文件
        File cutfile = getImgPath();
        //初始化 uri
        Uri outputUri = null; //真实的 uri
        Intent intent = new Intent("com.android.camera.action.CROP");

//        outputUri = FileProvider.getUriForFile(activity, PhotoUtils.getFileProviderName(activity), cutfile);
        //裁剪后保存的图片只能用下面的fromUri 执行
        outputUri = Uri.fromFile(cutfile);
        //把这个 uri 提供出去，就可以解析成 bitmap了
        cutUri = outputUri;
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", true);
        //需要加上这两句话  ： uri 权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        // aspectX,aspectY 是宽高的比例，这里设置正方形
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //设置要裁剪的宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        //如果图片过大，会导致oom，这里设置为false
        intent.putExtra("return-data", false);
        if (imageUri != null) {
            intent.setDataAndType(imageUri, "image/*");
        }
        if (outputUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        }
        intent.putExtra("noFaceDetection", true);
        //压缩图片
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }


    /**
     * 相册裁剪
     *
     * @param activity
     * @param uri
     * @return
     */
    private static Intent cutForPhoto(Activity activity, Uri uri) {
        //直接裁剪
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置裁剪之后的图片路径文件
        File cutfile = getImgPath();
        //初始化 uri
        Uri imageUri = uri; //返回来的 uri
        Uri outputUri = null; //真实的 uri
        Log.d(TAG, "CutForPhoto: " + cutfile);
//        outputUri = FileProvider.getUriForFile(activity, PhotoUtils.getFileProviderName(activity), cutfile);
        //裁剪后保存的图片只能用下面的fromUri 执行
        outputUri = Uri.fromFile(cutfile);
        cutUri = outputUri;
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", true);
        // aspectX,aspectY 是宽高的比例，这里设置正方形
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //设置要裁剪的宽高
        intent.putExtra("outputX", 200); //200dp
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        //如果图片过大，会导致oom，这里设置为false
        intent.putExtra("return-data", false);
        if (imageUri != null) {
            intent.setDataAndType(imageUri, "image/*");
        }
        if (outputUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        }
        intent.putExtra("noFaceDetection", true);
        //压缩图片
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }

    /*=====================接管oActivityResult======================*/
    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case TAKE_PHOTO:// 拍照
                if (resultCode == -1) {
                    if (isNeedClip) {
                        // 创建intent用于裁剪图片
                        activity.startActivityForResult(cutForCamera(activity), CROP_PHOTO);
                    } else {
                        List<File> files = new ArrayList();
                        files.add(imgFile);
                        mPhotoCallback.callBackFiles(files);
                        List<Uri> uris = new ArrayList();
                        uris.add(imageUri);
                        mPhotoCallback.callBackUris(uris);
                    }
                }
                break;
            case CHOOSE_PHOTO:// 系统图库
                if (resultCode == -1) {
                    // 获取图库所选图片的uri
                    Uri uri = data.getData();
                    Log.d(TAG, "uri: ==Authority: " + uri.getAuthority() + "== path:" + uri.getPath() + " ==Scheme:" + uri.getScheme());
                    if (isNeedClip) {
                        // 启动intent，开始裁剪
                        activity.startActivityForResult(cutForPhoto(activity, uri), CROP_PHOTO);
                    } else {
                        //take_photo 开源库里的方法
//                    mPhotoCallback.callBackFile(new File(PhotoUtils.getFilePathWithUri(uri, activity)));
                        //AWS demo里的uri获取图片方法
                        List<File> files = new ArrayList();
                        files.add(new File(PhotoUtils.getPath(uri, activity)));
                        mPhotoCallback.callBackFiles(files);
                        List<Uri> uris = new ArrayList();
                        uris.add(uri);
                        mPhotoCallback.callBackUris(uris);
                    }
                }

                break;
            case CROP_PHOTO:// 裁剪后展示图片
                if (resultCode == -1) {
                    List<File> files = new ArrayList();
                    files.add(new File(PhotoUtils.getPath(cutUri, activity)));
                    mPhotoCallback.callBackFiles(files);
                    List<Uri> uris = new ArrayList();
                    uris.add(cutUri);
                    mPhotoCallback.callBackUris(uris);
                }
                break;
            case MaPhotoManager.REQUEST_CODE_CHOOSE://第三方图选择
                MaPhotoManager.onActivityResult(activity, mPhotoCallback, requestCode, resultCode, data);
                break;
        }
    }
}
