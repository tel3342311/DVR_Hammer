package com.liteon.iView;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.liteon.iView.util.Def;

import java.lang.reflect.Type;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class InternetFragment extends Fragment {

    private String mAPN;
    private String mPIN;
    private String mDial_Num;
    private String mUsername;
    private String mPassword;
    private Map<String, String> modemList;

    public InternetFragment() {
        // Required empty public constructor
    }

    public static InternetFragment newInstance() {
        InternetFragment fragment = new InternetFragment();
        Bundle
                args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_internet, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = getContext().getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
        mAPN = sp.getString(Def.SP_APN3G, "APN");
        mPIN = sp.getString(Def.SP_PIN3G, "PIN");
        mDial_Num = sp.getString(Def.SP_USER3G, "User name");
        mUsername = sp.getString(Def.SP_PASSWORD3G, "Password");
        mPassword = sp.getString(Def.SP_DIAL3G, "Dial number");
        String json = sp.getString(Def.SP_MODEM_LIST_JSON, "");
        Type typeOfHashMap = new TypeToken<Map<String, String>>() { }.getType();
        Gson gson = new GsonBuilder().create();
        modemList = gson.fromJson(json, typeOfHashMap);

        Toast.makeText(getContext(), "APN " + mAPN + ", PIN " + mPIN + ", Dial_Num " + mDial_Num + ", User Name " + mUsername + ", Password " + mPassword + " modem list " + modemList.toString(), Toast.LENGTH_LONG).show();

    }
}
