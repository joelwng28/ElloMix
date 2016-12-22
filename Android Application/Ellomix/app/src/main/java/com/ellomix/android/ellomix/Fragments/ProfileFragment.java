package com.ellomix.android.ellomix.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ellomix.android.ellomix.R;

/**
 * Created by abetorres on 12/17/16.
 */

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        return v;
    }
}