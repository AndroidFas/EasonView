package eason.rxsomthing.photoPick;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import eason.rxsomthing.R;


public class PhotoPickDialog extends BasePhotoChooseDialog {

    public PhotoPickDialog(@NonNull Activity context, int max, boolean needClip, PhotoCallback callback) {
        super(context, max, callback);
        setContentView(R.layout.dialog_photo_pick);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = context.getResources().getDisplayMetrics().widthPixels;
        params.windowAnimations = R.style.BottomWindowAnimation;
        //        不能少！少了这个属性好像即使设置了宽度为屏幕宽度依旧不能全屏
        getWindow().setBackgroundDrawable(null);
        initClick(needClip);
    }

    private void initClick(final boolean needClip) {
        findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(true,needClip);
                dismiss();
            }
        });
        findViewById(R.id.tv_photo_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto(false,needClip);
                dismiss();
            }
        });
        findViewById(R.id.tv_photo_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
