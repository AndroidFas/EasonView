package base;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.util.FileUtil;
import base.util.Logger;
import base.util.Toaster;
import eason.rxsomthing.http.AlertDialogUtil;
import io.reactivex.subjects.PublishSubject;
import rxsomthing.bus.LifeCycleEvent;

/**
 * cyj  act 基类
 */
public class BaseActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST = 0x00001213;
    private static final int PERMISSION_REQUEST_SETTING = 0x00001215;
    private static final int GPS_REQUEST = 0x00001214;
    public static final int REQUEST_INSTALL_PACKAGES = 0x00001217;

    //管理生命周期的Subject
    protected final PublishSubject<LifeCycleEvent> mLifeCycleEventPublishSubject = PublishSubject.create();
    //默认加载框
    public AlertDialogUtil mAlertDialogUtil;

    /*=====================生命周期管理 begin======================*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlertDialogUtil = new AlertDialogUtil(this);
        mLifeCycleEventPublishSubject.onNext(LifeCycleEvent.CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLifeCycleEventPublishSubject.onNext(LifeCycleEvent.RESUME);
    }

    @Override
    protected void onPause() {
        mLifeCycleEventPublishSubject.onNext(LifeCycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mLifeCycleEventPublishSubject.onNext(LifeCycleEvent.DESTROY);
        super.onDestroy();
    }
    /*=====================生命周期管理 end======================*/

    /*=====================权限申请维护 begin======================*/
    private Map<String, String> permissionMap = new HashMap<String, String>() {{
        put(Manifest.permission.ACCESS_COARSE_LOCATION, "访问粗略地理位置");
        put(Manifest.permission.ACCESS_FINE_LOCATION, "访问精细地理位置");
        put(Manifest.permission.CAMERA, "相机权限");
        put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "写入sd卡");
        put(Manifest.permission.READ_EXTERNAL_STORAGE, "读取sd卡");
        put(Manifest.permission.RECORD_AUDIO, "麦克风权限");
    }};//权限值和对应描述

    List<String> stillNeeds = new ArrayList<>();
    private boolean forverNoGrant = false;//是否有永久拒绝权限
    private List<String> forverNoGrants = new ArrayList<>();//被永久拒绝的权限

    //权限申请回调
    protected abstract class PermissionCallback {
        //请求权限之前的操作(boolean 返回是否要继续请求下去)
        public abstract boolean beforeRequest();

        //权限通过的回调
        public abstract void pass();

        //权限被永久拒绝的回调
        public void forverNoGrant() {
            showPermissionDialog();
        }
    }

    protected interface GpsCallback {
        boolean beforeRequest(boolean hasOpen);

        void reqGPSResult(boolean hasOpen);

    }

    private PermissionCallback mPermissionCallback;
    private GpsCallback mGpsCallback;
    private String[] permissions;

    /**
     * 一般权限
     *
     * @param permissions
     * @param callback
     */
    protected void requestPermissions(String[] permissions, PermissionCallback callback) {
        this.permissions = permissions;
        mPermissionCallback = callback;
        boolean go = mPermissionCallback.beforeRequest();
        if (!go)
            return;
        //判断所需权限里没通过的
        stillNeeds.clear();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                stillNeeds.add(permission);
            }
        }
        if (stillNeeds.size() > 0) {
            String[] permissionses = stillNeeds.toArray(new String[stillNeeds.size()]);//将List转为数组
            ActivityCompat.requestPermissions(this, permissionses, PERMISSION_REQUEST);
        }
        if (stillNeeds.isEmpty())
            mPermissionCallback.pass();
    }

    private void showPermissionDialog() {
        StringBuilder sb = new StringBuilder("已禁用如下权限，请手动授权\n");
        for (int i = 0; i < forverNoGrants.size(); i++) {
            String noGrant = forverNoGrants.get(i);
            sb.append(i + 1).append(".")
                    .append(permissionMap.get(noGrant));
            if (i != forverNoGrants.size() - 1) {
                sb.append("\n");
            }
        }
        AlertDialog mAlertDialog = new AlertDialog.Builder(this)
                .setMessage(sb.toString())
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Uri packageURI = Uri.parse("package:" + FileUtil.getPackage(BaseActivity.this));
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        startActivityForResult(intent, PERMISSION_REQUEST_SETTING);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toaster.s(BaseActivity.this, "请到设置打开相关权限哦");
                        finish();
                    }
                }).create();
        mAlertDialog.setCanceledOnTouchOutside(false);
        if (!mAlertDialog.isShowing())
            mAlertDialog.show();
    }

    /**
     * gps权限
     */
    protected void requestGps(GpsCallback gpsCallback) {
        mGpsCallback = gpsCallback;
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean go = mGpsCallback.beforeRequest(ok);
        if (!go)
            return;
        //gp开关
        if (!ok) {
            Toast.makeText(this, "您未开启GPS定位服务,请开启高精度定位", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, GPS_REQUEST);
        } else {
            mGpsCallback.reqGPSResult(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST) {
            Logger.d("PermissionsResult", "grantResults.length:" + grantResults.length);
            stillNeeds.clear();
            forverNoGrants.clear();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    stillNeeds.add(permissions[i]);
//                    //判断是否勾选禁止后不在询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                    if (!showRequestPermission) {
                        //永久禁止
                        Logger.d("PermissionsResult", "forverNoGrant:" + i);
                        forverNoGrant = true;
                        forverNoGrants.add(permissions[i]);
                    }
                }
            }
            if (!forverNoGrant) {
                if (stillNeeds.size() > 0) {
                    String[] permissionses = stillNeeds.toArray(new String[stillNeeds.size()]);//将List转为数组
                    ActivityCompat.requestPermissions(this, permissionses, PERMISSION_REQUEST);
                } else {
                    mPermissionCallback.pass();
                }
            } else {
                mPermissionCallback.forverNoGrant();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_SETTING) {
            requestPermissions(permissions, mPermissionCallback);
        }
        if (requestCode == GPS_REQUEST) {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            mGpsCallback.reqGPSResult(ok);
        }
    }

    /*=====================权限申请维护 end======================*/

    /*=====================基本操作Api======================*/
    protected <T extends View> T $(int id) {
        return findViewById(id);
    }
    /*=====================基本操作Api======================*/


}
