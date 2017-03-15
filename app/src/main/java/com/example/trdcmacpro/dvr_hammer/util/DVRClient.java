package com.example.trdcmacpro.dvr_hammer.util;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;


public class DVRClient {

    private String username;
    private String password;
    private Uri mUri;
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
    public void setCamera(String mode) {

        Uri.Builder builder = mUri.buildUpon()
                .appendQueryParameter("page", "system_configuration")
                .appendQueryParameter("secondParam", paramValue2)
                .appendQueryParameter("thirdParam", paramValue3);
        String query = builder.build().getEncodedQuery();
        try {
            URL url = new URL(String.format(Def.DVR_Url, Def.camera_cgi));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (!TextUtils.isEmpty(password)) {
                urlConnection.setRequestProperty("Authorization", getAuthorizationHeader());
            }
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoOutput(true);
            urlConnection.getOutputStream()
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Uri.Builder getUriBuilder() {
        return new Uri.Builder()
                .scheme("http")
                .authority("192.168.10.1");

    }
    private String getAuthorizationHeader() {
        try {
            return "Basic " + new String(Base64.encode((username + ":" + password).getBytes("UTF-8"), Base64.NO_WRAP));
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
}
