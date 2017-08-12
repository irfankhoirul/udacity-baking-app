package com.irfankhoirul.recipe.modul.step_detail;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.irfankhoirul.recipe.R;
import com.irfankhoirul.recipe.data.pojo.Step;
import com.irfankhoirul.recipe.util.DisplayMetricUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_previous_step)
    TextView tvPreviousStep;
    @BindView(R.id.tv_next_step)
    TextView tvNextStep;
    @BindView(R.id.ll_step_navigation)
    LinearLayout llStepNavigation;
    @BindView(R.id.v_horizontal)
    View vHorizontal;

    private ArrayList<Step> steps = new ArrayList<>();
    private int currentStepIndex;
    private StepDetailFragment fragment;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().hasExtra("steps") &&
                getIntent().hasExtra("currentStepIndex")) {
            steps = getIntent().getParcelableArrayListExtra("steps");
            currentStepIndex = getIntent().getIntExtra("currentStepIndex", 0);
            setNavigationButton(currentStepIndex);
        }

        setupFragment(savedInstanceState, currentStepIndex);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "stepDetailFragment", fragment);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (DisplayMetricUtils.getDeviceOrientation(this) == Configuration.ORIENTATION_LANDSCAPE) {
            if (hasFocus) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
            }
            llStepNavigation.setVisibility(View.GONE);
            vHorizontal.setVisibility(View.GONE);
        }
    }

    private void setNavigationButton(int stepIndex) {
        if (steps.size() > 1) {
            if (stepIndex == 0) {
                tvPreviousStep.setVisibility(View.INVISIBLE);
                tvNextStep.setText(steps.get(stepIndex + 1).getShortDescription());
            } else if (stepIndex == steps.size() - 1) {
                tvNextStep.setVisibility(View.INVISIBLE);
                tvPreviousStep.setText(steps.get(stepIndex - 1).getShortDescription());
            } else {
                tvNextStep.setVisibility(View.VISIBLE);
                tvPreviousStep.setVisibility(View.VISIBLE);
                tvNextStep.setText(steps.get(stepIndex + 1).getShortDescription());
                tvPreviousStep.setText(steps.get(stepIndex - 1).getShortDescription());
            }
        } else {
            tvPreviousStep.setVisibility(View.GONE);
            tvNextStep.setVisibility(View.GONE);
        }
    }

    private void setupFragment(Bundle savedInstanceState, int stepIndex) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(steps.get(stepIndex).getShortDescription());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            if (DisplayMetricUtils.getDeviceOrientation(this) == Configuration.ORIENTATION_LANDSCAPE) {
                getSupportActionBar().hide();
            }
        }

        if (savedInstanceState != null) {
            fragment = (StepDetailFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, "stepDetailFragment");
        } else {
            fragment = StepDetailFragment.newInstance(steps.get(stepIndex), false);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rl_container, fragment)
                .commit();
    }

    @OnClick(R.id.tv_next_step)
    public void NextStepClick() {
        currentStepIndex = currentStepIndex + 1;
        setupFragment(savedInstanceState, currentStepIndex);
        setNavigationButton(currentStepIndex);
    }

    @OnClick(R.id.tv_previous_step)
    public void PreviousStepClick() {
        currentStepIndex = currentStepIndex - 1;
        setupFragment(savedInstanceState, currentStepIndex);
        setNavigationButton(currentStepIndex);
    }
}
