package com.xianwei.bakingapp;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.xianwei.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xianwei li on 9/22/2017.
 */

public class VideoFragment extends Fragment {
    @BindView(R.id.player_view)
    SimpleExoPlayerView playerView;
    @BindView(R.id.tv_video_description)
    TextView videoDescription;
    @BindView(R.id.btn_step_next)
    ImageButton nextStep;
    @BindView(R.id.btn_step_previous)
    ImageButton previousStep;
    @BindView(R.id.step_btn_layout)
    LinearLayout buttonLayout;
    @BindView(R.id.iv_step_thumbnail)
    ImageView stepImage;

    private static final String VIDEO_POSITION = "videoPosition";
    private static final String SAVED_STEPS = "steps";
    private static final String SAVED_STEP_ID = "stepId";

    private SimpleExoPlayer player;
    private long videoPosition = 0;
    private ArrayList<Step> steps;
    private Step step;
    private int stepId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_player, container, false);
        ButterKnife.bind(this, rootView);

        // if tablet, hind two step-buttons
        if (DetailActivity.twoPane) {
            buttonLayout.setVisibility(View.GONE);
        }

        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(SAVED_STEPS);
            stepId = savedInstanceState.getInt(SAVED_STEP_ID);
            step = steps.get(stepId);
            videoPosition = savedInstanceState.getLong(VIDEO_POSITION);
        } else {
            step = steps.get(stepId);
        }

        if (step.hasVideo()) {
            playerView.setVisibility(View.VISIBLE);
            setupExoPlayer(Uri.parse(step.getVideoURL()));
        } else {
            playerView.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(step.getThumbnailURL())) {
                stepImage.setVisibility(View.VISIBLE);
                setupStepImage(step.getThumbnailURL());
            }
        }

        videoDescription.setText(step.getDescription());
        return rootView;
    }

    private void setupExoPlayer(Uri videoUrlString) {
        if (videoUrlString == null) return;
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        playerView.setPlayer(player);

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(getContext(),
                        Util.getUserAgent(getContext(),
                                "BakingApp"), bandwidthMeter);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource videoSource = new ExtractorMediaSource(videoUrlString,
                dataSourceFactory,
                extractorsFactory,
                null,
                null);
        player.prepare(videoSource);
        if (videoPosition > 0) {
            player.seekTo(videoPosition);
            player.setPlayWhenReady(true);
        }
        //if tablet, don't make full screen video
        if (!DetailActivity.twoPane) {
            setupPlayerView(getResources().getConfiguration().orientation);
        }
    }

    private void setupStepImage(String thumbnailURL) {
        if ( ! TextUtils.isEmpty(thumbnailURL)) {
            Picasso.with(getContext())
                    .load(thumbnailURL)
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_broken_image)
                    .into(stepImage);
        }
    }

    // make full screen when phone rotate to landscape
    private void setupPlayerView(int orientation) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            buttonLayout.setVisibility(View.GONE);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            ((DetailActivity) getActivity()).getSupportActionBar().hide();
        }
    }

    public void setSteps(List<Step> steps) {
        this.steps = (ArrayList<Step>) steps;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (step.hasVideo()) {
            videoPosition = player.getCurrentPosition();
            outState.putLong(VIDEO_POSITION, videoPosition);
        }
        outState.putParcelableArrayList(SAVED_STEPS, steps);
        outState.putInt(SAVED_STEP_ID, stepId);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null) {
            player.setPlayWhenReady(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    //  back to next step
    @OnClick(R.id.btn_step_previous)
    void previousStep() {
        if (stepId > 0) {
            VideoFragment fragment = new VideoFragment();
            fragment.setSteps(steps);
            fragment.setStepId(--stepId);
            // make sure only save one videoFragment in backStack
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStack();
            manager.beginTransaction()
                    .replace(R.id.instruction_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(getContext(), R.string.first_step_toasst, Toast.LENGTH_LONG).show();
        }
    }
    // move to next step
    @OnClick(R.id.btn_step_next)
    void nextStep() {
        if (stepId < steps.size() - 1) {
            VideoFragment fragment = new VideoFragment();
            fragment.setSteps(steps);
            fragment.setStepId(++stepId);
            // make sure only save one videoFragment in backStack
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStack();
            manager.beginTransaction()
                    .replace(R.id.instruction_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(getContext(), R.string.last_step_toast, Toast.LENGTH_LONG).show();
        }
    }
}
