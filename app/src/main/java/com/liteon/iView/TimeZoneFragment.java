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
public class TimeZoneFragment extends Fragment {

    private Map<String , String> mTimeZoneList;
    private String mTimeZone;
    private String mNTPServer;
    private String mNTPSyncValue;

    public TimeZoneFragment() {
        // Required empty public constructor
    }

    public static TimeZoneFragment newInstance() {
        TimeZoneFragment fragment = new TimeZoneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_zone, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = getContext().getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
        String json = sp.getString(Def.SP_TIMEZONE_LIST, "");
        Type typeOfHashMap = new TypeToken<Map<String, String>>() { }.getType();
        Gson gson = new GsonBuilder().create();
        mTimeZoneList = gson.fromJson(json, typeOfHashMap);
        mTimeZone = sp.getString(Def.SP_TIMEZONE, "");
        mNTPServer = sp.getString(Def.SP_NTPSERVER, "");
        mNTPSyncValue = sp.getString(Def.SP_NTP_SYNC_VALUE, "");

        Toast.makeText(getContext(), "mTimeZoneList " + mTimeZoneList + ", mTimeZone " + mTimeZone + ",mNTPServer " + mNTPServer + ", mNTPSyncValue " + mNTPSyncValue + ", mTimeZoneList " + mTimeZoneList.toString(), Toast.LENGTH_LONG).show();

    }
}
