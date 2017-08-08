package com.irfankhoirul.recipe.modul.step_detail;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.irfankhoirul.recipe.R;
import com.irfankhoirul.recipe.data.pojo.Step;
import com.irfankhoirul.recipe.util.DisplayMetricUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener {

    private static MediaSessionCompat mMediaSession;
    @BindView(R.id.tv_step_description)
    TextView tvStepDescription;
    @BindView(R.id.video_player_view)
    SimpleExoPlayerView videoPlayerView;
    @BindView(R.id.pb_buffering)
    ProgressBar pbBuffering;
    private Step step;
    private SimpleExoPlayer mExoPlayer;
    private PlaybackStateCompat.Builder mStateBuilder;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    public static StepDetailFragment newInstance(Step step) {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("step", step);
        stepDetailFragment.setArguments(bundle);

        return stepDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            step = getArguments().getParcelable("step");
            if (step != null) {
                tvStepDescription.setText(step.getDescription());
                if ((step.getVideoURL() == null || step.getVideoURL().isEmpty()) &&
                        (step.getThumbnailURL() == null || step.getThumbnailURL().isEmpty())) {
                    videoPlayerView.setVisibility(View.GONE);
                } else {
                    if (DisplayMetricUtils.getDeviceOrientation(getActivity()) ==
                            Configuration.ORIENTATION_LANDSCAPE) {
                        ViewGroup.LayoutParams layoutParams =
                                videoPlayerView.getLayoutParams();
                        layoutParams.width = DisplayMetricUtils.getDeviceWidth(getActivity());
                        layoutParams.height = DisplayMetricUtils.getDeviceHeight(getActivity());
                        videoPlayerView.setLayoutParams(layoutParams);
                        tvStepDescription.setVisibility(View.GONE);
                    } else {
                        ViewGroup.LayoutParams layoutParams =
                                videoPlayerView.getLayoutParams();
                        layoutParams.width = DisplayMetricUtils.getDeviceWidth(getActivity());
                        layoutParams.height = (int) (9.0f / 16.0f * layoutParams.width);
                        videoPlayerView.setLayoutParams(layoutParams);
                    }

                    initializeMediaSession();
                    if (step.getVideoURL() != null && !step.getVideoURL().equalsIgnoreCase("")) {
                        initializePlayer(savedInstanceState, Uri.parse(step.getVideoURL()));
                    } else if (step.getThumbnailURL() != null && !step.getThumbnailURL().equalsIgnoreCase("")) {
                        initializePlayer(savedInstanceState, Uri.parse(step.getThumbnailURL()));
                    }
                }
            }
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mExoPlayer != null) {
            outState.putLong("currentVideoPosition", mExoPlayer.getCurrentPosition());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releasePlayer();
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getActivity(), "Recipe");
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        mMediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                mExoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                mExoPlayer.setPlayWhenReady(false);
            }

            @Override
            public void onSkipToPrevious() {
                mExoPlayer.seekTo(0);
            }
        });

        mMediaSession.setActive(true);
    }

    private void initializePlayer(Bundle savedInstanceState, Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            if (savedInstanceState != null) {
                mExoPlayer.seekTo(savedInstanceState.getLong("currentVideoPosition"));
            }

            videoPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getActivity(), "Recipe");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
            pbBuffering.setVisibility(View.GONE);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
            pbBuffering.setVisibility(View.GONE);
        } else if (playbackState == ExoPlayer.STATE_BUFFERING) {
            pbBuffering.setVisibility(View.VISIBLE);
        } else {
            pbBuffering.setVisibility(View.GONE);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
