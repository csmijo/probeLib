package com.csmijo.probe.baseData;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by chengqianqian-xy on 2016/9/30.
 */

public class AppData {

    private static Context context;
    private static final String TAG = "AppInfo";
    private static final String UMS_APPKEY = "UMS_APPKEY";

    public static void init(Context context) {
        AppData.context = context;
    }


    public static String getAppKey() {
        String umsAppkey = "";
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (ai != null) {
                umsAppkey = ai.metaData.getString(UMS_APPKEY);
                if (umsAppkey == null)
                    Log.e(TAG,
                            "Could not read UMS_APPKEY meta-data from AndroidManifest.xml.");
            }
        } catch (Exception e) {
            Log.e(TAG,
                    "Could not read UMS_APPKEY meta-data from AndroidManifest.xml.");
        }
        return umsAppkey;
    }

    public static String getAppVersion() {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null)
                versionName = "";
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return versionName;
    }

    public static String getAppVersionCode() {
        String versionCode = "";

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pInfo = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = String.valueOf(pInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionCode;
    }

}
