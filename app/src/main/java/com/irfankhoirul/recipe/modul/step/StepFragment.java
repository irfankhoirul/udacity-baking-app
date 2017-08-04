package com.irfankhoirul.recipe.modul.step;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.irfankhoirul.recipe.R;
import com.irfankhoirul.recipe.data.pojo.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment implements StepAdapter.StepClickListener {

    @BindView(R.id.rv_step)
    RecyclerView rvStep;

    private StepAdapter stepAdapter;

    public StepFragment() {
        // Required empty public constructor
    }

    public static StepFragment newInstance(ArrayList<Step> steps) {
        StepFragment stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("steps", steps);
        stepFragment.setArguments(bundle);

        return stepFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_list, container, false);
        ButterKnife.bind(this, view);

        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
//        int column = 1;
//        int marginInPixel = DisplayMetricUtils.convertDpToPixel(8);

//        RecyclerViewMarginDecoration decoration =
//                new RecyclerViewMarginDecoration(RecyclerViewMarginDecoration.ORIENTATION_VERTICAL,
//                        marginInPixel, column);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvStep.setLayoutManager(layoutManager);
//        rvStep.addItemDecoration(decoration);
        ArrayList<Step> steps = getArguments().getParcelableArrayList("steps");
        stepAdapter = new StepAdapter(steps, this);
        rvStep.setAdapter(stepAdapter);
    }

    @Override
    public void onStepItemClick(Step step) {

    }
}
