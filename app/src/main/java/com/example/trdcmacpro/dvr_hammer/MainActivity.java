package com.example.trdcmacpro.dvr_hammer;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.trdcmacpro.dvr_hammer.util.DVRClient;
import com.example.trdcmacpro.dvr_hammer.util.Def;
import com.example.trdcmacpro.dvr_hammer.util.RecordingItem;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    private final static String TAG = MainActivity.class.getName();
    private DVRFragmentAdapter mAdapter;
    private ViewPager mViewPager;
    private Toolbar mToolBar;
    private BottomNavigationView mBottomMenu;
    private boolean showMenu = true;
    private DVRClient mDvrClient;
    private int PAGE_COUNT = 3;
    private View loadingIndicator;
    private Handler mHandlerTime = new Handler();
    private boolean isTimerEnable = true;
    private String mCameraMode = "chb";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ActionBar actionbar = getSupportActionBar();
            String mode = mCameraMode;
            switch (item.getItemId()) {
                case R.id.navigation_preview:
                    mViewPager.setCurrentItem(0);
                    if (mode.equals(Def.FRONT_CAM_MODE)) {
                        actionbar.setTitle(mViewPager.getAdapter().getPageTitle(0) + " 1");
                    } else {
                        actionbar.setTitle(mViewPager.getAdapter().getPageTitle(0) + " 2");
                    }
                    return true;
                case R.id.navigation_storage:
                    mViewPager.setCurrentItem(1);
                    actionbar.setTitle(mViewPager.getAdapter().getPageTitle(1));
                    return true;
                case R.id.navigation_setting:
                    mViewPager.setCurrentItem(2);
                    actionbar.setTitle(mViewPager.getAdapter().getPageTitle(2));
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Camera 1");
        findViews();
        mDvrClient = new DVRClient("admin", "admin");
        mHandlerTime.postDelayed(HideUIControl, 1500);
    }

    public Runnable HideUIControl = new Runnable()
    {
        public void run() {

            if (showMenu) {
                mBottomMenu.setVisibility(View.GONE);
                mToolBar.setVisibility(View.GONE);
                showMenu = false;
            }

        }
    };

    private void findViews() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        mViewPager = (ViewPager) findViewById(R.id.content);
        setupViewPager(mViewPager);
        mBottomMenu = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomMenu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadingIndicator = buildLoadingIndicator(this);
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
                getSupportActionBar().setTitle(mViewPager.getAdapter().getPageTitle(0) + " 1");
                break;
            case R.id.action_camera_2:
                mode = Def.REAR_CAM_MODE;
                getSupportActionBar().setTitle(mViewPager.getAdapter().getPageTitle(0) + " 2");
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
            mToolBar.setVisibility(View.GONE);
        } else {
            mBottomMenu.setVisibility(View.VISIBLE);
            mToolBar.setVisibility(View.VISIBLE);
        }
        showMenu = !showMenu;
    }

    public void showSnackBar(String message, String action,  View.OnClickListener onSnackBarClickListener) {

        Snackbar snack = Snackbar.make(findViewById(R.id.container), message, Snackbar.LENGTH_LONG);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snack.getView().getLayoutParams();
        int bottomMargin = (showMenu) ? mBottomMenu.getHeight() : 0;
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
}
