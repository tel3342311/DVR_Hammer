package com.liteon.iView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.liteon.iView.util.Def;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.picker.OptionPicker;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimeZoneFragment extends Fragment {

    private Map<String , String> mTimeZoneList;
    private String mTimeZone;
    private String mTimeZoneTitle;
    private String mNTPServer;
    private String mNTPSyncValue;
    private EditText mEdTextNtpServer;
    private ViewGroup mPicker;
    private String currentTimeZone;
    private String currentNTPServer;
    private ImageView mConfirm;
    private static TimeZoneFragment timezoneFragment;

    public TimeZoneFragment() {}

    public static TimeZoneFragment newInstance() {
        if (timezoneFragment == null) {
            timezoneFragment = new TimeZoneFragment();
            Bundle args = new Bundle();
            timezoneFragment.setArguments(args);
        }
        return timezoneFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_zone, container, false);
        findViews(view);
        mEdTextNtpServer.addTextChangedListener(mTextWatcher);
        return view;
    }

    private void findViews(View rootView) {
        mEdTextNtpServer = (EditText) rootView.findViewById(R.id.edit_ntp_server);
        mPicker = (ViewGroup) rootView.findViewById(R.id.picker_container);
        mConfirm = (ImageView) getActivity().findViewById(R.id.confirm_icon);
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
        mTimeZoneTitle = "";
        for(Map.Entry entry: mTimeZoneList.entrySet()){
            if(mTimeZone.equals(entry.getValue())){
                mTimeZoneTitle = (String)entry.getKey();
                break;
            }
        }
        mEdTextNtpServer.setText(mNTPServer);
        setupPicker();
    }

    private void setupPicker() {
        List<String> list = new ArrayList(mTimeZoneList.keySet());
        final OptionPicker mOptionPicker = new OptionPicker(getActivity(), list.toArray(new String[0]));
        mOptionPicker.setTextColor(ContextCompat.getColor(getContext(),R.color.md_black_1000), ContextCompat.getColor(getContext(), R.color.md_white_1000));
        mOptionPicker.setLineVisible(false);
        mOptionPicker.setOnWheelListener(mWheelListener);
        mOptionPicker.setSelectedItem(mTimeZoneTitle);
        mPicker.addView(mOptionPicker.getContentView());
    }

    private OptionPicker.OnWheelListener mWheelListener = new OptionPicker.OnWheelListener() {

        @Override
        public void onWheeled(int index, String timezone) {
            Log.i("TimeZone", timezone);
            currentTimeZone = mTimeZoneList.get(timezone);
            isSettingChanged();
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            currentNTPServer = s.toString();
            isSettingChanged();
        }
    };

    private void isSettingChanged() {
        if (TextUtils.equals(currentNTPServer, mNTPServer) &&
                TextUtils.equals(mTimeZone, currentTimeZone)) {
            mConfirm.setEnabled(false);
        } else {
            mConfirm.setEnabled(true);
        }
    }

    public String getCurrentTimeZone() {
        return currentTimeZone;
    }

    public String getNTPServer() {
        return currentNTPServer;
    }
}
