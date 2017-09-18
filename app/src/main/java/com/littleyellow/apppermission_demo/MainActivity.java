package com.littleyellow.apppermission_demo;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxPermissions rxPermissions =new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CALENDAR)
                .subscribe(new Consumer<Permission>() {
                               @Override
                               public void accept(@NonNull Permission permission) throws Exception {
                                   Toast.makeText(MainActivity.this,permission.toString(),Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        });
        TextView text_view = (TextView) findViewById(R.id.text_view);
        text_view.setText("呵呵呵哥");
    }
}
