package com.liteon.iView;


import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.liteon.iView.util.Def;
import com.liteon.iView.util.RecordingItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FILENAME = "filename";
    private static final String ARG_PATH = "path";

    // TODO: Rename and change types of parameters
    private String mParamFileName;
    private String mParamPath;

    private VideoView mVideoView;
    private ViewGroup mViewControlGroup;
    private ImageView mPause;
    private ImageView mRewind;
    private ImageView mForward;
    private ImageView mSnapshot;
    private ImageView mThumbnail;
    private SeekBar   mSeekBar;
    private TextView  mVideoEndTime;
    private int mDuration;
    private Timer mUpdateUITimer;
    private boolean isComplete;
    private static VideoFragment mFragment;
    private List<RecordingItem> mRecordinglist;

    public VideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param filename Parameter ARG_FILENAME.
     * @param path Parameter ARG_PATH.
     * @return A new instance of fragment VideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoFragment newInstance(String filename, String path) {
        if (mFragment == null) {
            mFragment = new VideoFragment();
            Bundle args = new Bundle();
            args.putString(ARG_FILENAME, filename);
            args.putString(ARG_PATH, path);
            mFragment.setArguments(args);
        }
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamFileName = getArguments().getString(ARG_FILENAME);
            mParamPath = getArguments().getString(ARG_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        findViews(view);
        setListener();
        return view;
    }

    private void setListener() {
        mVideoView.setOnTouchListener(mOnVideoViewTouchListener);
        mPause.setOnClickListener(mOnPlayPauseClickListener);
        mRewind.setOnClickListener(mOnRewindClickListener);
        mForward.setOnClickListener(mOnForwardClickListener);
        mSnapshot.setOnClickListener(mOnSnapShotClickListener);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
    }

    private View.OnTouchListener mOnVideoViewTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ((MainActivity)getActivity()).resetMenuTimer();
                ((MainActivity)getActivity()).toggleMenu();
                return true;
            }
            return false;
        }
    };


    private View.OnClickListener mOnPlayPauseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mVideoView.isPlaying()) {
                mVideoView.pause();
            } else {
                mVideoView.start();
            }
        }
    };

    private View.OnClickListener mOnRewindClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            previousVideo();
        }
    };

    private View.OnClickListener mOnForwardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            nextVideo();
        }
    };

    private OnSeekBarChangeListener mSeekBarChangeListener = new OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                stopUITimer();
                int position = mDuration * progress / 100;
                mVideoView.seekTo(position);
                updateUI();
                startUITimer();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private View.OnClickListener mOnSnapShotClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private void findViews(View view) {
        mVideoView = (VideoView) view.findViewById(R.id.video_view);
        mViewControlGroup = (ViewGroup) view.findViewById(R.id.video_control);
        mPause = (ImageView) view.findViewById(R.id.play_pause);
        mRewind = (ImageView) view.findViewById(R.id.rewind);
        mForward = (ImageView) view.findViewById(R.id.forward);
        mSnapshot = (ImageView) view.findViewById(R.id.snapshot);
        mThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        mSeekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        mVideoEndTime = (TextView) view.findViewById(R.id.end_time);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupVideoView();
    }

    private void startUITimer() {
        mUpdateUITimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });
            }
        };
        mUpdateUITimer.schedule(task, 0, 50);
    }

    private void setDuration(){
        mDuration = mVideoView.getDuration();
        mVideoEndTime.setText(getProgressString(mDuration));
    }

    private String getProgressString(int duration) {
        duration /= 1000;
        int minutes = (duration / 60);
        int seconds = duration - (minutes * 60) ;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void updateUI() {
        int current = mVideoView.getCurrentPosition();
        int progress = current * 100 / mDuration;
        mSeekBar.setProgress(progress);
        //mVideoStartTime.setText(getProgressString(current));
    }

    private void setupVideoView() {
        RecordingItem item = ((MainActivity)getActivity()).getCurrentVideoItem();
        Uri uri = Uri.parse(item.getUrl());
        //Uri uri = Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+R.raw.rtrs);
        mVideoView.setVideoURI(uri);
        mVideoView.start();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                setDuration();
                startUITimer();
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                isComplete = true;
                mSeekBar.setProgress(100);
                stopUITimer();
            }

        });
    }

    private void stopUITimer() {
        mUpdateUITimer.cancel();
    }

    public void showVideoControl(boolean isShow) {
        if (isShow) {
            mViewControlGroup.setVisibility(View.VISIBLE);
        } else {
            mViewControlGroup.setVisibility(View.INVISIBLE);
        }
    }


    private void nextVideo() {
        if (mRecordinglist == null) {
            getRecordingList();
        }
        RecordingItem item = ((MainActivity)getActivity()).getCurrentVideoItem();
        int idx;
        for (idx = 0; idx < mRecordinglist.size(); idx++) {
            if (mRecordinglist.get(idx).getName().equals(item.getName())) {
                break;
            }
        }
        if (idx == mRecordinglist.size() - 1) {
            idx = 0;
        } else {
            idx++;
        }
        item = mRecordinglist.get(idx);
        ((MainActivity)getActivity()).setCurrentVideoItem(item);
        setupVideoView();
    }

    private void previousVideo() {
        if (mRecordinglist == null) {
            getRecordingList();
        }
        RecordingItem item = ((MainActivity)getActivity()).getCurrentVideoItem();
        int idx;
        for (idx = 0; idx < mRecordinglist.size(); idx++) {
            if (mRecordinglist.get(idx).getName().equals(item.getName())) {
                break;
            }
        }
        if (idx == 0) {
            idx = mRecordinglist.size() -1;
        } else {
            idx--;
        }
        item = mRecordinglist.get(idx);
        ((MainActivity)getActivity()).setCurrentVideoItem(item);
        setupVideoView();
    }
    private void getRecordingList() {
        SharedPreferences sp = getContext().getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
        String json = sp.getString(Def.SP_RECORDING_LIST, "");
        Type typeOfList = new TypeToken<List<RecordingItem>>() { }.getType();
        Gson gson = new GsonBuilder().create();
        mRecordinglist = gson.fromJson(json, typeOfList);
    }
}
