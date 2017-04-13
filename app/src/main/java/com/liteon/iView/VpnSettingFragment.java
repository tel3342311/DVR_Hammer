package com.liteon.iView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class VpnSettingFragment extends Fragment {


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

}
