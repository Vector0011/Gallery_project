package com.example.penup.permission;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;

import com.example.penup.BuildConfig;
import com.example.penup.responsitory.ExternalStorageResponsitory;
import com.google.android.material.snackbar.Snackbar;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

public class PermissionUtil {
    private static PermissionUtil INSTANCE = null;
    private PermissionUtil(){};
    public static int PERMISSION_CODE = 123;
    public static synchronized PermissionUtil getInstance(){
        if(INSTANCE == null){
            INSTANCE = new PermissionUtil();
        }
        return INSTANCE;
    }
    public void requestPermission(Context context, PermissionListener permissionListener){
        TedPermission.with(context)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
    public void requestPermissionForSDK30(Activity activity){
        if(SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Snackbar.make(activity.findViewById(android.R.id.content), "Permission needed!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                                activity.startActivityForResult(intent, PERMISSION_CODE);
                            } catch (Exception ex) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                activity.startActivityForResult(intent, PERMISSION_CODE);
                            }
                        }
                    })
                    .show();
            }
        }
    }
}
