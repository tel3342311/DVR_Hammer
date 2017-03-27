package com.example.trdcmacpro.dvr_hammer;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.camera.simplemjpeg.MjpegInputStream;
import com.camera.simplemjpeg.MjpegView;
import com.example.trdcmacpro.dvr_hammer.util.Def;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreviewFragment extends Fragment {
    private final static String TAG = PreviewFragment.class.getName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MjpegView mv;
    private boolean suspending;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static PreviewFragment mFragment;

    public PreviewFragment() {
        // Required empty public constructor
    }

    public static PreviewFragment newInstance(String param1, String param2) {
        if (mFragment == null) {
            mFragment = new PreviewFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            mFragment.setArguments(args);
        }
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_preview, container, false);
        mv = (MjpegView) view.findViewById(R.id.preview);
        new ReadDVR().execute(Def.DVR_PREVIEW_URL);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mv != null) {
            if (suspending) {
                mv.resumePlayback();
                suspending = false;
            }
        }
        Log.d(TAG, "onResume called");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mv != null) {
            if (mv.isStreaming()) {
                mv.stopPlayback();
                suspending = true;
            }
        }
        Log.d(TAG, "OnPause called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mv != null) {
            mv.freeCameraMemory();
        }
    }

    public void pauseVideo() {
        if (mv != null) {
            if (mv.isStreaming()) {
                mv.stopPlayback();
                suspending = true;
            }
        }
    }

    public void playVideo() {
        if (mv != null) {
            if (suspending) {
                new ReadDVR().execute(Def.DVR_PREVIEW_URL);
                suspending = false;
            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class ReadDVR extends AsyncTask<String, Void, MjpegInputStream> {
        protected MjpegInputStream doInBackground(String... url) {
            return MjpegInputStream.read(url[0]);
        }

        protected void onPostExecute(MjpegInputStream result) {
            mv.setSource(result);
            if (result != null) {
                result.setSkip(1);
            } else {
            }
            mv.setDisplayMode(MjpegView.SIZE_FULLSCREEN);
            mv.showFps(true);
        }
    }
}
