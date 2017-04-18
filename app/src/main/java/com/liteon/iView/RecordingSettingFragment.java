package com.liteon.iView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liteon.iView.util.Def;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordingSettingFragment extends Fragment {


    private String mRecordingLength;
    private String mRecordingChannel;
    private String mPreviewChannel;
    public RecordingSettingFragment() {
        // Required empty public constructor
    }

    public static RecordingSettingFragment newInstance() {
        RecordingSettingFragment fragment = new RecordingSettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recording_setting, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = getContext().getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
        mRecordingLength = sp.getString(Def.SP_RECORDING_LENGTH, "2m");
        mRecordingChannel = sp.getString(Def.SP_RECORDING_CAMERA, "chab");
        mPreviewChannel = sp.getString(Def.SP_PREVIEW_CAMERA, "cha");
        Toast.makeText(getContext(), "mRecordingLength " + mRecordingLength + ", mRecordingChannel " + mRecordingChannel + ", mPreviewChannel " + mPreviewChannel, Toast.LENGTH_LONG).show();

    }
}
