package com.irfankhoirul.recipe.modul.step;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.irfankhoirul.recipe.R;
import com.irfankhoirul.recipe.data.pojo.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Irfan Khoirul on 7/25/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private ArrayList<Step> steps;
    private StepClickListener clickListener;

    public StepAdapter(ArrayList<Step> steps, StepClickListener clickListener) {
        this.steps = steps;
        this.clickListener = clickListener;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step, parent, false);

        return new StepAdapter.StepViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        Step item = steps.get(position);
        if (position < getItemCount() - 1) {
            holder.vStepConnector.setVisibility(View.VISIBLE);
            Log.v("StepConnector", "Visible");
        } else {
            holder.vStepConnector.setVisibility(View.INVISIBLE);
            Log.v("StepConnector", "Invisible");
        }
        holder.tvStepShortDescription.setText(item.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    interface StepClickListener {
        void onStepItemClick(Step step);
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ll_step)
        LinearLayout llStep;
        @BindView(R.id.tv_step_short_description)
        TextView tvStepShortDescription;
        @BindView(R.id.v_step_connector)
        View vStepConnector;

        StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvStepShortDescription.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onStepItemClick(steps.get(getAdapterPosition()));
        }
    }
}
