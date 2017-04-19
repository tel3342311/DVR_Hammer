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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
public class InternetFragment extends Fragment {

    private String mAPN;
    private String mPIN;
    private String mDial_Num;
    private String mUsername;
    private String mPassword;
    private String mModem;
    private String mModemTitle;
    private Map<String, String> mModemList;

    private TextView mTextViewAPN;
    private TextView mTextViewPIN;
    private TextView mTextViewDialNum;
    private TextView mTextViewUserName;
    private TextView mTextViewPassword;
    private ViewGroup mPicker;
    private ImageView mConfirm;
    private View mRootView;

    public String getCurrentAPN() {
        return currentAPN;
    }

    public String getCurrentPIN() {
        return currentPIN;
    }

    public String getCurrentDialNum() {
        return currentDialNum;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getCurrentModem() {
        return currentModem;
    }

    private String currentAPN;
    private String currentPIN;
    private String currentDialNum;
    private String currentUsername;
    private String currentPassword;
    private String currentModem;

    private static InternetFragment mFragment;
    public InternetFragment() {
        // Required empty public constructor
    }

    public static InternetFragment newInstance() {
        if (mFragment == null) {
            mFragment = new InternetFragment();
            Bundle args = new Bundle();
            mFragment.setArguments(args);
        }
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_internet, container, false);
        findViews(view);
        setListeners();
        return view;
    }

    private void setListeners() {
        mTextViewAPN.addTextChangedListener(mTextWatcherAPN);
        mTextViewPIN.addTextChangedListener(mTextWatcherPIN);
        mTextViewDialNum.addTextChangedListener(mTextWatcherDialNum);
        mTextViewUserName.addTextChangedListener(mTextWatcherUserName);
        mTextViewPassword.addTextChangedListener(mTextWatcherPassword);
    }

    private void setupPicker() {
        List<String> list = new ArrayList(mModemList.keySet());
        final OptionPicker mOptionPicker = new OptionPicker(getActivity(), list.toArray(new String[0]));
        mOptionPicker.setTextColor(ContextCompat.getColor(getContext(),R.color.md_black_1000), ContextCompat.getColor(getContext(), R.color.md_white_1000));
        mOptionPicker.setLineVisible(false);
        mOptionPicker.setOnWheelListener(mWheelListener);
        mOptionPicker.setSelectedItem(mModemTitle);
        mPicker.addView(mOptionPicker.getContentView());
    }

    private OptionPicker.OnWheelListener mWheelListener = new OptionPicker.OnWheelListener() {

        @Override
        public void onWheeled(int index, String modem) {
            Log.i("Modem", modem);
            currentModem = mModemList.get(modem);
            isSettingChanged();
        }
    };

    private void isSettingChanged() {
        if (TextUtils.equals(currentAPN, mAPN) &&
                TextUtils.equals(currentPIN, mPIN) &&
                TextUtils.equals(currentDialNum, mDial_Num) &&
                TextUtils.equals(currentUsername, mUsername) &&
                TextUtils.equals(currentPassword, mPassword) &&
                TextUtils.equals(mModem, currentModem)) {
            mConfirm.setEnabled(false);
        } else {
            mConfirm.setEnabled(true);
        }
    }

    private void findViews(View view) {
        mTextViewAPN = (TextView) view.findViewById(R.id.edit_apn_server);
        mTextViewPIN = (TextView) view.findViewById(R.id.edit_pin);
        mTextViewDialNum = (TextView) view.findViewById(R.id.edit_dial_num_server);
        mTextViewUserName = (TextView) view.findViewById(R.id.edit_username);
        mTextViewPassword = (TextView) view.findViewById(R.id.edit_password);
        mPicker = (ViewGroup) view.findViewById(R.id.picker_container);
        mConfirm = (ImageView) getActivity().findViewById(R.id.confirm_icon);
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
        mModem = sp.getString(Def.SP_MODEM_NAME, "AUTO");
        String json = sp.getString(Def.SP_MODEM_LIST_JSON, "");
        Type typeOfHashMap = new TypeToken<Map<String, String>>() { }.getType();
        Gson gson = new GsonBuilder().create();
        mModemList = gson.fromJson(json, typeOfHashMap);

        for(Map.Entry entry: mModemList.entrySet()){
            if(mModem.equals(entry.getValue())){
                mModemTitle = (String)entry.getKey();
                break;
            }
        }
        setupPicker();
        //Toast.makeText(getContext(), "APN " + mAPN + ", PIN " + mPIN + ", Dial_Num " + mDial_Num + ", User Name " + mUsername + ", Password " + mPassword + " modem list " + mModemList.toString(), Toast.LENGTH_LONG).show();
        //Set Default value
        mTextViewAPN.setText(mAPN);
        mTextViewPIN.setText(mPIN);
        mTextViewDialNum.setText(mDial_Num);
        mTextViewUserName.setText(mUsername);
        mTextViewPassword.setText(mPassword);

    }

    private TextWatcher mTextWatcherAPN = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            currentAPN = s.toString();
            isSettingChanged();
        }
    };

    private TextWatcher mTextWatcherPIN = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            currentPIN = s.toString();
            isSettingChanged();
        }
    };

    private TextWatcher mTextWatcherDialNum = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            currentDialNum = s.toString();
            isSettingChanged();
        }
    };

    private TextWatcher mTextWatcherUserName = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            currentUsername = s.toString();
            isSettingChanged();
        }
    };

    private TextWatcher mTextWatcherPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            currentPassword = s.toString();
            isSettingChanged();
        }
    };
}
