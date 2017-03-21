package com.example.trdcmacpro.dvr_hammer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.camera.simplemjpeg.MjpegInputStream;
import com.camera.simplemjpeg.MjpegView;
import com.example.trdcmacpro.dvr_hammer.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_preview:
                    setFragment(PreviewFragment.newInstance("",""));
                    return true;
                case R.id.navigation_storage:
                    setFragment(ItemFragment.newInstance(0));
                    return true;
                case R.id.navigation_setting:
                    setFragment(SettingFragment.newInstance("",""));
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String URL = "http://192.168.10.1:8081/?action=stream";

        MjpegView mv = new MjpegView(this);
        setContentView(mv);

        mv.setSource(MjpegInputStream.read(URL));
        mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
        mv.showFps(true);
        //setContentView(R.layout.activity_main);

        //setFragment(PreviewFragment.newInstance("",""));
        //BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
