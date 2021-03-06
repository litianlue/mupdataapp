package com.hanma.myappup.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;


import com.hanma.myappup.customview.ConfirmDialog;
import com.hanma.myappup.feature.Callback;

import androidx.core.content.ContextCompat;


/**
 * Updata by Litianlue on 2020/05/15.
 */
public class UpdateAppUtils {

    private final String TAG = "UpdateAppUtils";
    public static final int CHECK_BY_VERSION_NAME = 1001;
    public static final int CHECK_BY_VERSION_CODE = 1002;
    public static final int DOWNLOAD_BY_APP = 1003;
    public static final int DOWNLOAD_BY_BROWSER = 1004;

    private Activity activity;
    private int checkBy = CHECK_BY_VERSION_CODE;
    private int downloadBy = DOWNLOAD_BY_APP;
    private int serverVersionCode = 0;
    private String apkPath="";
    private String serverVersionName="";
    private boolean isForce = false; //是否强制更新
    private int localVersionCode = 0;
    private String localVersionName="";

    private UpdateAppUtils(Activity activity) {
        this.activity = activity;
        getAPPLocalVersion(activity);
    }

    public static UpdateAppUtils from(Activity activity){
        return new UpdateAppUtils(activity);
    }

    public UpdateAppUtils checkBy(int checkBy){
        this.checkBy = checkBy;
        return this;
    }

    public UpdateAppUtils apkPath(String apkPath){
        this.apkPath = apkPath;
        return this;
    }

    public UpdateAppUtils downloadBy(int downloadBy){
        this.downloadBy = downloadBy;
        return this;
    }

    public UpdateAppUtils serverVersionCode(int serverVersionCode){
        this.serverVersionCode = serverVersionCode;
        return this;
    }

    public UpdateAppUtils serverVersionName(String  serverVersionName){
        this.serverVersionName = serverVersionName;
        return this;
    }

    public UpdateAppUtils isForce(boolean  isForce){
        this.isForce = isForce;
        return this;
    }

    //获取apk的版本号 currentVersionCode
    private  void getAPPLocalVersion(Context ctx) {
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            localVersionName = info.versionName; // 版本名
            localVersionCode = info.versionCode; // 版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void update(){

        switch (checkBy){
            case CHECK_BY_VERSION_CODE:
                if (serverVersionCode >localVersionCode){
                    toUpdate();
                }else {
                    Log.i(TAG,"当前版本是最新版本"+serverVersionCode+"/"+serverVersionName);
                }
                break;

            case CHECK_BY_VERSION_NAME:
                if (!serverVersionName.equals(localVersionName)){
                    toUpdate();
                }else {
                    Log.i(TAG,"当前版本是最新版本"+serverVersionCode+"/"+serverVersionName);
                }
                break;
        }

    }

    private void toUpdate() {

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            realUpdate();
        }else {//申请权限
            Toast.makeText(activity, "请申请读写SD卡权限", Toast.LENGTH_SHORT).show();
        }

    }

    private void realUpdate() {
        ConfirmDialog dialog = new ConfirmDialog(activity, new Callback() {
            @Override
            public void callback(int position) {
                switch (position){
                    case 0:  //cancle
                        if (isForce)activity.finish();
                        break;

                    case 1:  //sure
                        if (downloadBy == DOWNLOAD_BY_APP) {
                            com.hanma.mupdataapp.util.DownloadAppUtils.downloadForAutoInstall(activity, apkPath, "demo.apk", serverVersionName);
                        }else if (downloadBy == DOWNLOAD_BY_BROWSER){
                            com.hanma.mupdataapp.util.DownloadAppUtils.downloadForWebView(activity,apkPath);
                        }
                        break;
                }
            }
        });
        dialog .setContent("发现新版本:"+serverVersionName+"\n是否下载更新?");
        dialog.setCancelable(false);
        dialog.show();
    }



}
