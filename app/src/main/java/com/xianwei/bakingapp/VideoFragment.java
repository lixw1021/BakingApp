package com.xianwei.bakingapp;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

    private static final String VIDEO_POSITION = "videoPosition";

    SimpleExoPlayer player;
    long currentPosition = 0;
    private ArrayList<Step> steps;
    private Step step;
    private int stepPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_player, container, false);
        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList("steps");
            stepPosition = savedInstanceState.getInt("position");
            step = steps.get(stepPosition);
            currentPosition = savedInstanceState.getLong(VIDEO_POSITION);
        } else {
            step = steps.get(stepPosition);
        }

        if (step.hasVideo()) {
            playerView.setVisibility(View.VISIBLE);
            setupExoPlayer(Uri.parse(step.getVideoURL()));
        } else {
            playerView.setVisibility(View.GONE);
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
        player.seekTo(currentPosition);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            buttonLayout.setVisibility(View.GONE);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            ((DetailActivity)getActivity()).getSupportActionBar().hide();

        }
    }

    public void setSteps(List<Step> steps) {
        this.steps = (ArrayList<Step>) steps;
    }

    public void setStepPosition(int stepPosition) {
        this.stepPosition = stepPosition;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (step.hasVideo()) {
            currentPosition = player.getCurrentPosition();
            outState.putLong(VIDEO_POSITION, currentPosition);
        }
        outState.putParcelableArrayList("steps", steps);
        outState.putInt("position", stepPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.release();
        }
    }

    @OnClick(R.id.btn_step_previous)
    void previousStep() {
        if (stepPosition > 0) {
            VideoFragment fragment = new VideoFragment();
            fragment.setSteps(steps);
            fragment.setStepPosition(--stepPosition);

            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.instruction_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(getContext(), "this is the first step", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.btn_step_next)
    void nextStep() {
        if (stepPosition < steps.size() - 1) {
            VideoFragment fragment = new VideoFragment();
            fragment.setSteps(steps);
            fragment.setStepPosition(++stepPosition);

            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.instruction_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(getContext(), "this is the last step", Toast.LENGTH_LONG).show();
        }
    }
}
