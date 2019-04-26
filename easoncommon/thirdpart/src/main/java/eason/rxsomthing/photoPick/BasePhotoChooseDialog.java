package eason.rxsomthing.photoPick;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;

import eason.rxsomthing.photoPick.custom.PhotoManager;
import eason.rxsomthing.photoPick.matisse.MaPhotoManager;


/**
 * 具有拍照功能的dialog 需要自定义继承此类即可
 */
public class BasePhotoChooseDialog extends Dialog {
    private Activity mActivity;
    private int maxCount;//最多选择的数量

    public BasePhotoChooseDialog(@NonNull Activity context, int maxChooseCount, PhotoCallback callback) {
        super(context);
        this.mActivity = context;
        this.maxCount = maxChooseCount;
        PhotoManager.setmPhotoCallback(callback);
    }

    //拍照
    protected void takePhoto(boolean system, boolean needClip) {
        if (system) {
            PhotoManager.takePhoto(mActivity,needClip);
        } else {
            MaPhotoManager.takePhoto(mActivity, 1);
        }
    }

    //图库选择
    protected void choosePhoto(boolean system, boolean needClip) {
        if (needClip) {
            PhotoManager.choosePhoto(mActivity,needClip);
            return;
        }
        if (system) {
            PhotoManager.choosePhoto(mActivity, needClip);
        } else {
            MaPhotoManager.pickPhoto(mActivity, maxCount);
        }
    }


}
