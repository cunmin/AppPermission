package com.littleyellow.permission;

public interface PermissionStrategy {

    boolean isGranted(String permission);

    boolean shouldShowRequestPermissionRationale(String permission);

    void requestPermission(IPermissionCallback callback,String... permission);

}
