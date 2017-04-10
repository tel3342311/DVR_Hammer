package com.example.trdcmacpro.dvr_hammer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Build;
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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.trdcmacpro.dvr_hammer.util.DVRClient;
import com.example.trdcmacpro.dvr_hammer.util.Def;
import com.example.trdcmacpro.dvr_hammer.util.RecordingItem;

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
    private int PAGE_COUNT = 3;
    private View loadingIndicator;
    private Handler mHandlerTime = new Handler();
    private boolean isTimerEnable = true;
    private String mCameraMode = "chb";

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String mode = intent.getStringExtra(Def.EXTRA_GET_SYS_MODE);

            Fragment frag = mAdapter.getItem(mViewPager.getCurrentItem());
            if (frag instanceof PreviewFragment) {
                ((PreviewFragment) frag).onSysModeChange(mode);
            } else if (frag instanceof ItemFragment) {
                ((ItemFragment) frag).onSysModeChange(mode);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setListener();
        mTitleView.setText("Camera 1");
        mDvrClient = new DVRClient("admin", "admin");
        mHandlerTime.postDelayed(HideUIControl, 1500);
    }

    public Runnable HideUIControl = new Runnable()
    {
        public void run() {

            if (showMenu) {
                mBottomMenu.setVisibility(View.GONE);
                mToolBarPreview.setVisibility(View.GONE);
                showMenu = false;
            }

        }
    };

    private void findViews() {
        mToolBarPreview = (Toolbar) findViewById(R.id.toolbar_preview);
        mToolBarRecordings = (Toolbar) findViewById(R.id.toolbar_recordings);
        mToolBarSetting = (Toolbar) findViewById(R.id.toolbar_setting);

        mTitleView = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolBarPreview);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mViewPager = (ViewPager) findViewById(R.id.content);
        setupViewPager(mViewPager);
        mBottomMenu = findViewById(R.id.bottombar);
        mPreview = (ImageView)findViewById(R.id.preview_icon);
        mRecordings = (ImageView)findViewById(R.id.recordings_icon);
        mSetting = (ImageView)findViewById(R.id.setting_icon);
        mPreview.setSelected(true);
        loadingIndicator = buildLoadingIndicator(this);
    }

    private void setListener() {
        mPreview.setOnClickListener(mOnBottomIconClickListener);
        mRecordings.setOnClickListener(mOnBottomIconClickListener);
        mSetting.setOnClickListener(mOnBottomIconClickListener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        String cameraMode = mCameraMode;
        if (cameraMode.equals(Def.FRONT_CAM_MODE)) {
            MenuItem item = menu.findItem(R.id.action_camera_1);
            item.setActionView(null);
            item.setVisible(false);
        } else if (cameraMode.equals(Def.REAR_CAM_MODE)) {
            MenuItem item = menu.findItem(R.id.action_camera_2);
            item.setActionView(null);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public void onListFragmentInteraction(RecordingItem item) {

        Log.i(TAG, "recording item clicked : " + item.name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final String mode;
        Log.d(TAG, "onOptionsItemSelected called");
        item.setActionView(loadingIndicator);
        switch (item.getItemId()) {
            case R.id.action_camera_1:
                mode = Def.FRONT_CAM_MODE;
                mTitleView.setText(mViewPager.getAdapter().getPageTitle(0) + " 1");
                break;
            case R.id.action_camera_2:
                mode = Def.REAR_CAM_MODE;
                mTitleView.setText(mViewPager.getAdapter().getPageTitle(0) + " 2");
                break;
            default:
                mode = Def.FRONT_CAM_MODE;
                break;
        }

        //TODO update this thread by calling service
        new Thread() {
            @Override
            public void run() {
                super.run();
                mDvrClient.setCameraMode(mode);
                mCameraMode = mDvrClient.getCameraMode();
                invalidateOptionsMenu();
            }
        }.start();


        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        mAdapter = new DVRFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(mOnPageChangeListener);
    }

    public void toggleMenu() {
        if (showMenu) {
            mBottomMenu.setVisibility(View.GONE);
            mToolBarPreview.setVisibility(View.GONE);
        } else {
            mBottomMenu.setVisibility(View.VISIBLE);
            mToolBarPreview.setVisibility(View.VISIBLE);
        }
        showMenu = !showMenu;
    }

    public void showSnackBar(String message, String action,  View.OnClickListener onSnackBarClickListener) {

        Snackbar snack = Snackbar.make(findViewById(R.id.container), message, Snackbar.LENGTH_LONG);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snack.getView().getLayoutParams();
        int bottomMargin = (showMenu) ? 56 : 0;
        params.setMargins(0, 0, 0, bottomMargin);
        snack.getView().setLayoutParams(params);
        snack.setAction(action, onSnackBarClickListener);
        snack.show();
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

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public static int dpToPx(Context ctx, int val) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                val, ctx.getResources().getDisplayMetrics());
    }

    public static View buildLoadingIndicator(Context ctx) {
        boolean large = (ctx.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) > Configuration.SCREENLAYOUT_SIZE_NORMAL;
        boolean fresh = Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1;
        int px = (large && fresh) ? 64 : 56;
        FrameLayout fl = new FrameLayout(ctx);
        fl.setMinimumWidth(dpToPx(ctx, px));
        ProgressBar pb = new ProgressBar(ctx);
        px = dpToPx(ctx, 32);
        fl.addView(pb, new FrameLayout.LayoutParams(px, px, Gravity.CENTER));
        return fl;
    }

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
            }
            return super.getPageTitle(position);
        }
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
                    mViewPager.setCurrentItem(0);
                    mToolBarPreview.setVisibility(View.VISIBLE);
                    if (mode.equals(Def.FRONT_CAM_MODE)) {
                        mTitleView.setText(mViewPager.getAdapter().getPageTitle(0) + " 1");
                    } else {
                        mTitleView.setText(mViewPager.getAdapter().getPageTitle(0) + " 2");
                    }
                    mPreview.setSelected(true);
                    return ;
                case R.id.recordings_icon:
                    mViewPager.setCurrentItem(1);
                    mToolBarRecordings.setVisibility(View.VISIBLE);
                    mTitleView.setText(mViewPager.getAdapter().getPageTitle(1));
                    mRecordings.setSelected(true);
                    return ;
                case R.id.setting_icon:
                    mViewPager.setCurrentItem(2);
                    mToolBarSetting.setVisibility(View.VISIBLE);
                    mTitleView.setText(mViewPager.getAdapter().getPageTitle(2));
                    mSetting.setSelected(true);
                    return;
            }
        }
    };
}
