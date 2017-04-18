package com.liteon.iView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liteon.iView.util.Def;


/**
 * A simple {@link Fragment} subclass.
 */
public class WifiSettingFragment extends Fragment {


    private String mSsid;
    private String mBssid;
    private String mSecurityMode;
    private String mEncryptType;

    public WifiSettingFragment() {
        // Required empty public constructor
    }

    public static WifiSettingFragment newInstance() {
        WifiSettingFragment fragment = new WifiSettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wifi_setting, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = getContext().getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
        mSsid = sp.getString(Def.SP_SSID, "SSID");
        mBssid = sp.getString(Def.SP_BSSID, "BSSID");
        mSecurityMode = sp.getString(Def.SP_SECURITY, "OPEN");
        mEncryptType = sp.getString(Def.SP_ENCRYPTTYPE, "NONE");
        Toast.makeText(getContext(), "SSID " + mSsid + ", BSSID " + mBssid + ", Security mode " + mSecurityMode + ", Encrypt Type " + mEncryptType, Toast.LENGTH_LONG).show();
    }
}
