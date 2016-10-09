package com.csmijo.probe.baseData;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.csmijo.probe.CommonUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by chengqianqian-xy on 2016/9/30.
 */

public class DeviceData {

    private static final String tag = "DeviceInfo";
    private static Context context;
    private static Location location;
    private static TelephonyManager telephonyManager;
    private static LocationManager locationManager;
    private static BluetoothAdapter bluetoothAdapter;
    private static SensorManager sensorManager;

    public static void init(Context context) {
        DeviceData.context = context;

        try {
            telephonyManager = (TelephonyManager) (DeviceData.context
                    .getSystemService(Context.TELEPHONY_SERVICE));
            locationManager = (LocationManager) DeviceData.context
                    .getSystemService(Context.LOCATION_SERVICE);
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        } catch (Exception e) {
            Log.e(tag, e.toString());
        }
        getLocation();
    }

    public static String getLanguage() {
        String language = Locale.getDefault().getLanguage();
        Log.i(tag, "getLanguage()=" + language);
        if (language == null)
            return "";
        return language;
    }

    public static String getResolution() {

        DisplayMetrics displaysMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displaysMetrics);
        Log.i(tag, "getResolution()=" + displaysMetrics.widthPixels + "x"
                + displaysMetrics.heightPixels);
        return displaysMetrics.widthPixels + "x" + displaysMetrics.heightPixels;
    }

    public static String getDeviceProduct() {
        String result = Build.PRODUCT;
        Log.i(tag, "getDeviceProduct()=" + result);
        if (result == null)
            return "";
        return result;
    }

    public static boolean getBluetoothAvailable() {
        if (bluetoothAdapter == null)
            return false;
        else
            return true;
    }

    private static boolean isSimulator() {
        if (getDeviceIMEI().equals("000000000000000"))
            return true;
        else
            return false;
    }

    public static boolean getGravityAvailable() {
        try {
            // This code getSystemService(Context.SENSOR_SERVICE);
            // often hangs out the application when it runs in Android
            // Simulator.
            // so in simulator, this line will not be run.
            if (isSimulator())
                sensorManager = null;
            else
                sensorManager = (SensorManager) context
                        .getSystemService(Context.SENSOR_SERVICE);
            Log.i(tag, "getGravityAvailable()");
            return (sensorManager == null) ? false : true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getOsVersion() {
        String result = Build.VERSION.RELEASE;
        Log.i(tag, "getOsVersion()=" + result);
        if (result == null)
            return "";

        return result;
    }

    /**
     * Returns a constant indicating the device phone type. This indicates the
     * type of radio used to transmit voice calls.
     *
     * @return PHONE_TYPE_NONE //0 PHONE_TYPE_GSM //1 PHONE_TYPE_CDMA //2
     * PHONE_TYPE_SIP //3
     */
    public static int getPhoneType() {
        if (telephonyManager == null)
            return -1;
        int result = telephonyManager.getPhoneType();
        Log.i(tag, "getPhoneType()=" + result);
        return result;
    }

    /**
     * get IMSI for GSM phone, return "" if it is unavailable.
     *
     * @return IMSI string
     */
    public static String getIMSI() {
        String result = "";
        try {
            if (!CommonUtil.checkPermissions(context,
                    Manifest.permission.READ_PHONE_STATE)) {
                Log.e(tag,
                        "READ_PHONE_STATE permission should be added into AndroidManifest.xml.");
                return "";
            }
            result = telephonyManager.getSubscriberId();
            Log.i(tag, "getIMSI()=" + result);
            if (result == null)
                return "";
            return result;

        } catch (Exception e) {
        }

        return result;
    }

    public static String getWifiMac() {
        try {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wi = wifiManager.getConnectionInfo();
            String result = wi.getMacAddress();
            if (result == null)
                result = "";
            Log.i(tag, "getWifiMac()=" + result);
            return result;
        } catch (Exception e) {
            return "";
        }

    }

    public static String getDeviceTime() {
        try {
            Date date = new Date();
            SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.US);
            String result = localSimpleDateFormat.format(date);
            return result;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDeviceName() {
        try {
            String manufacturer = Build.MANUFACTURER;
            if (manufacturer == null)
                manufacturer = "";
            String model = Build.MODEL;
            if (model == null)
                model = "";

            if (model.startsWith(manufacturer)) {
                return capitalize(model).trim();
            } else {
                return (capitalize(manufacturer) + " " + model).trim();
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String getNetworkTypeWIFI2G3G() {

        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            String type = ni.getTypeName().toLowerCase(Locale.US);
            if (!type.equals("wifi")) {
                type = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                        .getExtraInfo();
            }
            return type;
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean getWiFiAvailable() {
        try {
            if (!CommonUtil.checkPermissions(context,
                    Manifest.permission.ACCESS_WIFI_STATE)) {
                Log.e(tag,
                        "ACCESS_WIFI_STATE permission should be added into AndroidManifest.xml.");
                return false;
            }
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getTypeName().equals("WIFI")
                                && info[i].isConnected()) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getDeviceIMEI() {
        String result = "";
        try {
            if (!CommonUtil.checkPermissions(context,
                    Manifest.permission.READ_PHONE_STATE)) {
                Log.e(tag,
                        "READ_PHONE_STATE permission should be added into AndroidManifest.xml.");
                return "";
            }
            result = telephonyManager.getDeviceId();
            Log.i(tag, "getIMEI()=" + result);
            if (result == null)
                result = "";
        } catch (Exception e) {
        }
        return result;
    }

    private static String getSSN() {
        String result = "";
        try {

            if (!CommonUtil.checkPermissions(context,
                    Manifest.permission.READ_PHONE_STATE)) {
                Log.e(tag,
                        "READ_PHONE_STATE permission should be added into AndroidManifest.xml.");
                return "";
            }
            result = telephonyManager.getSimSerialNumber();
            if (result == null)
                result = "";
        } catch (Exception e) {

        }
        return result;
    }


    public static String getDeviceId() {
        String result = null;
        result = getDeviceIMEI();
        if (!TextUtils.isEmpty(result)) {
            result = CommonUtil.md5Appkey(result);
        } else {
            result = "000000";
        }
        return result;
    }

    public static String getLatitude() {
        if (location == null)
            return "";
        return String.valueOf(location.getLatitude());
    }

    public static String getLongitude() {
        if (location == null)
            return "";
        return String.valueOf(location.getLongitude());

    }

    public static String getGPSAvailable() {
        if (location == null)
            return "false";
        else
            return "true";
    }

    private static void getLocation() {
        Log.i(tag, "getLocation");
        try {
            List<String> matchingProviders = locationManager.getAllProviders();
            for (String prociderString : matchingProviders) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission
                        .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission
                                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                location = locationManager.getLastKnownLocation(prociderString);
                if (location != null)
                    break;
            }
        } catch (Exception e) {
            Log.e(tag, e.toString());
        }
    }

    public static String getMCCMNC() {
        String result = "";
        try {
            String operator = telephonyManager.getNetworkOperator();
            if (operator == null)
                result = "";
            else
                result = operator;
        } catch (Exception e) {
            result = "";
            Log.e(tag, e.toString());
        }
        return result;
    }

    /**
     * Capitalize the first letter
     *
     * @param s model,manufacturer
     * @return Capitalize the first letter
     */
    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }

    }

}
