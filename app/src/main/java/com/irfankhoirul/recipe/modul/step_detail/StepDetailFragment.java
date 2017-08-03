package com.irfankhoirul.recipe.modul.step_detail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.irfankhoirul.recipe.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment {


    public StepDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_detail2, container, false);
    }

}
