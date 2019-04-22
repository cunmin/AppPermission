package com.littleyellow.permission.permissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.littleyellow.permission.AppPermission;

public class PhoneState implements IPermission {
    @Override
    public String getName() {
        return Manifest.permission.READ_PHONE_STATE;
    }

    @Override
    public boolean isGrantedCompat(AppPermission appPermission) {
        return !TextUtils.isEmpty(getDeviceId(appPermission.getContext()));
    }

    public String getDeviceId(Context context){
        try {
            TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String deviceId = telephonyManager.getDeviceId();
            return deviceId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SecurityException("清单文件没有申明android.permission.READ_PHONE_STATE权限");
        }
    }
}
