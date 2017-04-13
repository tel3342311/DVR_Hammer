package com.liteon.iView.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.liteon.iView.util.DVRClient;
import com.liteon.iView.util.Def;

import java.util.Map;

public class DvrInfoService extends IntentService {

    private static final String TAG = DvrInfoService.class.getName();
    private static final String ACTION_GET_ALL_INFO = Def.ACTION_GET_ALL_INFO;
    private static final String ACTION_GET_SYS_MODE = Def.ACTION_GET_SYS_MODE;
    private static final String ACTION_SET_SYS_MODE = Def.ACTION_SET_SYS_MODE;
    private static final String ACTION_GET_CAM_MODE = Def.ACTION_GET_CAM_MODE;
    private static final String ACTION_GET_INTERNET = Def.ACTION_GET_INTERNET;
    private static final String ACTION_GET_WIRELESS = Def.ACTION_GET_WIRELESS;
    private static final String ACTION_GET_SECURITY = Def.ACTION_GET_SECURITY;
    private static final String ACTION_GET_ADMIN    = Def.ACTION_GET_ADMIN;

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.trdcmacpro.dvr_hammer.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.trdcmacpro.dvr_hammer.service.extra.PARAM2";

    public DvrInfoService() {
        super("DvrInfoService");
    }

    public static void startActionGetAllinfo(Context context) {
        Intent intent = new Intent(context, DvrInfoService.class);
        intent.setAction(ACTION_GET_ALL_INFO);
        context.startService(intent);
    }

    public static void startActionGetSysInfo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DvrInfoService.class);
        intent.setAction(ACTION_GET_SYS_MODE);
        context.startService(intent);
    }

    public static void startActionGetCamMode(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DvrInfoService.class);
        intent.setAction(ACTION_GET_CAM_MODE);
        context.startService(intent);
    }

    public static void startActionGetInternet(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DvrInfoService.class);
        intent.setAction(ACTION_GET_INTERNET);
        context.startService(intent);
    }

    public static void startActionGetWireless(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DvrInfoService.class);
        intent.setAction(ACTION_GET_WIRELESS);
        context.startService(intent);
    }

    public static void startActionGetSecurity(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DvrInfoService.class);
        intent.setAction(ACTION_GET_SECURITY);
        context.startService(intent);
    }

    public static void startActionGetAdmin(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DvrInfoService.class);
        intent.setAction(ACTION_GET_ADMIN);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_ALL_INFO.equals(action)) {
                handleActionGetAllInfo();
            } else if (ACTION_GET_SYS_MODE.equals(action)) {
                handleActionGetSysInfo();
            } else if (ACTION_SET_SYS_MODE.equals(action)) {
                String mode = intent.getStringExtra(Def.EXTRA_SET_SYS_MODE);
                handleActionSetSysInfo(mode);
            } else if (ACTION_GET_CAM_MODE.equals(action)) {
                handleActionGetCamMode();
            } else if (ACTION_GET_INTERNET.equals(action)) {
                handleActionGetInternet();
            } else if (ACTION_GET_WIRELESS.equals(action)) {
                handleActionGetWireless();
            } else if (ACTION_GET_SECURITY.equals(action)) {
                handleActionGetSecurity();
            } else if (ACTION_GET_ADMIN.equals(action)) {
                handleActionGetAdmin();
            }
        }
    }

    private void handleActionGetAllInfo() {

    }

    private void handleActionGetSysInfo() {
        DVRClient dvrClient = new DVRClient("admin", "admin");
        String mode = dvrClient.getSystemMode();
        Log.v(TAG, "[handleActionGetSysInfo] sys mode is " + mode);
        Intent intent = new Intent(Def.ACTION_GET_SYS_MODE);
        intent.putExtra(Def.EXTRA_GET_SYS_MODE, mode);
        sendBroadcast(intent);
    }

    private void handleActionSetSysInfo(String mode) {
        DVRClient dvrClient = new DVRClient("admin", "admin");
        dvrClient.setSystemMode(mode);
        Log.v(TAG, "[handleActionSetSysInfo] sys mode is " + mode);
        Intent intent = new Intent(Def.ACTION_GET_SYS_MODE);
        intent.putExtra(Def.EXTRA_GET_SYS_MODE, mode);
        sendBroadcast(intent);
    }

    private void handleActionGetCamMode(){
        DVRClient dvrClient = new DVRClient("admin", "admin");
        String mode = dvrClient.getCameraMode();
        Log.v(TAG, "[handleActionGetCamMode] Camera Mode is " + mode);
        Intent intent = new Intent(Def.ACTION_GET_CAM_MODE);
        intent.putExtra(Def.EXTRA_GET_CAM_MODE, mode);
        sendBroadcast(intent);
        //TODO reduce query times
        String length = dvrClient.getRecordingLength();
        Log.v(TAG, "[handleActionGetCamMode] recording length is " + length);

    }

    private void handleActionGetInternet(){
        DVRClient dvrClient = new DVRClient("admin", "admin");
        Map map = dvrClient.get3GModemList();
        Log.v(TAG, "[handleActionGetInternet] get3GModemList is " + map.toString());
    }

    private void handleActionGetWireless(){

    }

    private void handleActionGetSecurity(){

    }

    private void handleActionGetAdmin(){
        DVRClient dvrClient = new DVRClient("admin", "admin");
        Map map = dvrClient.getTimeZoneList();
        Log.v(TAG, "[handleActionGetAdmin] getTimeZoneList is " + map.toString());
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
    }


}
