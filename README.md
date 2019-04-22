# AppPermission
动态申请权限，封装了申请权限框架， 方便使用自己项目的申请框架，封装了申请权限前说明申请权限的用途（UI自己实现），兼容了6.0系统以下或targetSDK<23的读写SD卡和获取设备id的
其他权限不兼容也可以(少数机型会申请权限回调和判断权限状态会有问题)
## Setup

要使用这个库 `minSdkVersion`  >= 14
```gradle
allprojects {
    repositories {
        ...
        jcenter()//一般android studio新建项目都会自动加这行引进这个仓库的
    }
}

dependencies {
    implementation XXX
}
```

## Usage
新建一个申请类实现PermissionStrategy，类名随意写，实现三个方法，在方法里用自己项目的申请框架实现即可，参考demo用RxPermission
```
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
```

申请时调用
```
AppPermission.newBuilder()
                .add(WritesStorage.class)
                .strategy(new Rxpermission(this))//上面新建类的对像
                .permissionBefore(new PermissionBefore() {//申请权限前的操作，如弹窗提示权限的用途，不提示则不设置即可
                    @Override
                    public void requestBefore(List<String> permissions, final Action action) {
                        new MaterialDialog.Builder(MainActivity.this)
                                .title("需要你的SD卡权限")
                                .content("需要你的SD卡权限")
                                .positiveText("确定")
                                .cancelable(false)
                                .canceledOnTouchOutside(false)
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
                        Toast.makeText(MainActivity.this,"获取权限成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(List<String> permissions) {
                        Toast.makeText(MainActivity.this,"获取权限失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAskNeverAgain(final Intent intent, List<String> permissions) {//跳到系统的设置界面的Intent,用来引导用户到应用设置里开启权限
                        new MaterialDialog.Builder(MainActivity.this)
                                .title("需要你的SD卡权限")
                                .content("到应该权限界面勾选权限")
                                .positiveText("设置")
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
```

## instructions
上面的WritesStorage对应Manifest.permission.WRITE_EXTERNAL_STORAGE权限，有`WritesStorage`、`PhoneState`、Sensors、SendSMS、RecordAudio、Location、
Location、Camera、Calendar。

如果没有你需要的就自己新建一个类实现IPermission接口即可

getName()方法返回权限名。

isGrantedCompat(AppPermission appPermission)方法返回6.0以下的判断权限结果，不知道就直接return ContextCompat.checkSelfPermission(appPermission.getContext(),getName())==PackageManager.PERMISSION_GRANTED;

# License
```
Copyright (C) 2019, 小黄
  
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at 
 
       http://www.apache.org/licenses/LICENSE-2.0 

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
