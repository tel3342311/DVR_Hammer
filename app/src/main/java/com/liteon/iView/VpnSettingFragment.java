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
public class VpnSettingFragment extends Fragment {


    private String mPPTPServer;
    private String mPPTPUsername;
    private String mPPTPPassword;

    public VpnSettingFragment() {
        // Required empty public constructor
    }

    public static VpnSettingFragment newInstance() {
        VpnSettingFragment fragment = new VpnSettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vpn_setting, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sp = getContext().getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
        mPPTPServer = sp.getString(Def.SP_PPTPSERVER, "serverIP");
        mPPTPUsername = sp.getString(Def.SP_PPTPUSER, "User name");
        mPPTPPassword = sp.getString(Def.SP_PPTPPASS, "Password");
        Toast.makeText(getContext(), "serverIP " + mPPTPServer + ", User name " + mPPTPUsername + ", PPTP Password " + mPPTPPassword, Toast.LENGTH_LONG).show();

    }
}
