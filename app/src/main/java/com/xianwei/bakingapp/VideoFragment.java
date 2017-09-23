package com.xianwei.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

    private String videoUriString;
    private String description;
    SimpleExoPlayer player;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_player, container, false);
        ButterKnife.bind(this, rootView);
        Log.i("1234","VideoFragment created");
        
        if (videoUriString != null) {
            setupExoPlayer(Uri.parse(videoUriString));
        }
        if (description != null) {
            videoDescription.setText(description);
        }

        return rootView;
    }

    private void setupExoPlayer(Uri videoUrlString) {
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        player =
                ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        playerView.setPlayer(player);

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(getContext(),
                        Util.getUserAgent(getContext(),
                                "BakingApp"), bandwidthMeter);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(videoUrlString,
                dataSourceFactory,
                extractorsFactory,
                null,
                null);
        // Prepare the player with the source.
        player.prepare(videoSource);
    }

    public void setVideoUriString(String videoUriString) {
        this.videoUriString = videoUriString;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void onPause() {
        super.onPause();
        player.release();
    }
}
