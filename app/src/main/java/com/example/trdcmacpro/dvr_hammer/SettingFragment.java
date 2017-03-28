package com.example.trdcmacpro.dvr_hammer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.trdcmacpro.dvr_hammer.util.DVRClient;
import com.example.trdcmacpro.dvr_hammer.util.Def;

import static com.example.trdcmacpro.dvr_hammer.R.string.title_setting_wifi_setting;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private DVRClient mDvrClient;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner mSpinner;
    private Spinner mModemSpinner;

    private Button mButtonPreview;
    private Button mButtonStrorage;
    private Button mButtonFront;
    private Button mButtonRear;
    private Button mButtonFrontRear;
    private Button mButtonLength2M;
    private Button mButtonLength3M;
    private Button mButtonLength5M;

    private ImageView mTimeZone;
    private ImageView mRecording;
    private ImageView mInternet;
    private ImageView mVpn;
    private ImageView mWifi;

    public SettingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mDvrClient = new DVRClient("admin", "admin");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        findViews(view);
        setListener();
        return view;
    }

    void findViews(View rootView) {
        mTimeZone = (ImageView) rootView.findViewById(R.id.time_zone);
        mRecording = (ImageView) rootView.findViewById(R.id.recording);
        mInternet = (ImageView) rootView.findViewById(R.id.internet);
        mVpn = (ImageView) rootView.findViewById(R.id.vpn);
        mWifi = (ImageView) rootView.findViewById(R.id.wifi);
//        mSpinner = (Spinner) rootView.findViewById(R.id.spinner);
//        ArrayAdapter<CharSequence> gmtList = ArrayAdapter.createFromResource(getContext(),
//                R.array.gmt_list,
//                android.R.layout.simple_spinner_dropdown_item);
//        mSpinner.setAdapter(gmtList);
//
//        mModemSpinner = (Spinner) rootView.findViewById(R.id.modem_spinner);
//        ArrayAdapter<CharSequence> dev3gList = ArrayAdapter.createFromResource(getContext(),
//                R.array.dev3g_list,
//                android.R.layout.simple_spinner_dropdown_item);
//        mModemSpinner.setAdapter(dev3gList);
//
//        mButtonPreview = (Button) rootView.findViewById(R.id.recording_mode);
//        mButtonStrorage = (Button) rootView.findViewById(R.id.storage_mode);
//        mButtonFront = (Button) rootView.findViewById(R.id.front_cam);
//        mButtonRear = (Button) rootView.findViewById(R.id.rear_cam);
//        mButtonFrontRear = (Button) rootView.findViewById(R.id.front_rear_cam);
//        mButtonLength2M = (Button) rootView.findViewById(R.id.length_2m);
//        mButtonLength3M = (Button) rootView.findViewById(R.id.length_3m);
//        mButtonLength5M = (Button) rootView.findViewById(R.id.length_5m);
    }
    void setListener() {
        mTimeZone.setOnClickListener(mOnSettingClickListener);
        mRecording.setOnClickListener(mOnSettingClickListener);
        mInternet.setOnClickListener(mOnSettingClickListener);
        mVpn.setOnClickListener(mOnSettingClickListener);
        mWifi.setOnClickListener(mOnSettingClickListener);

//        mButtonPreview.setOnClickListener(mOnRecordingClickListener);
//        mButtonStrorage.setOnClickListener(mOnStrorageClickListener);
//        mButtonFront.setOnClickListener(mOnFrontCamClickListener);
//        mButtonRear.setOnClickListener(mOnRearCamClickListener);
//        mButtonFrontRear.setOnClickListener(mOnFrontRearClickListener);
//        mButtonLength2M.setOnClickListener(mOnLengthClickListener);
//        mButtonLength3M.setOnClickListener(mOnLengthClickListener);
//        mButtonLength5M.setOnClickListener(mOnLengthClickListener);
    }
    private View.OnClickListener mOnRecordingClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    mDvrClient.setSystemMode(Def.RECORDING_MODE);
                }
            }.start();

        }
    };

    private View.OnClickListener mOnStrorageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    mDvrClient.setSystemMode(Def.STORAGE_MODE);
                }
            }.start();
        }
    };

    private View.OnClickListener mOnFrontCamClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    mDvrClient.setCameraMode(Def.FRONT_CAM_MODE);
                }
            }.start();
        }
    };

    private View.OnClickListener mOnRearCamClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    mDvrClient.setCameraMode(Def.REAR_CAM_MODE);                }
            }.start();
        }
    };

    private View.OnClickListener mOnFrontRearClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    mDvrClient.setCameraMode(Def.FRONT_REAR_CAM_MODE);                }
            }.start();
        }
    };

    private View.OnClickListener mOnLengthClickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
//                    switch(v.getId()) {
//                        case R.id.length_2m:
//                            mDvrClient.setRecordingLength(Def.LENGTH_2M);
//                            break;
//                        case R.id.length_3m:
//                            mDvrClient.setRecordingLength(Def.LENGTH_3M);
//                            break;
//                        case R.id.length_5m:
//                            mDvrClient.setRecordingLength(Def.LENGTH_5M);
//                            break;
//                    }
                }
            }.start();
        }
    };

    private View.OnClickListener mOnSettingClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            ViewPager viewPager = SettingMainFragment.newInstance().getViewPager();
            switch (view.getId()) {
                case R.id.time_zone:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.recording:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.internet:
                    viewPager.setCurrentItem(3);
                    break;
                case R.id.vpn:
                    viewPager.setCurrentItem(4);
                    break;
                case R.id.wifi:
                    viewPager.setCurrentItem(5);
                    break;
            }
        }
    };
}