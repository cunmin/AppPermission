package com.littleyellow.apppermissiondemo;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;

import com.littleyellow.permission.IPermissionCallback;
import com.littleyellow.permission.PermissionStrategy;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class Rxpermission implements PermissionStrategy {

    final RxPermissions rxPermissions;
    final Activity activity;

    public Rxpermission(Activity activity) {
        this.activity = activity;
        rxPermissions = new RxPermissions(activity);
    }

    @Override
    public boolean isGranted(String permission) {
        return rxPermissions.isGranted(permission);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity,permission);
    }

    @Override
    public void requestPermission(final IPermissionCallback callback, String... permission) {
        rxPermissions
                .requestEach(permission)
                .buffer(permission.length)
                .subscribe(new Consumer<List<Permission>>() {
                    @Override
                    public void accept(List<Permission> permissions) throws Exception {
                        List<String> data = new ArrayList<>(permissions.size());
                        for(Permission item:permissions){
                            data.add(item.name);
                        }
                        callback.onSuccess(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callback.onFailed(throwable);
                    }
                });
    }
}
