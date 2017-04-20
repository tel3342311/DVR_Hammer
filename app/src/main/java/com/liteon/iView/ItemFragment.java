package com.liteon.iView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.liteon.iView.service.DvrInfoService;
import com.liteon.iView.util.DVRClient;
import com.liteon.iView.util.Def;
import com.liteon.iView.util.RecordingItem;
import com.liteon.iView.util.VideoItemRecyclerViewAdapter;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

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
    private List<RecordingItem> mlist;
    private static ItemFragment mFragment;
    public void onSysModeChange(String mode) {
        if (mode.equals(Def.STORAGE_MODE)) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    mDvrClient = DVRClient.newInstance(getContext());
                    mlist = mDvrClient.getRecordingList();

                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(new VideoItemRecyclerViewAdapter(mlist, mListener));
                        }
                    });
                }
            }.start();
        } else {
            SharedPreferences sp = getContext().getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
            String json = sp.getString(Def.SP_RECORDING_LIST, "");
            Type typeOfList = new TypeToken<List<RecordingItem>>() { }.getType();
            Gson gson = new GsonBuilder().create();
            mlist = gson.fromJson(json, typeOfList);
            recyclerView.setAdapter(new VideoItemRecyclerViewAdapter(mlist, mListener));
        }
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

    @Override
    public void onResume() {
        super.onResume();
        onSysModeChange(Def.RECORDING_MODE);

    }
}
