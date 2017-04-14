package com.liteon.iView.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DVRClient {

    private final static String TAG = DVRClient.class.getName();
    private static DVRClient mDVRClient;
    private Context mContext;
    private String username = "admin";
    private String password = "admin";
    private Uri mUri;
    private String mCameraMode = Def.FRONT_CAM_MODE;
    private SharedPreferences mSharedPref;

    private DVRClient(Context c) {
        mContext = c;
        mUri = new Uri.Builder()
                    .scheme("http")
                    .authority("192.168.10.1").build();
         mSharedPref = mContext.getSharedPreferences(
                Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);

    }

    public static DVRClient newInstance(Context context) {
        if (mDVRClient == null) {
            mDVRClient = new DVRClient(context);
        }
        return mDVRClient;
    }

    public void setSystemMode(String mode) {
        try {
            URL url = new URL(String.format(Def.DVR_Url, Def.system_cgi));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (!TextUtils.isEmpty(password)) {
                urlConnection.setRequestProperty("Authorization", getAuthorizationHeader());
            }
            Uri.Builder builder = mUri.buildUpon()
                    .appendQueryParameter("page", "system_configuration")
                    .appendQueryParameter("listbox_usbmode", mode);

            String query = builder.build().getEncodedQuery();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Length", Integer.toString(query.getBytes().length));

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            int response = urlConnection.getResponseCode();
            Log.i(TAG, "Set DVR mode to " + mode + ", Response is " + response);
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //public String getCameraMode() {
    //    return mCameraMode;
    //}

    public void setCameraMode(String mode) {

        try {
            URL url = new URL(String.format(Def.DVR_Url, Def.camera_cgi));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (!TextUtils.isEmpty(password)) {
                urlConnection.setRequestProperty("Authorization", getAuthorizationHeader());
            }
            Uri.Builder builder = mUri.buildUpon()
                    .appendQueryParameter("page", "camera_configuration")
                    //.appendQueryParameter("listbox_capture", "cha")
                    //.appendQueryParameter("listbox_video_length", "2m")
                    .appendQueryParameter("listbox_resolution", mode);

            String query = builder.build().getEncodedQuery();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Length", Integer.toString(query.getBytes().length));

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            int response = urlConnection.getResponseCode();
            Log.i(TAG, "Set Camera mode to " + mode + ", Response is " + response);
            mCameraMode = mode;
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRecordingLength(String length) {

        try {
            URL url = new URL(String.format(Def.DVR_Url, Def.camera_cgi));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (!TextUtils.isEmpty(password)) {
                urlConnection.setRequestProperty("Authorization", getAuthorizationHeader());
            }
            Uri.Builder builder = mUri.buildUpon()
                    .appendQueryParameter("page", "camera_configuration")
                    .appendQueryParameter("listbox_video_length", length);

            String query = builder.build().getEncodedQuery();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Length", Integer.toString(query.getBytes().length));

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            int response = urlConnection.getResponseCode();
            Log.i(TAG, "Set recording length to " + length + ", Response is " + response);
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSystemMode() {
        String mode = "";
        try {
            URL url = new URL(String.format(Def.DVR_Url, Def.system_setting));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (!TextUtils.isEmpty(password)) {
                urlConnection.setRequestProperty("Authorization", getAuthorizationHeader());
            }

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);

            InputStream is = urlConnection.getInputStream();
            Document doc = Jsoup.parse(is, "UTF-8", url.toString());

            Elements element = doc.getElementsByAttributeValue("language", "JavaScript");
            String data = element.first().data();
            Pattern pattern= Pattern.compile("var opmod  = \"(.*)\";");
            Matcher matcher = pattern.matcher(data);

            if (matcher.find()) {
                mode = matcher.group(1);
            }
            Log.i(TAG, "Get System mode , mode is " + mode);
            is.close();
            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mode;
    }

    public String getCameraMode() {
        String mode = "";
        try {
            URL url = new URL(String.format(Def.DVR_Url, Def.camera_setting));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (!TextUtils.isEmpty(password)) {
                urlConnection.setRequestProperty("Authorization", getAuthorizationHeader());
            }

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);

            InputStream is = urlConnection.getInputStream();
            Document doc = Jsoup.parse(is, "UTF-8", url.toString());
            Elements element = doc.getElementsByAttributeValue("language", "JavaScript");
            String data = element.first().data();
            Pattern pattern= Pattern.compile("var resol  = \"(.*)\";");
            Matcher matcher = pattern.matcher(data);

            if (matcher.find()) {
                mode = matcher.group(1);
            }
            Log.i(TAG, "Get Camera mode , mode is " + mode);
            int response = urlConnection.getResponseCode();
            Log.i(TAG, "Get Camera mode , Response is " + response);
            is.close();
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mode;
    }

    public String getRecordingLength() {
        String length = "";
        try {
            URL url = new URL(String.format(Def.DVR_Url, Def.camera_setting));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (!TextUtils.isEmpty(password)) {
                urlConnection.setRequestProperty("Authorization", getAuthorizationHeader());
            }

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);

            InputStream is = urlConnection.getInputStream();
            Document doc = Jsoup.parse(is, "UTF-8", url.toString());
            Elements element = doc.getElementsByAttributeValue("language", "JavaScript");
            String data = element.first().data();
            Pattern pattern= Pattern.compile("var clipl  = \"(.*)\";");
            Matcher matcher = pattern.matcher(data);

            if (matcher.find()) {
                length = matcher.group(1);
            }
            Log.i(TAG, "Get recording length, length is " + length);
            int response = urlConnection.getResponseCode();
            Log.i(TAG, "Get recording length , Response is " + response);
            is.close();
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return length;
    }

    public Map<String, String> get3GModemList() {
        Map<String, String> map = new HashMap<>();
        try {
            URL url = new URL(String.format(Def.DVR_Url, Def.net_setting));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (!TextUtils.isEmpty(password)) {
                urlConnection.setRequestProperty("Authorization", getAuthorizationHeader());
            }

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);

            InputStream is = urlConnection.getInputStream();
            Document doc = Jsoup.parse(is, "UTF-8", url.toString());
            Elements elements = doc.select("select[name=Dev3G] > option");
            for (Element e : elements) {
                map.put(e.text(), e.val());
            }
            Log.i(TAG, "Get 3GModem List, map is " + map.toString());
            int response = urlConnection.getResponseCode();
            Log.i(TAG, "Get 3GModem List , Response is " + response);
            is.close();
            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    //public get infomation from ADM page "adm/management.shtml"
    //NTP server
    //Timezone list

    public void getInfoFromADMPage() {
        Map<String, String> map = new HashMap<>();
        try {
            URL url = new URL(String.format(Def.DVR_Url, Def.adm_setting));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (!TextUtils.isEmpty(password)) {
                urlConnection.setRequestProperty("Authorization", getAuthorizationHeader());
            }

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);

            InputStream is = urlConnection.getInputStream();
            Document doc = Jsoup.parse(is, "UTF-8", url.toString());
            Elements elements = doc.select("select[name=time_zone] > option");
            for (Element e : elements) {
                map.put(e.text(), e.val());
            }
            Log.i(TAG, "getInfoFromADMPage Timezone List, map is " + map.toString());
            int response = urlConnection.getResponseCode();
            Log.i(TAG, "getInfoFromADMPage, Response is " + response);
            is.close();
            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getWifiBasic() {
        String ssid = "";
        String passphase = "";
        String bssid = "";
        try {
            URL url = new URL(String.format(Def.DVR_Url, Def.wifi_setting));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (!TextUtils.isEmpty(password)) {
                urlConnection.setRequestProperty("Authorization", getAuthorizationHeader());
            }

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);

            InputStream is = urlConnection.getInputStream();
            Document doc = Jsoup.parse(is, "UTF-8", url.toString());
            Elements element = doc.getElementsByAttributeValue("language", "JavaScript");
            String data = element.first().data();
            Pattern pattern = Pattern.compile("document.wireless_basic.mssid_0.value = \"(.*)\";");
            Matcher matcher = pattern.matcher(data);

            if (matcher.find()) {
                ssid = matcher.group(1);
            }
            Log.i(TAG, "Get SSID is " + ssid);

            element = doc.select("td[id=basicBSSID] + td");
            //remove &nbsp;
            bssid = element.text().replace("\u00a0","");
            Log.i(TAG, "Get BSSID is " + bssid);

            int response = urlConnection.getResponseCode();
            Log.i(TAG, "Get getWifiBasic , Response is " + response);
            is.close();
            SharedPreferences.Editor editor = mSharedPref.edit();
            editor.putString(Def.SP_SSID, ssid);
            editor.putString(Def.SP_BSSID, bssid);
            editor.commit();
            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getSecurity() {
        String ssid = "";
        try {
            URL url = new URL(String.format(Def.DVR_Url, Def.security_setting));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (!TextUtils.isEmpty(password)) {
                urlConnection.setRequestProperty("Authorization", getAuthorizationHeader());
            }

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);

            InputStream is = urlConnection.getInputStream();
            Document doc = Jsoup.parse(is, "UTF-8", url.toString());
            Elements elements = doc.select("select[name=ssidIndex] > option");
            ssid = elements.first().val();
            Log.i(TAG, "Get SSID is " + ssid);
            int response = urlConnection.getResponseCode();
            Log.i(TAG, "Get SSID List , Response is " + response);

            //get Passphase
//            pattern = Pattern.compile("WPAPSK[0] = \"(.*)\"");
//            matcher = pattern.matcher(data);
//            if (matcher.find()) {
//                passphase = matcher.group(1);
//            }
//            Log.i(TAG, "Get passphase is " + passphase);
//
//            pattern = Pattern.compile("if \\(\\(document.getElementById\\(\"newap_text_\" + i\\).value == \"(.*)\"");
//            matcher = pattern.matcher(data);
//            if (matcher.find()) {
//                bssid = matcher.group(1);
//            }
//            Log.i(TAG, "Get bssid is " + passphase);

            is.close();
            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getTimeZoneList() {
        Map<String, String> map = new HashMap<>();
        try {
            URL url = new URL(String.format(Def.DVR_Url, Def.adm_setting));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (!TextUtils.isEmpty(password)) {
                urlConnection.setRequestProperty("Authorization", getAuthorizationHeader());
            }

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);

            InputStream is = urlConnection.getInputStream();
            Document doc = Jsoup.parse(is, "UTF-8", url.toString());
            Elements elements = doc.select("select[name=time_zone] > option");
            for (Element e : elements) {
                map.put(e.text(), e.val());
            }
            Log.i(TAG, "Get Timezone List, map is " + map.toString());
            int response = urlConnection.getResponseCode();
            Log.i(TAG, "Get Timezone List , Response is " + response);
            is.close();
            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public List<RecordingItem> getRecordingList() {

        List<RecordingItem> list = new ArrayList<>();
        try {
            URL url = new URL(Def.DVR_RECORDINGS_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (!TextUtils.isEmpty(password)) {
                urlConnection.setRequestProperty("Authorization", getAuthorizationHeader());
            }

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);

            InputStream is = urlConnection.getInputStream();
            Document doc = Jsoup.parse(is, "UTF-8", Def.DVR_RECORDINGS_URL);
            Elements elements = doc.select("a[href*=.mp4]");
            for (Element element : elements) {
                String uri = element.attr("abs:href");
                String name = element.text();
                String time = element.parent().siblingElements().get(0).text();
                String size = element.parent().siblingElements().get(1).text();

                Log.i(TAG, "Get recording clips , <a> uri is " + uri);
                Log.i(TAG, "Get recording clips , <a> name is " + name);
                Log.i(TAG, "Get recording clips , <a> time is " + time);
                Log.i(TAG, "Get recording clips , <a> size is " + size);
                RecordingItem item = new RecordingItem(uri, name, time, size);
                list.add(item);
            }
            is.close();
            int response = urlConnection.getResponseCode();
            Log.i(TAG, "Get recording clips , Response is " + response);
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void testInputData(InputStream is) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(is, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                Log.d(TAG, line);
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getAuthorizationHeader() {
        try {
            return "Basic " + new String(Base64.encode((username + ":" + password).getBytes("UTF-8"), Base64.NO_WRAP));
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
}
