package com.littleyellow.permission;

import android.content.Intent;

import java.util.List;

public interface PermissionCallback {

    /**
     * 用户拒绝了权限请求, 权限请求失败, 但还可以继续请求该权限
     * @param permissions 请求失败的权限名
     */
    void onFailure(List<String> permissions);

    /**
     * 用户拒绝了权限请求并且用户选择了以后不再询问, 权限请求失败, 这时将不能继续请求该权限, 需要提示用户进入设置页面打开该权限
     * @param intent 跳到系统的设置界面的Intent,用来引导用户到应用设置里开启权限
     * @param permissions 请求失败的权限名
     */
    void onAskNeverAgain(Intent intent,List<String> permissions);

    /**
     * 权限请求成功
     */
    void onSuccess();

}
