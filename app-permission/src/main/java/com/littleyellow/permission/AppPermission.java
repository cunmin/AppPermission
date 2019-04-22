package com.littleyellow.permission;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.littleyellow.permission.permissions.IPermission;

import java.util.ArrayList;
import java.util.List;

import static com.littleyellow.permission.utils.Utils.isMarshmallow;

public class AppPermission {

    PermissionStrategy strategy;

    PermissionCallback callback;

    PermissionBefore permissionBefore;

    Context context;

    List<IPermission> reqs;

    List<String> needRequest = new ArrayList<>();

    private AppPermission(Builder builder) {
        strategy = builder.strategy;
        callback = builder.callback;
        permissionBefore = builder.permissionBefore;
        context = builder.context;
        reqs = builder.iPermissions;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public void request(){
        if (reqs == null || reqs.size() == 0){
            if(null!=callback){
                callback.onSuccess();
            }
            return;
        }

        if(isMarshmallow(context)){
            for (IPermission permission : reqs) { //过滤调已经申请过的权限
                if (!strategy.isGranted(permission.getName())) {
                    needRequest.add(permission.getName());
                }
            }
        }else{
            for (IPermission permission : reqs) { //过滤调已经申请过的权限
                if (!permission.isGrantedCompat(this)) {
                    needRequest.add(permission.getName());
                }
            }

            if(!needRequest.isEmpty()){
                //不支持动态权限机制，并且没权限说明，用户在应用设置里把权限勾掉或国产手机系统弹窗用户点了拒绝
                //这时应该引导用户到应用设置里开启权限
                if(null!=callback){
                    Uri packageURI = Uri.parse("package:" + context.getPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    callback.onAskNeverAgain(intent,needRequest);
                }
                return;
            }
        }

        if (needRequest.isEmpty()) {//全部权限都已经申请过，直接执行操作
            if(null!=callback){
                callback.onSuccess();
            }
        } else if(null!=permissionBefore){
            permissionBefore.requestBefore(needRequest, new PermissionBefore.Action() {
                @Override
                public void next() {
                    startRequest();
                }
            });
        } else{
            startRequest();
        }
    }

    private void startRequest(){
        strategy.requestPermission(new IPermissionCallback() {
            @Override
            public void onSuccess(List<String> permissions) {
                if(null==callback){
                    return;
                }
                List<String> failurePermissions = new ArrayList<>();
                List<String> askNeverAgainPermissions = new ArrayList<>();
                for (String permission: permissions) {
                    if (!strategy.isGranted(permission)) {
                        if (strategy.shouldShowRequestPermissionRationale(permission)) {
                            failurePermissions.add(permission);
                        } else {
                            askNeverAgainPermissions.add(permission);
                        }
                    }
                }
                if (failurePermissions.size() > 0 ) {
                    callback.onFailure(failurePermissions);
                }

                if (askNeverAgainPermissions.size() > 0){
                    Uri packageURI = Uri.parse("package:" + context.getPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    callback.onAskNeverAgain(intent,askNeverAgainPermissions);
                }

                if (failurePermissions.size() == 0 && askNeverAgainPermissions.size() == 0){
                    callback.onSuccess();
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                callback.onFailure(null);
            }
        }, needRequest.toArray(new String[needRequest.size()]));
    }

    @TargetApi(23)
    public boolean isGranted(String permission){
        return null==strategy?null:strategy.isGranted(permission);
    }

    @TargetApi(23)
    public boolean isAskNeverAgain(String permission) {
        return null==strategy?null:strategy.shouldShowRequestPermissionRationale(permission);
    }

    public Context getContext() {
        return context;
    }

    public static final class Builder {
        private PermissionStrategy strategy;
        private PermissionCallback callback;
        private PermissionBefore permissionBefore;
        private Context context;
        private List<Class> reqs = new ArrayList<>();
        private List<IPermission> iPermissions;

        private Builder() {
        }

        public Builder strategy(PermissionStrategy val) {
            strategy = val;
            return this;
        }

        public Builder callback(PermissionCallback val) {
            callback = val;
            return this;
        }

        public Builder permissionBefore(PermissionBefore val) {
            permissionBefore = val;
            return this;
        }

        public <T extends IPermission> Builder add(Class<T> permission){
            reqs.add(permission);
            return this;
        }

        public AppPermission build(Context context) {
            this.context = context;
            if(null == context){
                throw new IllegalArgumentException("上下文参数context是必须参数");
            }

            if(isMarshmallow(context)&&null==strategy){
                throw new IllegalArgumentException("支持动态权限机制需要提供PermissionStrategy接口的实现类");
            }

            if(!this.reqs.isEmpty()){
                iPermissions = new ArrayList<>(reqs.size());
                for(Class clz:reqs){
                    try{
                        IPermission permission = (IPermission)Class.forName(clz.getName()).newInstance();
                        iPermissions.add(permission);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            return new AppPermission(this);
        }
    }
}
