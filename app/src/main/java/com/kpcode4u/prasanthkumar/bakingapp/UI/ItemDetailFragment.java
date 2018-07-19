package com.kpcode4u.prasanthkumar.bakingapp.UI;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.kpcode4u.prasanthkumar.bakingapp.R;
import com.kpcode4u.prasanthkumar.bakingapp.adapter.Ingredientsadapter;
import com.kpcode4u.prasanthkumar.bakingapp.model.Ingredients;
import com.kpcode4u.prasanthkumar.bakingapp.model.Steps;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment implements ExoPlayer.EventListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    private static final String ingredientsKey = "ingredientsKey";
    private static final String recipeNameKey = "recipeNameKey";
    private static final String stepsKey = "stepsKey";

    public static final String ARG_ITEM_ID = "item_id";
    private static final String TAG = ItemDetailFragment.class.getSimpleName();
    private String positionKey = "position";
    private String recipeName;

    private long playerStopPosition;
    private boolean stopPlay;
    private long playbackPosition;
    private Boolean playWhenReady = true;


    public ItemDetailFragment() {
    }

    SimpleExoPlayerView exoPlayerView;
    ImageView imageView;
    SimpleExoPlayer exoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    TextView descriptionTv;
    TextView totalSteps;
    ImageView previous;
    ImageView next;

    private String videoURL = null;
    private String description;
    private int vId;
    private int totalVideoSteps;

    private List<Steps> stepsVideoList;
    private List<Ingredients> ingredientsList;
    int position;
    private long videoPosition;

    @BindView(R.id.ingredients_Recyclerview)
    RecyclerView mRecyclerView;

    private Ingredientsadapter ingredientsadapter;

    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            recipeName = getArguments().getString(recipeNameKey);

            Activity activity = this.getActivity();

            if (toolbar != null) {
                toolbar.setTitle("" + recipeName);
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        ingredientsList = new ArrayList<>();


        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("stepPosition");
            playbackPosition = savedInstanceState.getLong("videoPosition");
            playWhenReady = savedInstanceState.getBoolean("play_when_ready");
        }

        ingredientsList = getArguments().getParcelableArrayList(ingredientsKey);

        if (ingredientsList != null) {
            rootView = inflater.inflate(R.layout.activity_ingredients_list, container, false);
            ButterKnife.bind(this, rootView);

            Toast.makeText(getActivity(), "ingredients : " + ingredientsList.get(position).getIngredient(), Toast.LENGTH_SHORT).show();
            position = getArguments().getInt(positionKey);
            ingredientsadapter = new Ingredientsadapter(getContext(), ingredientsList);
            mRecyclerView.setAdapter(ingredientsadapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            mRecyclerView.scrollToPosition(position);
            ingredientsadapter.notifyDataSetChanged();

        } else {
            rootView = inflater.inflate(R.layout.item_detail, container, false);

            stepsVideoList = new ArrayList<>();

            if (getArguments() != null) {
                stepsVideoList = getArguments().getParcelableArrayList(stepsKey);
                position = getArguments().getInt(positionKey);
            }

            exoPlayerView = rootView.findViewById(R.id.simpleExoPlayerView);
            imageView = rootView.findViewById(R.id.thumbnail_image);
            descriptionTv = rootView.findViewById(R.id.stepDescription);
            totalSteps = rootView.findViewById(R.id.total_steps);
            previous = rootView.findViewById(R.id.previousVideoStep);
            next = rootView.findViewById(R.id.nextVideoStep);

            playbackPosition = C.TIME_UNSET;
            imageView.setVisibility(View.GONE);

            description = stepsVideoList.get(position).getDescription();
            descriptionTv.setText(description);
            vId = stepsVideoList.get(position).getId();
            totalVideoSteps = stepsVideoList.size() - 1;
            totalSteps.setText(vId + "/" + totalVideoSteps);

            if (!stepsVideoList.get(position).getVideoURL().contentEquals("")) {
                videoURL = stepsVideoList.get(position).getVideoURL();
                exoPlayerView.setVisibility(View.VISIBLE);
                //Media Session
                initializeMediaSession();
                //calling ExoPlayer
                callexoplayer();
            }

            if (position == 0) {
                previous.setVisibility(View.INVISIBLE);
            }
            if (position == totalVideoSteps) {
                next.setVisibility(View.INVISIBLE);

            }

            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    releasePlayer();
                    callexoplayer();
                    if (position >= 1) {
                        next.setVisibility(View.VISIBLE);
                        position--;
                        videoURL = stepsVideoList.get(position).getVideoURL();
                        description = stepsVideoList.get(position).getDescription();
                        descriptionTv.setText(description);
                        hideUnhideExo();
                        //exoPlayer.stop();
                        exoPlayer.seekTo(0);
                        callexoplayer();

                        vId = stepsVideoList.get(position).getId();
                        totalVideoSteps = stepsVideoList.size() - 1;
                        totalSteps.setText(vId + "/" + totalVideoSteps);

                    } else {
                        previous.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "This is the First Step", Toast.LENGTH_SHORT).show();

                    }

                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     releasePlayer();
                     callexoplayer();

                    if (position == totalVideoSteps) {
                        Toast.makeText(getActivity(), "This is the Last Step", Toast.LENGTH_SHORT).show();
                        next.setVisibility(View.INVISIBLE);
                        previous.setVisibility(View.VISIBLE);

                    } else {
                        previous.setVisibility(View.VISIBLE);
                        next.setVisibility(View.VISIBLE);
                        position++;
                        videoURL = stepsVideoList.get(position).getVideoURL();
                        description = stepsVideoList.get(position).getDescription();
                        descriptionTv.setText(description);
                        hideUnhideExo();
                      //  exoPlayer.stop();
                        exoPlayer.seekTo(0);
                        callexoplayer();

                        vId = stepsVideoList.get(position).getId();
                        totalVideoSteps = stepsVideoList.size() - 1;
                        totalSteps.setText(vId + "/" + totalVideoSteps);

                    }
                }
            });
        }

        return rootView;
    }

    private void initializeMediaSession() {
        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);


    }

    private void callexoplayer() {
        try {

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            LoadControl loadControl = new DefaultLoadControl();

            if (exoPlayer == null) {
                exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            }

            if (exoPlayer != null) {

                if (videoURL.equals("")) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.recipes);
                    exoPlayerView.setDefaultArtwork(bitmap);
                }

                exoPlayer.addListener(this);

                Uri videoURI = Uri.parse(videoURL);

                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

                MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

                exoPlayerView.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(playWhenReady);

                if (playbackPosition != 0 && !stopPlay) {

                    exoPlayer.seekTo(playbackPosition);
                } else {
                    exoPlayer.seekTo(playerStopPosition);
                }

                return;
            }

        } catch (Exception e) {
            Log.e("ExoPlayerActivity", "exoplayer error : " + e.toString());
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            playerStopPosition = exoPlayer.getCurrentPosition();
            stopPlay = true;
            releasePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        playWhenReady = exoPlayer.getPlayWhenReady();
        if (exoPlayer != null) {
            playerStopPosition = exoPlayer.getCurrentPosition();
        }

        if (Util.SDK_INT <=23){
            releasePlayer();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            callexoplayer();
        }
    }
    /*
     * Release Player
     */
    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    private void hideUnhideExo() {
        if (TextUtils.isEmpty(videoURL)) {
            exoPlayerView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(stepsVideoList.get(position).getThumbnailURL())) {
                Picasso.with(getContext()).load(stepsVideoList.get(position).getThumbnailURL());
            }
        } else {
            exoPlayerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("stepPosition", position);
        outState.putLong("videoPosition", exoPlayer.getCurrentPosition());
        outState.putBoolean("play_when_ready", exoPlayer.getPlayWhenReady());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
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
                    exoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    exoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }


    private class MySessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            super.onPlay();

        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
        }
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }


}
