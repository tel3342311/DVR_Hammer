package com.liteon.iView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.liteon.iView.service.DvrInfoService;
import com.liteon.iView.util.DVRClient;
import com.liteon.iView.util.Def;
import com.liteon.iView.util.RecordingItem;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    private final static String TAG = MainActivity.class.getName();
    private DVRFragmentAdapter mAdapter;
    private ViewPager mViewPager;
    private Toolbar mToolBarPreview;
    private Toolbar mToolBarRecordings;
    private Toolbar mToolBarSetting;
    private TextView mTitleView;
    private View mBottomMenu;
    private ImageView mPreview;
    private ImageView mRecordings;
    private ImageView mSetting;
    private boolean showMenu = true;
    private DVRClient mDvrClient;
    private int PAGE_COUNT = 4;
    private View loadingIndicator;
    private Handler mHandlerTime = new Handler();
    private boolean isTimerEnable = true;
    private String mCameraMode = "chb";
    private String mSystemMode = Def.RECORDING_MODE;
    private ImageView mCamera1;
    private ImageView mCamera2;
    private ImageView mVideoPageBack;
    private View mSelectAll;

    public RecordingItem getCurrentVideoItem() {
        return mCurrentVideoItem;
    }

    public void setCurrentVideoItem(RecordingItem mCurrentVideoItem) {
        this.mCurrentVideoItem = mCurrentVideoItem;
    }

    private RecordingItem mCurrentVideoItem;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String mode = intent.getStringExtra(Def.EXTRA_GET_SYS_MODE);
            mSystemMode = mode;
            if (mSystemMode.equals(Def.RECORDING_MODE) && mViewPager.getCurrentItem() == 0) {
                Fragment frag = mAdapter.getItem(mViewPager.getCurrentItem());
                ((PreviewFragment)frag).onSystemReady();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setListener();
        setSupportActionBar(mToolBarPreview);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setupViewPager(mViewPager);
        mTitleView.setText("Camera 1");
        mCamera2.setVisibility(View.VISIBLE);
        mPreview.setSelected(true);
        mDvrClient = DVRClient.newInstance(getApplicationContext());
        mHandlerTime.postDelayed(HideUIControl, 1500);

//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectAll()
//                .penaltyLog()
//                .penaltyDeath()
//                .build());

    }

    public Runnable HideUIControl = new Runnable()
    {
        public void run() {

            if (showMenu && (mViewPager.getCurrentItem() == 0 || mViewPager.getCurrentItem() == 3)) {
                mBottomMenu.setVisibility(View.GONE);

                Animation animToolbar = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.toolbar_hide);
                animToolbar.setAnimationListener(mToolbarAnimation);
                if (mViewPager.getCurrentItem() == 3)
                    mToolBarRecordings.startAnimation(animToolbar);
                else
                    mToolBarPreview.startAnimation(animToolbar);


                Animation animBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_hide);
                animBottom.setAnimationListener(mBottomBarAnimation);
                mBottomMenu.startAnimation(animBottom);

                showMenu = false;
            }
        }
    };

    private Animation.AnimationListener mToolbarAnimation = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            mToolBarPreview.setVisibility(View.INVISIBLE);
            if (mViewPager.getCurrentItem() == 3)
                mToolBarRecordings.setVisibility(View.INVISIBLE);
            else
                mToolBarPreview.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Animation.AnimationListener mBottomBarAnimation = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mViewPager.getCurrentItem() == 3 || mViewPager.getCurrentItem() == 0) {
                mBottomMenu.setVisibility(View.INVISIBLE);
            }
            if (mViewPager.getCurrentItem() == 3) {
                VideoFragment frag = (VideoFragment)(mAdapter.getItem(mViewPager.getCurrentItem()));
                frag.showVideoControl(false);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
    private void findViews() {
        mToolBarPreview = (Toolbar) findViewById(R.id.toolbar_preview);
        mToolBarRecordings = (Toolbar) findViewById(R.id.toolbar_recordings);
        mToolBarSetting = (Toolbar) findViewById(R.id.toolbar_setting);

        mTitleView = (TextView) findViewById(R.id.toolbar_title);
        mViewPager = (ViewPager) findViewById(R.id.video_date);

        mBottomMenu = findViewById(R.id.bottombar);
        mPreview = (ImageView)findViewById(R.id.preview_icon);
        mRecordings = (ImageView)findViewById(R.id.recordings_icon);
        mSetting = (ImageView)findViewById(R.id.setting_icon);

        loadingIndicator =  findViewById(R.id.icon_loading);

        mCamera1 = (ImageView) findViewById(R.id.icon_camera_1);
        mCamera2 = (ImageView) findViewById(R.id.icon_camera_2);

        mVideoPageBack = (ImageView) findViewById(R.id.video_back);
        mSelectAll = findViewById(R.id.select_all);
    }

    private void setListener() {
        mPreview.setOnClickListener(mOnBottomIconClickListener);
        mRecordings.setOnClickListener(mOnBottomIconClickListener);
        mSetting.setOnClickListener(mOnBottomIconClickListener);

        mCamera1.setOnClickListener(mOnCameraClickListener);
        mCamera2.setOnClickListener(mOnCameraClickListener);

        mVideoPageBack.setOnClickListener(mOnVideoBackClickListener);
    }

    private View.OnClickListener mOnVideoBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mViewPager.setCurrentItem(1, false);
        }
    };

    private View.OnClickListener mOnCameraClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String mode;
            loadingIndicator.setVisibility(View.VISIBLE);
            switch (view.getId()) {
                case R.id.icon_camera_1:
                    mode = Def.FRONT_CAM_MODE;
                    mCamera1.setVisibility(View.INVISIBLE);
                    mTitleView.setText(mViewPager.getAdapter().getPageTitle(0) + " 1");
                    break;
                case R.id.icon_camera_2:
                    mode = Def.REAR_CAM_MODE;
                    mCamera2.setVisibility(View.INVISIBLE);
                    mTitleView.setText(mViewPager.getAdapter().getPageTitle(0) + " 2");
                    break;
                default:
                    mode = Def.FRONT_CAM_MODE;
                    break;
            }
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    mDvrClient.setCameraMode(mode);
                    mDvrClient.getWifiBasic();
                    mDvrClient.getWifiSecurity();
                    mDvrClient.getNetworkSetting();
                    mDvrClient.getInfoFromADMPage();
                    mDvrClient.getCameraSetting();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateCameraIcon(mode);
                        }
                    });
                }
            }.start();
        }
    };

    private boolean updateCameraIcon(String cameraMode) {

        loadingIndicator.setVisibility(View.INVISIBLE);
        if (cameraMode.equals(Def.FRONT_CAM_MODE)) {
            mCamera2.setVisibility(View.VISIBLE);
        } else if (cameraMode.equals(Def.REAR_CAM_MODE)) {
            mCamera1.setVisibility(View.VISIBLE);
        }
        return true;
    }

    @Override
    public void onListFragmentInteraction(RecordingItem item) {

        Log.i(TAG, "recording item clicked : " + item.getName());
        mCurrentVideoItem = item;
        mViewPager.setCurrentItem(3, false);
    }

    private void setupViewPager(ViewPager viewPager) {
        mAdapter = new DVRFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(mOnPageChangeListener);
    }

    public void toggleMenu() {
        if (showMenu) {
            mBottomMenu.setVisibility(View.GONE);
            if (mViewPager.getCurrentItem() == 0 ) {
                mToolBarPreview.setVisibility(View.GONE);
            }
            else if (mViewPager.getCurrentItem() == 3) {
                mToolBarRecordings.setVisibility(View.GONE);
                VideoFragment frag = (VideoFragment)(mAdapter.getItem(mViewPager.getCurrentItem()));
                frag.showVideoControl(false);
            }
            mHandlerTime.removeCallbacks(HideUIControl);
        } else {
            mBottomMenu.setVisibility(View.VISIBLE);
            if (mViewPager.getCurrentItem() == 0 ) {
                mToolBarPreview.setVisibility(View.VISIBLE);
            } else if (mViewPager.getCurrentItem() == 3) {
                mToolBarRecordings.setVisibility(View.VISIBLE);
                VideoFragment frag = (VideoFragment)(mAdapter.getItem(mViewPager.getCurrentItem()));
                frag.showVideoControl(true);
            }
            mHandlerTime.postDelayed(HideUIControl, 1500);
        }
        showMenu = !showMenu;
    }

    public void resetMenuTimer() {
        mHandlerTime.removeCallbacks(HideUIControl);
        mHandlerTime.postDelayed(HideUIControl, 1500);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        private int currentPosition = 0;
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentPosition = position;
            Log.d(TAG, "Current position of Fragment is " + currentPosition);
            if (position == 0) {
                mToolBarPreview.setVisibility(View.VISIBLE);
                mHandlerTime.postDelayed(HideUIControl,1500);
                setDVRMode(Def.RECORDING_MODE);
            } else if (position == 1) {
                mToolBarRecordings.setVisibility(View.VISIBLE);
                mToolBarRecordings.findViewById(R.id.video_back).setVisibility(View.INVISIBLE);
                mHandlerTime.removeCallbacks(HideUIControl);
                mSelectAll.setVisibility(View.VISIBLE);
                setDVRMode(Def.STORAGE_MODE);
            } else if (position == 2) {
                mToolBarSetting.setVisibility(View.VISIBLE);
                mHandlerTime.removeCallbacks(HideUIControl);

            } else if (position == 3) {
                mHandlerTime.postDelayed(HideUIControl,1500);
                mToolBarRecordings.findViewById(R.id.video_back).setVisibility(View.VISIBLE);
                mSelectAll.setVisibility(View.GONE);
            }
            mBottomMenu.setVisibility(View.VISIBLE);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public class DVRFragmentAdapter extends FragmentPagerAdapter {

        public DVRFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return PreviewFragment.newInstance("","");
            } else if (position == 1) {
                return ItemFragment.newInstance(10);
            } else if (position == 2) {
                return SettingMainFragment.newInstance();
            } else if (position == 3) {
                return VideoFragment.newInstance("", "");
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.title_home);
            } else if (position == 1) {
                return getString(R.string.title_dashboard);
            } else if (position == 2) {
                return getString(R.string.title_notifications);
            } else if (position == 3) {
                String title = mCurrentVideoItem.getName();
                if (title == null) {
                    title = "";
                }
                return title;
            }
            return super.getPageTitle(position);
        }
    }

    private void setDVRMode(String mode) {
        Intent intent = new Intent();
        intent.setAction(Def.ACTION_SET_SYS_MODE);
        intent.putExtra(Def.EXTRA_SET_SYS_MODE, mode);
        intent.setClass(MainActivity.this, DvrInfoService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(Def.ACTION_GET_SYS_MODE);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBroadcastReceiver);
    }

    private View.OnClickListener mOnBottomIconClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mPreview.setSelected(false);
            mRecordings.setSelected(false);
            mSetting.setSelected(false);
            mToolBarPreview.setVisibility(View.INVISIBLE);
            mToolBarRecordings.setVisibility(View.INVISIBLE);
            mToolBarSetting.setVisibility(View.INVISIBLE);
            String mode = mCameraMode;
            switch (v.getId()) {
                case R.id.preview_icon:
                    mViewPager.setCurrentItem(0,false);
                    mToolBarPreview.setVisibility(View.VISIBLE);
                    if (mode.equals(Def.FRONT_CAM_MODE)) {
                        mTitleView.setText(mViewPager.getAdapter().getPageTitle(0) + " 1");
                    } else {
                        mTitleView.setText(mViewPager.getAdapter().getPageTitle(0) + " 2");
                    }
                    mPreview.setSelected(true);
                    return ;
                case R.id.recordings_icon:
                    mViewPager.setCurrentItem(1,false);
                    mToolBarRecordings.setVisibility(View.VISIBLE);
                    mRecordings.setSelected(true);
                    return ;
                case R.id.setting_icon:
                    mViewPager.setCurrentItem(2,false);
                    mToolBarSetting.setVisibility(View.VISIBLE);
                    mSetting.setSelected(true);
                    return;
            }
        }
    };
}
