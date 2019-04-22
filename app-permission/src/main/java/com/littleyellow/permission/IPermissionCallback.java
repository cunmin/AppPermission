package com.littleyellow.permission;

import java.util.List;

public interface IPermissionCallback {
    void onSuccess(List<String> permissions);

    void onFailed(Throwable throwable);

}
