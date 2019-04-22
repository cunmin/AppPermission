package com.littleyellow.permission.permissions;

import com.littleyellow.permission.AppPermission;

public interface IPermission {

    String getName();

    boolean isGrantedCompat(AppPermission appPermission);

//    boolean isAskNeverAgainCompat(AppPermission appPermission);

}
