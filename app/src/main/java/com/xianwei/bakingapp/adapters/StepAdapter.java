package com.xianwei.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xianwei.bakingapp.DetailActivity;
import com.xianwei.bakingapp.R;
import com.xianwei.bakingapp.VideoFragment;
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
        holder.stepId = position;
        if (currentStep.hasVideo()) {
            holder.stepVideoImage.setVisibility(View.VISIBLE);
        } else {
            holder.stepVideoImage.setVisibility(View.INVISIBLE);
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
        int stepId;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoFragment fragment = new VideoFragment();
                    fragment.setSteps(steps);
                    fragment.setStepId(stepId);

                    // check two pane, if yes, replace new fragment and don't put into backStack
                    if (DetailActivity.twoPane) {
                        ((DetailActivity) itemView.getContext())
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.video_container, fragment)
                                .commit();
                    } else {
                        ((DetailActivity) itemView.getContext())
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.instruction_container, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            });
        }
    }
}
