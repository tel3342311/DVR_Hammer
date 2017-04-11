package com.example.trdcmacpro.dvr_hammer;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trdcmacpro.dvr_hammer.service.DvrInfoService;
import com.example.trdcmacpro.dvr_hammer.util.DVRClient;
import com.example.trdcmacpro.dvr_hammer.util.Def;
import com.example.trdcmacpro.dvr_hammer.util.RecordingItem;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {
    private final static String TAG = ItemFragment.class.getName();
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private DVRClient mDvrClient;
    private static ItemFragment mFragment;
    public void onSysModeChange(String mode) {
        if (mode.equals(Def.STORAGE_MODE)) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    mDvrClient = new DVRClient("admin", "admin");
                    final List<RecordingItem> list = mDvrClient.getRecordingList();

                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(new VideoItemRecyclerViewAdapter(list, mListener));
                        }
                    });
                }
            }.start();
        } else {
            ((MainActivity) getActivity()).showSnackBar("Please change DVR mode to continue.", "Change to Storage mode", mOnSnackBarClickListener);
        }
    }


    private View.OnClickListener mOnSnackBarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "snackbar click");
            setDVRMode();
        }
    };

    private void setDVRMode() {
        Intent intent = new Intent();
        intent.setAction(Def.ACTION_SET_SYS_MODE);
        intent.putExtra(Def.EXTRA_SET_SYS_MODE, Def.STORAGE_MODE);
        intent.setClass(getActivity(), DvrInfoService.class);
        getContext().startService(intent);
    }

    private void checkDVRMode() {
        Intent intent = new Intent();
        intent.setAction(Def.ACTION_GET_SYS_MODE);
        intent.setClass(getActivity(), DvrInfoService.class);
        getContext().startService(intent);
    }

    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        if (mFragment == null) {
            mFragment = new ItemFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_COLUMN_COUNT, columnCount);
            mFragment.setArguments(args);
        }
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        // Set the adapter
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(RecordingItem item);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart called");
    }
}
