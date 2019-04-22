package com.littleyellow.permission;

import java.util.List;

public interface PermissionBefore {

    void requestBefore(List<String> permissions,Action action);

    interface Action{
        void next();
    }

}
