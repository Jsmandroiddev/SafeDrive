package com.example.jsm.safedrive.fragments;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jsm.safedrive.AccidentZoneActivity;
import com.example.jsm.safedrive.EmergencyCallActivity;
import com.example.jsm.safedrive.NearbyActivity;
import com.example.jsm.safedrive.R;
import com.example.jsm.safedrive.ShareLocationActivity;
import com.example.jsm.safedrive.SpeedSafetyActivity;
import com.example.jsm.safedrive.TripRecordingActivity;
import com.example.jsm.safedrive.databinding.FragmentHomeBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    FragmentHomeBinding fragmentHomeBinding;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentHomeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);

        fragmentHomeBinding.imgTripRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), TripRecordingActivity.class);
                startActivity(intent);
            }
        });

        fragmentHomeBinding.imgShareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShareLocationActivity.class);
                startActivity(intent);
            }
        });

        fragmentHomeBinding.imgEmergencyCallIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EmergencyCallActivity.class);
                startActivity(intent);
            }
        });

        fragmentHomeBinding.imgAccidentZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AccidentZoneActivity.class);
                startActivity(intent);
            }
        });

        fragmentHomeBinding.imgNearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NearbyActivity.class);
                startActivity(intent);
            }
        });

        fragmentHomeBinding.imgSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SpeedSafetyActivity.class);
                startActivity(intent);
            }
        });

/*
        ((MainActivity)getActivity()).getMainBinding().imghome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(container.getContext(),"Home Tab Clicked",Toast.LENGTH_SHORT).show();
            }
        });

        ((MainActivity)getActivity()).getMainBinding().imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(container.getContext(),"Search Tab Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        ((MainActivity) getActivity()).getMainBinding().imgsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(container.getContext(),"Setting Tab Clicked",Toast.LENGTH_SHORT).show();
            }
        });

        ((MainActivity) getActivity()).getMainBinding().imgabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(container.getContext(),"About Tab Clicked",Toast.LENGTH_SHORT).show();
            }
        });

        ((MainActivity) getActivity()).getMainBinding().imgmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(container.getContext(),"More Tab Clicked",Toast.LENGTH_SHORT).show();

            }
        });*/

        return fragmentHomeBinding.getRoot();
    }

}
