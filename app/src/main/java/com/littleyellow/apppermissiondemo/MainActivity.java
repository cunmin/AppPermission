package com.littleyellow.apppermissiondemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.littleyellow.permission.AppPermission;
import com.littleyellow.permission.PermissionBefore;
import com.littleyellow.permission.PermissionCallback;
import com.littleyellow.permission.permissions.Calendar;
import com.littleyellow.permission.permissions.Camera;
import com.littleyellow.permission.permissions.PhoneState;
import com.littleyellow.permission.permissions.WritesStorage;
import com.squareup.leakcanary.LeakCanary;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LeakCanary.install(getApplication());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_sd_card)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSdPermission();
                    }
                });
        findViewById(R.id.btn_device_id)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPhonePermission();
                    }
                });
        findViewById(R.id.btn_camera)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getCamera();
                    }
                });
        findViewById(R.id.btn_calenda)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getCalenda();
                    }
                });

        TextView text_view = (TextView) findViewById(R.id.text_view);
        text_view.setText(isSDCardPresent()+
                "---" +       ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)+
                "---"+ (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED));
        Button btn = (Button) findViewById(R.id.btn_device_id);
        btn.setText(getDeviceId());

        final TextView tv_test = (TextView) findViewById(R.id.tv_test);
        tv_test.post(new Runnable() {
            @Override
            public void run() {
                tv_test.setTextSize(TypedValue.COMPLEX_UNIT_PX,tv_test.getHeight());
            }
        });

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.testRelative);
    }



    public static boolean isSDCardPresent() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private void getSdPermission(){
        AppPermission.newBuilder()
                .add(WritesStorage.class)
                .strategy(new Rxpermission(this))
                .permissionBefore(new PermissionBefore() {
                    @Override
                    public void requestBefore(List<String> permissions, final Action action) {
                        new MaterialDialog.Builder(MainActivity.this)
                                .title("需要你的SD卡权限")
                                .content("需要你的SD卡权限")
                                .positiveText("确定")
                                .cancelable(false)
                                .canceledOnTouchOutside(false)
//                                .negativeText(isForce?"退出":"暂不")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        action.next();
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                })
                .callback(new PermissionCallback() {
                    @Override
                    public void onSuccess() {
                        TextView text_view = (TextView) findViewById(R.id.text_view);
                        text_view.setText(isSDCardPresent()+
                                "---" +       ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)+
                                "---"+ (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED));
                        Toast.makeText(MainActivity.this,"获取权限成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(List<String> permissions) {

                    }

                    @Override
                    public void onAskNeverAgain(final Intent intent, List<String> permissions) {
                        new MaterialDialog.Builder(MainActivity.this)
                                .title("需要你的SD卡权限")
                                .content("到应该权限界面勾选权限")
                                .positiveText("设置")
//                                .cancelable(false)
//                                .canceledOnTouchOutside(false)
//                                .negativeText(isForce?"退出":"暂不")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        startActivity(intent);
                                        dialog.dismiss();
//                                        finish();
                                    }
                                })
                                .show();
                    }
                })
                .build(this)
                .request();
    }

    public String getDeviceId(){
        try {
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String deviceId = telephonyManager.getDeviceId();
            return deviceId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getPhonePermission(){
        AppPermission.newBuilder()
                .add(PhoneState.class)
                .strategy(new Rxpermission(this))
                .permissionBefore(new PermissionBefore() {
                    @Override
                    public void requestBefore(List<String> permissions, final Action action) {
                        new MaterialDialog.Builder(MainActivity.this)
                                .title("需要你的读取手机状态权限")
                                .content("需要你的读取手机状态权限")
                                .positiveText("确定")
                                .cancelable(false)
                                .canceledOnTouchOutside(false)
//                                .negativeText(isForce?"退出":"暂不")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        action.next();
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                })
                .callback(new PermissionCallback() {
                    @Override
                    public void onSuccess() {
                        Button btn = (Button) findViewById(R.id.btn_device_id);
                        btn.setText(getDeviceId());
                        Toast.makeText(MainActivity.this,"获取权限成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(List<String> permissions) {

                    }

                    @Override
                    public void onAskNeverAgain(final Intent intent, List<String> permissions) {
                        new MaterialDialog.Builder(MainActivity.this)
                                .title("需要你的读取手机状态权限")
                                .content("到应该权限界面勾选权限")
                                .positiveText("设置")
//                                .cancelable(false)
//                                .canceledOnTouchOutside(false)
//                                .negativeText(isForce?"退出":"暂不")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        startActivity(intent);
                                        dialog.dismiss();
//                                        finish();
                                    }
                                })
                                .show();
                    }
                })
                .build(this)
                .request();
    }

    private void getCamera(){
        AppPermission.newBuilder()
                .add(Camera.class)
                .strategy(new Rxpermission(this))
                .permissionBefore(new PermissionBefore() {
                    @Override
                    public void requestBefore(List<String> permissions, final Action action) {
                        new MaterialDialog.Builder(MainActivity.this)
                                .title("需要你的相机权限")
                                .content("需要你的相机权限")
                                .positiveText("确定")
                                .cancelable(false)
                                .canceledOnTouchOutside(false)
//                                .negativeText(isForce?"退出":"暂不")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        action.next();
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                })
                .callback(new PermissionCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this,"获取相机权限成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(List<String> permissions) {

                    }

                    @Override
                    public void onAskNeverAgain(final Intent intent, List<String> permissions) {
                        new MaterialDialog.Builder(MainActivity.this)
                                .title("需要你的相机卡权限")
                                .content("到应该权限界面勾选权限")
                                .positiveText("设置")
//                                .cancelable(false)
//                                .canceledOnTouchOutside(false)
//                                .negativeText(isForce?"退出":"暂不")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        startActivity(intent);
                                        dialog.dismiss();
//                                        finish();
                                    }
                                })
                                .show();
                    }
                })
                .build(this)
                .request();
    }

    private void getCalenda() {
        AppPermission.newBuilder()
                .add(Calendar.class)
                .strategy(new Rxpermission(this))
                .permissionBefore(new PermissionBefore() {
                    @Override
                    public void requestBefore(List<String> permissions, final Action action) {
                        new MaterialDialog.Builder(MainActivity.this)
                                .title("需要你的日历权限")
                                .content("需要你的日历权限")
                                .positiveText("确定")
                                .cancelable(false)
                                .canceledOnTouchOutside(false)
//                                .negativeText(isForce?"退出":"暂不")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        action.next();
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                })
                .callback(new PermissionCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this,"获取相日历限成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(List<String> permissions) {

                    }

                    @Override
                    public void onAskNeverAgain(final Intent intent, List<String> permissions) {
                        new MaterialDialog.Builder(MainActivity.this)
                                .title("需要你的日历权限")
                                .content("到应该权限界面勾选权限")
                                .positiveText("设置")
//                                .cancelable(false)
//                                .canceledOnTouchOutside(false)
//                                .negativeText(isForce?"退出":"暂不")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        startActivity(intent);
                                        dialog.dismiss();
//                                        finish();
                                    }
                                })
                                .show();
                    }
                })
                .build(this)
                .request();

    }
}
