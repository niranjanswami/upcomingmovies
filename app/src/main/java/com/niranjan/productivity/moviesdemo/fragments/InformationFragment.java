package com.niranjan.productivity.moviesdemo.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.niranjan.productivity.moviesdemo.R;

import static com.niranjan.productivity.moviesdemo.activities.HomeActivity.updateToolBar;

public class InformationFragment extends BaseFragment {

    private View rootView;
    static InformationFragment fragment;

    public static InformationFragment getInstance() {
        if (fragment == null) {
            fragment = new InformationFragment();
        }
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateToolBar(getString(R.string.information));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_information, container, false);


        return rootView;
    }

}
