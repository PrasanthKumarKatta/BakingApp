package com.kpcode4u.prasanthkumar.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
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
import com.kpcode4u.prasanthkumar.bakingapp.adapter.Ingredientsadapter;
import com.kpcode4u.prasanthkumar.bakingapp.model.Ingredients;
import com.kpcode4u.prasanthkumar.bakingapp.model.Steps;

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
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    TextView descriptionTv, totalSteps;
    ImageView previous, next;

    //    String videoURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4";
    private String videoURL = null, description;
    private int vId, totalVideoSteps;

    private ArrayList<Steps> stepsVideoList;
    private ArrayList<Ingredients> ingredientsList;
    int position;
    private long videoPosition;

    @BindView(R.id.ingredients_Recyclerview) RecyclerView mRecyclerView;
    private Ingredientsadapter ingredientsadapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            // mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
         //   CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
         /*   if (appBarLayout != null) {
                //appBarLayout.setTitle(mItem.content);
            }
          */
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        ingredientsList = new ArrayList<>();

        ingredientsList = getArguments().getParcelableArrayList("ingredientsList");

        if (ingredientsList != null){
                rootView = inflater.inflate(R.layout.activity_ingredients_list,container,false);
                ButterKnife.bind(this,rootView);

               Toast.makeText(getActivity(), "ingredients : "+ingredientsList.get(position).getIngredient(), Toast.LENGTH_SHORT).show();
                position = getArguments().getInt("position");
                ingredientsadapter = new Ingredientsadapter(getContext(),ingredientsList);
                mRecyclerView.setAdapter(ingredientsadapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                mRecyclerView.scrollToPosition(position);
                ingredientsadapter.notifyDataSetChanged();

            } else {
                rootView = inflater.inflate(R.layout.item_detail, container, false);

                stepsVideoList = new ArrayList<>();

                exoPlayerView = rootView.findViewById(R.id.simpleExoPlayerView);
                descriptionTv = rootView.findViewById(R.id.stepDescription);
                totalSteps = rootView.findViewById(R.id.total_steps);
                previous = rootView.findViewById(R.id.previousVideoStep);
                next = rootView.findViewById(R.id.nextVideoStep);

   /*     if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            position = savedInstanceState.getInt("stepPosition");
            videoPosition = savedInstanceState.getLong("videoPosition");
        }
*/
                stepsVideoList = getArguments().getParcelableArrayList("stepsList");
                position = getArguments().getInt("position");
                videoURL = stepsVideoList.get(position).getVideoURL();
                description = stepsVideoList.get(position).getDescription();
                descriptionTv.setText(description);
                vId = stepsVideoList.get(position).getId();
                totalVideoSteps = stepsVideoList.size()-1;
                totalSteps.setText(vId + "/" + totalVideoSteps);
                callexoplayer();
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position >= 1) {
                            position--;
                            videoURL = stepsVideoList.get(position).getVideoURL();
                            description = stepsVideoList.get(position).getDescription();
                            descriptionTv.setText(description);
                            callexoplayer();
                            exoPlayer.seekTo(0);
                            vId = stepsVideoList.get(position).getId();
                            totalVideoSteps = stepsVideoList.size()-1;
                            totalSteps.setText(vId + "/" + totalVideoSteps);

                        } else {
                            Toast.makeText(getActivity(), "This is the First Step", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position == stepsVideoList.size() - 1){
                            Toast.makeText(getActivity(), "This is the Last Step", Toast.LENGTH_SHORT).show();
                        } else {
                            position++;
                            videoURL = stepsVideoList.get(position).getVideoURL();
                            description = stepsVideoList.get(position).getDescription();
                            descriptionTv.setText(description);
                            callexoplayer();
                            exoPlayer.seekTo(0);

                            vId = stepsVideoList.get(position).getId();
                            totalVideoSteps = stepsVideoList.size()-1;
                            totalSteps.setText(vId + "/" + totalVideoSteps);

                        }
                    }
                });

            }


              return rootView;
    }

    private void callexoplayer() {
        try {

                if (videoURL.equals("")){
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.recipes);
                    exoPlayerView.setDefaultArtwork(bitmap);
                }

                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

                Uri videoURI = Uri.parse(videoURL);

                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

                MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

                exoPlayerView.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(true);
                if (videoPosition !=0){
                    exoPlayer.seekTo(videoPosition);
                }

            } catch (Exception e) {
                Log.e("ExoPlayerActivity", "exoplayer error : " + e.toString());
            }

    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStart() {
        super.onStart();
        callexoplayer();
    }


    /*
    * Release Player
    */

    private void releasePlayer() {
        if (exoPlayer !=null){
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("stepPosition", position);
        outState.putLong("videoPosition", exoPlayer.getCurrentPosition());
    }

}
