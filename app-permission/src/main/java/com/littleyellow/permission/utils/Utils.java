package com.littleyellow.permission.utils;

import android.content.Context;
import android.os.Build;

public class Utils {

    public static boolean isMarshmallow(Context context) {
        int sdkVersion = context.getApplicationInfo().targetSdkVersion;
        return Build.VERSION.SDK_INT >= 23&&sdkVersion>=23;
    }

}
