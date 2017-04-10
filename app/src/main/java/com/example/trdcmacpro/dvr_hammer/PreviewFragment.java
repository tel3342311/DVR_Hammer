package com.example.trdcmacpro.dvr_hammer;


import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.camera.simplemjpeg.MjpegInputStream;
import com.camera.simplemjpeg.MjpegView;
import com.example.trdcmacpro.dvr_hammer.service.DvrInfoService;
import com.example.trdcmacpro.dvr_hammer.util.Def;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class PreviewFragment extends Fragment {
    private final static String TAG = PreviewFragment.class.getName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MjpegView mv;
    private ImageView mSnapshot;
    private ImageView mThumbnail;
    private boolean suspending;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static PreviewFragment mFragment;


    public void onSysModeChange(String mode) {
        if (mode.equals(Def.RECORDING_MODE)) {
            if (!mv.isStreaming()) {
                new PreviewFragment.ReadDVR().execute(Def.DVR_PREVIEW_URL);
            }

        } else {
            if (mv.isStreaming()) {
                mv.stopPlayback();
            }
            ((MainActivity) getActivity()).showSnackBar("Please change DVR mode to continue.", "Change to Preview mode",mOnSnackBarClickListener);
        }
    }

    private View.OnClickListener mOnSnackBarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "snackbar click");
            setDVRMode();
        }
    };

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
        mSnapshot = (ImageView) view.findViewById(R.id.snapshot);
        mThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        setListener();
        return view;
    }

    public void setListener() {

        mv.setOnClickListener(mOnClickListener);
        mSnapshot.setOnClickListener(mOnSnapshotClickListener);
    }

    public View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((MainActivity)getActivity()).toggleMenu();
        }
    };

    private View.OnClickListener mOnSnapshotClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ContentResolver cr = getContext().getContentResolver();
            String uri = MediaStore.Images.Media.insertImage(cr, snapShot(), "", "" );
            Log.d(TAG, "The URI of insert image is " + uri);
            long id = ContentUris.parseId(android.net.Uri.parse(uri));
            Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
            mThumbnail.setImageBitmap(miniThumb);
            mThumbnail.setVisibility(View.VISIBLE);

        }
    };

    private Bitmap snapShot() {
        return mv.getBitmap();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void checkDVRMode() {
        Intent intent = new Intent();
        intent.setAction(Def.ACTION_GET_SYS_MODE);
        intent.setClass(getActivity(), DvrInfoService.class);
        getContext().startService(intent);
    }

    private void setDVRMode() {
        Intent intent = new Intent();
        intent.setAction(Def.ACTION_SET_SYS_MODE);
        intent.putExtra(Def.EXTRA_SET_SYS_MODE, Def.RECORDING_MODE);
        intent.setClass(getActivity(), DvrInfoService.class);
        getContext().startService(intent);
    }

    private void resumeVideo() {
        if (mv != null) {
            if (suspending) {
                mv.resumePlayback();
                suspending = false;
            }
        }
    }

    private void pauseVideo() {
        if (mv != null) {
            if (mv.isStreaming()) {
                mv.stopPlayback();
                suspending = true;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkDVRMode();
        resumeVideo();
        Log.d(TAG, "onResume called");
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseVideo();
        Log.d(TAG, "OnPause called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mv != null) {
            mv.freeCameraMemory();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class ReadDVR extends AsyncTask<String, Void, MjpegInputStream> {
        protected MjpegInputStream doInBackground(String... url) {

            try {
                URL _url = new URL(url[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) _url.openConnection();
                InputStream is = urlConnection.getInputStream();
                int c = is.read();
                if (is == null) {
                    return null;
                }
                return MjpegInputStream.read(is);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(MjpegInputStream result) {
            if (result != null) {
                mv.setSource(result);
                result.setSkip(1);
                mv.setDisplayMode(MjpegView.SIZE_FULLSCREEN);
                mv.showFps(true);
            } else {
                Toast.makeText(getContext(),"Fail to open preview URL", Toast.LENGTH_LONG).show();
            }
        }
    }
}
