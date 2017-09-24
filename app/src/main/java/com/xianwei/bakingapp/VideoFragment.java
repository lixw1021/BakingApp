package com.xianwei.bakingapp;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xianwei li on 9/22/2017.
 */

public class VideoFragment extends Fragment {
    @BindView(R.id.player_view)
    SimpleExoPlayerView playerView;
    @BindView(R.id.tv_video_description)
    TextView videoDescription;

    private static final String POSITION = "position";
    private static final String DESCRIPTION = "description";
    private static final String VIDEO_URI = "videoUri";

    private String videoUriString;
    private String description;
    SimpleExoPlayer player;
    long currentPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_player, container, false);
        ButterKnife.bind(this, rootView);
        ((DetailActivity) getActivity()).getSupportActionBar().hide();

        if (savedInstanceState != null) {
            videoUriString = savedInstanceState.getString(VIDEO_URI);
            description = savedInstanceState.getString(DESCRIPTION);
            currentPosition = savedInstanceState.getLong(POSITION);
        }

        if (videoUriString != null && videoUriString.length() != 0) {
            playerView.setVisibility(View.VISIBLE);
            setupExoPlayer(Uri.parse(videoUriString));
        } else {
            playerView.setVisibility(View.GONE);
        }

        videoDescription.setText(description);
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
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        }
    }

    public void setVideoUriString(String videoUriString) {
        this.videoUriString = videoUriString;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (videoUriString != null && videoUriString.length() != 0) {
            currentPosition = player.getCurrentPosition();
            outState.putLong(POSITION, currentPosition);
            outState.putString(VIDEO_URI, videoUriString);
        }
        outState.putString(DESCRIPTION, description);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.release();
        }
    }
}
