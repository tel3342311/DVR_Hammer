package com.example.trdcmacpro.dvr_hammer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingMainFragment extends Fragment {

    private static final int PAGE_COUNT = 6;
    private ViewPager mViewPager;
    private SettingFragmentAdapter mAdapter;
    private SettingMainFragment() {
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

        mViewPager = (ViewPager) view.findViewById(R.id.content);
        mAdapter = new SettingFragmentAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        return view;
    }


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

            if (position == 0) {
                return SettingFragment.newInstance("","");
            } else if (position == 1) {
                return TimeZoneFragment.newInstance();
            } else if (position == 2) {
                return RecordingSettingFragment.newInstance();
            } else if (position == 3) {
                return InternetFragment.newInstance();
            } else if (position == 4) {
                return VpnSettingFragment.newInstance();
            } else if (position == 5) {
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
