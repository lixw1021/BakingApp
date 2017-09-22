package com.xianwei.bakingapp.adapters;

import android.content.Context;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xianwei.bakingapp.DetailActivity;
import com.xianwei.bakingapp.InstructionFragment;
import com.xianwei.bakingapp.MainActivity;
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
    DetailActivity detailActivity;

    public StepAdapter(DetailActivity detailActivity, List<Step> steps) {
        this.detailActivity = detailActivity;
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
        holder.description = currentStep.getDescription();
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
        String description;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoFragment fragment = new VideoFragment();
                    fragment.setVideoUriString(videoUrl);
                    fragment.setDescription(description);

                    detailActivity
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.video_fragment_container, fragment)
                            .commit();
                }
            });
        }
    }
}
