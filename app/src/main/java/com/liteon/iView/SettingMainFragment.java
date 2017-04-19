package com.liteon.iView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.liteon.iView.service.DvrInfoService;
import com.liteon.iView.util.Def;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingMainFragment extends Fragment {

    private static final int PAGE_COUNT = 6;
    private static final int SETTING_MAIN = 0;
    private static final int SETTING_TIMEZONE = 1;
    private static final int SETTING_RECORDINGS = 2;
    private static final int SETTING_INTERNET = 3;
    private static final int SETTING_VPN = 4;
    private static final int SETTING_WIFI = 5;

    private ViewPager mViewPager;
    private SettingFragmentAdapter mAdapter;
    private Toolbar mToolBar;
    private ImageView mCancel;
    private ImageView mConfirm;

    public SettingMainFragment() {
        // Required empty public constructor
    }
    private static SettingMainFragment mFragment;
    public static SettingMainFragment newInstance() {
        if (mFragment == null) {
            mFragment = new SettingMainFragment();
            Bundle args = new Bundle();
            mFragment.setArguments(args);
            return mFragment;
        }
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_main, container, false);
        findViews(view);
        setListener();
        mAdapter = new SettingFragmentAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(mOnSettingPageChangeListener);
        mConfirm.setEnabled(false);
        return view;
    }

    private void setListener() {
        mCancel.setOnClickListener(mOnCancelClickListener);
        mConfirm.setOnClickListener(mOnConfirmClickListener);
    }

    private void findViews(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.content);
        mToolBar = (Toolbar) getActivity().findViewById(R.id.toolbar_setting);
        mCancel = (ImageView) mToolBar.findViewById(R.id.cancel_icon);
        mConfirm = (ImageView) mToolBar.findViewById(R.id.confirm_icon);
    }

    private View.OnClickListener mOnCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mViewPager.setCurrentItem(SETTING_MAIN, false);
        }
    };

    private View.OnClickListener mOnConfirmClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            int position = mViewPager.getCurrentItem();
            Fragment fragment = mAdapter.getItem(position);
            if (position == SETTING_TIMEZONE) {
                String timezone = ((TimeZoneFragment)fragment).getCurrentTimeZone();
                String ntpServer = ((TimeZoneFragment)fragment).getNTPServer();
                intent.setAction(Def.ACTION_SET_TIMEZONE);
                intent.putExtra(Def.EXTRA_TIMEZONE, timezone);
                intent.putExtra(Def.EXTRA_NTP_SERVER, ntpServer);
            } else if (position == SETTING_RECORDINGS) {
                String recordingLength = ((RecordingSettingFragment)fragment).getRecordingLength();
                String recordingChannel = ((RecordingSettingFragment)fragment).getRecordingChannel();
                intent.setAction(Def.ACTION_SET_RECORDINGS);
                intent.putExtra(Def.EXTRA_RECORDING_LENGTH, recordingLength);
                intent.putExtra(Def.EXTRA_RECORDING_CHANNEL, recordingChannel);
            } else if (position == SETTING_INTERNET) {
                String apn = ((InternetFragment)fragment).getCurrentAPN();
                String pin = ((InternetFragment)fragment).getCurrentPIN();
                String dial_Num = ((InternetFragment)fragment).getCurrentDialNum();
                String username = ((InternetFragment)fragment).getCurrentUsername();
                String password = ((InternetFragment)fragment).getCurrentPassword();
                String modem = ((InternetFragment)fragment).getCurrentModem();
                intent.setAction(Def.ACTION_SET_INTERNET);
                intent.putExtra(Def.EXTRA_APN, apn);
                intent.putExtra(Def.EXTRA_PIN, pin);
                intent.putExtra(Def.EXTRA_DIAL_NUM, dial_Num);
                intent.putExtra(Def.EXTRA_USERNAME_3G, username);
                intent.putExtra(Def.EXTRA_PASSWORD_3G, password);
                intent.putExtra(Def.EXTRA_MODEM, modem);
            } else if (position == SETTING_VPN) {

            } else if (position == SETTING_WIFI) {

            }
            intent.setClass(getActivity(), DvrInfoService.class);
            getContext().startService(intent);
        }
    };

    private ViewPager.OnPageChangeListener mOnSettingPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == SETTING_MAIN) {
                mCancel.setVisibility(View.INVISIBLE);
                mConfirm.setVisibility(View.INVISIBLE);
            } else {
                mCancel.setVisibility(View.VISIBLE);
                mConfirm.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public class SettingFragmentAdapter extends FragmentPagerAdapter {

        public SettingFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {

            if (position == SETTING_MAIN) {
                return SettingFragment.newInstance("","");
            } else if (position == SETTING_TIMEZONE) {
                return TimeZoneFragment.newInstance();
            } else if (position == SETTING_RECORDINGS) {
                return RecordingSettingFragment.newInstance();
            } else if (position == SETTING_INTERNET) {
                return InternetFragment.newInstance();
            } else if (position == SETTING_VPN) {
                return VpnSettingFragment.newInstance();
            } else if (position == SETTING_WIFI) {
                return WifiSettingFragment.newInstance();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.title_notifications);
            } else if (position == 1) {
                return getString(R.string.title_setting_date);
            } else if (position == 2) {
                return getString(R.string.title_setting_recording);
            } else if (position == 3) {
                return getString(R.string.title_setting_internet);
            } else if (position == 4) {
                return getString(R.string.title_setting_vpn_routing);
            } else if (position == 5) {
                return getString(R.string.title_setting_wifi_setting);
            }
            return super.getPageTitle(position);
        }
    }
}
