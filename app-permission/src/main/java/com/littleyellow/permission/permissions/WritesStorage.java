package com.littleyellow.permission.permissions;

import android.Manifest;
import android.os.Environment;

import com.littleyellow.permission.AppPermission;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class WritesStorage implements IPermission {
    @Override
    public String getName() {
        return Manifest.permission.WRITE_EXTERNAL_STORAGE;
    }

    @Override
    public boolean isGrantedCompat(AppPermission appPermission) {
        try {
            String testPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/permission_test.txt";
            File testFile = new File(testPath);
            if(null!=testFile&&!testFile.exists()){
                testFile.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile,true));
            writer.write("1");
            writer.close();
            boolean handle = testFile.delete();
            boolean readMounted = isSDCardPresent();
            if(handle&&!readMounted){
                return false;
            }else if(readMounted&&!handle){//没挂SD卡
                return true;
            }else{
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean isSDCardPresent() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


}
