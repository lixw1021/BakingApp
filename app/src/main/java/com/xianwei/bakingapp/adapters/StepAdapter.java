package com.xianwei.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xianwei.bakingapp.R;
import com.xianwei.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xianwei li on 9/22/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
    private List<Step> steps;

    public StepAdapter(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public StepAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_step, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(StepAdapter.ViewHolder holder, int position) {
        Step currentStep = steps.get(position);
        holder.stepIdTv.setText(currentStep.getStepId());
        holder.stepDescription.setText(currentStep.getShortDescription());
        holder.videoUrl = currentStep.getVideoURL();
        if (holder.videoUrl == null || holder.videoUrl.length() == 0) {
            holder.stepVideoImage.setVisibility(View.INVISIBLE);
        } else {
            holder.stepVideoImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (steps == null) return 0;
        return steps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_step_id)
        TextView stepIdTv;
        @BindView(R.id.tv_step_description)
        TextView stepDescription;
        @BindView(R.id.iv_step_video)
        ImageView stepVideoImage;
        String videoUrl;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
