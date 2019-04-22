package com.littleyellow.permission.permissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.littleyellow.permission.AppPermission;

public class Location implements IPermission {
    @Override
    public String getName() {
        return Manifest.permission.ACCESS_FINE_LOCATION;
    }

    @Override
    public boolean isGrantedCompat(AppPermission appPermission) {
        return ContextCompat.checkSelfPermission(appPermission.getContext(),getName())== PackageManager.PERMISSION_GRANTED;
    }


}
