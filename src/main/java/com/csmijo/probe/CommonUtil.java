package com.csmijo.probe;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.security.MessageDigest;

/**
 * Created by chengqianqian-xy on 2016/9/30.
 */

public class CommonUtil {

    private static final String TAG = "CommonUtil";

    /**
     * check permission
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermissions(Context context, String permission) {
        boolean result = ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        return result;
    }


    public static String md5Appkey(String str) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(str.getBytes());
            byte[] arrayOfByte = localMessageDigest.digest();
            StringBuffer localStringBuffer = new StringBuffer();
            for (int i = 0; i < arrayOfByte.length; i++) {
                int j = 0xFF & arrayOfByte[i];
                if (j < 16)
                    localStringBuffer.append("0");
                localStringBuffer.append(Integer.toHexString(j));
            }
            return localStringBuffer.toString();
        } catch (Exception e) {
        }
        return "";
    }
}
