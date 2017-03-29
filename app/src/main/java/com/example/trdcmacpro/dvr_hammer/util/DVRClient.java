package com.example.trdcmacpro.dvr_hammer.util;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;


public class DVRClient {

    private final static String TAG = DVRClient.class.getName();

    private String username;
    private String password;
    private Uri mUri;
    private String mCameraMode = Def.FRONT_CAM_MODE;

    public DVRClient(String user, String pass) {
        if (!TextUtils.isEmpty(user)) {
            this.username = user;
        }
        if (!TextUtils.isEmpty(pass)) {
            this.password = pass;
        }
        mUri = new Uri.Builder()
                    .scheme("http")
                    .authority("192.168.10.1").build();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCameraMode() {
        return mCameraMode;
    }

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
