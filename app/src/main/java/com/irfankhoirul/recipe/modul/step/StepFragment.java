package com.irfankhoirul.recipe.modul.step;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.irfankhoirul.recipe.R;
import com.irfankhoirul.recipe.data.pojo.Step;
import com.irfankhoirul.recipe.modul.step_detail.StepDetailActivity;

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
    private ArrayList<Step> steps = new ArrayList<>();
    private boolean isTablet;
    private StepFragmentListener fragmentListener;

    public StepFragment() {
        // Required empty public constructor
    }

    public static StepFragment newInstance(ArrayList<Step> steps, boolean isTablet) {
        StepFragment stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("steps", steps);
        bundle.putBoolean("isTablet", isTablet);
        stepFragment.setArguments(bundle);

        return stepFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentListener = (StepFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_list, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            steps = getArguments().getParcelableArrayList("steps");
            isTablet = getArguments().getBoolean("isTablet");
        }
        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvStep.setLayoutManager(layoutManager);
        stepAdapter = new StepAdapter(steps, this);
        rvStep.setAdapter(stepAdapter);
    }

    @Override
    public void onStepItemClick(Step step) {
        if (isTablet) {
            fragmentListener.onStepClicked(step);
        } else {
            Intent intent = new Intent(getActivity(), StepDetailActivity.class);
            intent.putParcelableArrayListExtra("steps", steps);
            intent.putExtra("currentStepIndex", steps.indexOf(step));
            startActivity(intent);
        }
    }

    public interface StepFragmentListener {
        void onStepClicked(Step step);
    }
}
