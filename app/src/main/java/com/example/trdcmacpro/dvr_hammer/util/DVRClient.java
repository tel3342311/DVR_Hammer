package com.example.trdcmacpro.dvr_hammer.util;

import android.os.Bundle;

import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;


public static class DVRClient {

    public void setCamera(String mode) {
        URL url = new URL(Def.DVR_Url + Def.camera_cgi);
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "basic " +
                    Base64.encode((Def.username+":"+Def.password).getBytes(), Base64.NO_WRAP));
            urlConnection.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private getA
}
