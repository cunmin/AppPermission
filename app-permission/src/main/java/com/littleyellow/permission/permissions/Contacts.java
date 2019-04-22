package com.littleyellow.permission.permissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.littleyellow.permission.AppPermission;

public class Contacts implements IPermission {
    @Override
    public String getName() {
        return Manifest.permission.WRITE_CONTACTS;
    }

    @Override
    public boolean isGrantedCompat(AppPermission appPermission) {
        return ContextCompat.checkSelfPermission(appPermission.getContext(),getName())== PackageManager.PERMISSION_GRANTED;
    }


}
