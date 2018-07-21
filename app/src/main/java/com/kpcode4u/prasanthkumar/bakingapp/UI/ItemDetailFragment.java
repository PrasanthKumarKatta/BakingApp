package com.kpcode4u.prasanthkumar.bakingapp.UI;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
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
public class ItemDetailFragment extends Fragment{
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

    @BindView(R.id.ingredients_Recyclerview)
    RecyclerView mRecyclerView;

    private Ingredientsadapter ingredientsadapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            recipeName = getArguments().getString(recipeNameKey);
            ActionBar actionBar = null;
            actionBar.setTitle(""+recipeName);
            Activity activity = this.getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        ingredientsList = new ArrayList<>();

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

            if (savedInstanceState != null) {
                position = savedInstanceState.getInt("stepPosition");
                playbackPosition = savedInstanceState.getLong("videoPosition",C.TIME_UNSET);
                playWhenReady = savedInstanceState.getBoolean("play_when_ready");
            }

            description = stepsVideoList.get(position).getDescription();
            descriptionTv.setText(description);
            vId = stepsVideoList.get(position).getId();
            totalVideoSteps = stepsVideoList.size() - 1;
            totalSteps.setText(vId + "/" + totalVideoSteps);

            if (!stepsVideoList.get(position).getVideoURL().contentEquals("")) {
                videoURL = stepsVideoList.get(position).getVideoURL();
                exoPlayerView.setVisibility(View.VISIBLE);
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
                    next.setVisibility(View.VISIBLE);
                    position--;
                    videoURL = stepsVideoList.get(position).getVideoURL();
                    description = stepsVideoList.get(position).getDescription();
                    descriptionTv.setText(description);
                    if (position == 0)
                    {
                        previous.setVisibility(View.INVISIBLE);
                    }
                    hideUnhideExo();
                   // callexoplayer();
                    callexoplayer();
                    exoPlayer.seekTo(0);

                    vId = stepsVideoList.get(position).getId();
                    totalVideoSteps = stepsVideoList.size() - 1;
                    totalSteps.setText(vId + "/" + totalVideoSteps);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releasePlayer();
               // callexoplayer();
                previous.setVisibility(View.VISIBLE);
                position++;
                videoURL = stepsVideoList.get(position).getVideoURL();
                description = stepsVideoList.get(position).getDescription();
                descriptionTv.setText(description);
                if (position == totalVideoSteps) {
                    Toast.makeText(getActivity(), "This is the Last Step", Toast.LENGTH_SHORT).show();
                    next.setVisibility(View.INVISIBLE);
                }
                hideUnhideExo();
                callexoplayer();
                exoPlayer.seekTo(0);

                vId = stepsVideoList.get(position).getId();
                totalVideoSteps = stepsVideoList.size() - 1;
                totalSteps.setText(vId + "/" + totalVideoSteps);

            }
        });
    }

        return rootView;
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

                Uri videoURI = Uri.parse(videoURL);

                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

                MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

                exoPlayerView.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(playWhenReady);

                if(playbackPosition !=0 && !stopPlay)
                {
                    exoPlayer.seekTo(playbackPosition);
                }
                else {
                    exoPlayer.seekTo(playerStopPosition);
                }
            }

        } catch (Exception e) {
            Log.e("ExoPlayerActivity", "exoplayer error : " + e.toString());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            callexoplayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
         /*if (exoPlayer != null)
         {
            playerStopPosition = exoPlayer.getCurrentPosition();
         }*/
        if ((Util.SDK_INT <= 23 || exoPlayer == null))
        {
            callexoplayer();
            exoPlayer.seekTo(playerStopPosition);

            exoPlayer.setPlayWhenReady(playWhenReady);
        }

    }

    @Override
        public void onPause() {
            super.onPause();
            playWhenReady = exoPlayer.getPlayWhenReady();
            if (exoPlayer != null) {
                playerStopPosition = exoPlayer.getCurrentPosition();
                exoPlayer.stop();
            }
/*
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }*/
    }
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (exoPlayer != null){
                playerStopPosition = exoPlayer.getCurrentPosition();
                stopPlay = true;
                releasePlayer();
                }
        }
    }
    /*
     * Release Player
     */
    private void releasePlayer()
    {
        if (exoPlayer != null)
        {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        releasePlayer();
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
        outState.putLong("playbackPosition", exoPlayer.getCurrentPosition());
        outState.putBoolean("play_when_ready", exoPlayer.getPlayWhenReady());
    }


}
