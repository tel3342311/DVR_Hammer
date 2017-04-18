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
    //private RecyclerView mTimeZoneView;
    private ViewGroup mPicker;

    private String currentTimeZone;
    private String currentServer;

    private ImageView mConfirm;
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
        View view = inflater.inflate(R.layout.fragment_time_zone, container, false);
        findViews(view);
        Context context = view.getContext();
        //mTimeZoneView.setLayoutManager(new LinearLayoutManager(context));

        mEdTextNtpServer.addTextChangedListener(mTextWatcher);
        return view;
    }

    private void findViews(View rootView) {
        mEdTextNtpServer = (EditText) rootView.findViewById(R.id.edit_ntp_server);
        //mTimeZoneView = (RecyclerView) rootView.findViewById(R.id.time_zone_list);
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

        for(Map.Entry entry: mTimeZoneList.entrySet()){
            if(mTimeZone.equals(entry.getValue())){
                mTimeZoneTitle = (String)entry.getKey();
                break;
            }
        }
        //Toast.makeText(getContext(), "mTimeZoneList " + mTimeZoneList + ", mTimeZone " + mTimeZone + ",mNTPServer " + mNTPServer + ", mNTPSyncValue " + mNTPSyncValue + ", mTimeZoneList " + mTimeZoneList.toString(), Toast.LENGTH_LONG).show();

        //mTimeZoneView.setAdapter(new TimeZoneItemRecyclerViewAdapter(list, mOnTimezoneselectListener));
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

    public interface OnTimeZoneFragmentInteractionListener {
        void onTimeZoneFragmentInteraction(String timezone);
    }

    private OnTimeZoneFragmentInteractionListener mOnTimezoneselectListener = new OnTimeZoneFragmentInteractionListener() {
        @Override
        public void onTimeZoneFragmentInteraction(String timezone) {

            currentTimeZone = timezone;
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
            isSettingChanged();
        }
    };

    private void isSettingChanged() {
        if (TextUtils.equals(mEdTextNtpServer.getText(), mNTPServer) &&
                TextUtils.equals(mTimeZone, currentTimeZone)) {
            mConfirm.setEnabled(false);
        } else {
            mConfirm.setEnabled(true);
        }
    }
}
