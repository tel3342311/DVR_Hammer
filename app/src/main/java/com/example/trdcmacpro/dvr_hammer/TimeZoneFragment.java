package com.example.trdcmacpro.dvr_hammer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimeZoneFragment extends Fragment {


    private TimeZoneFragment() {
        // Required empty public constructor
    }

    public static TimeZoneFragment newInstance() {
        TimeZoneFragment fragment = new TimeZoneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_zone, container, false);
    }

}
