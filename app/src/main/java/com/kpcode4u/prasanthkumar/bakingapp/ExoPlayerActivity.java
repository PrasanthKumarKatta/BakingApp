package com.kpcode4u.prasanthkumar.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.kpcode4u.prasanthkumar.bakingapp.model.Steps;

import java.util.ArrayList;
import java.util.List;

public class ExoPlayerActivity extends AppCompatActivity {

    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    TextView descriptionTv,totalSteps;
    ImageView previous,next;

//    String videoURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4";
    private String videoURL, description ;
    private int vId, totalVideoSteps,pos;

    private List<Steps> stepsList;
    int p;


   // private int vId, totalVideoSteps;

    private ArrayList<Steps> stepsVideoList;
    int position;
    private long videoPosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);

        exoPlayerView = findViewById(R.id.simpleExoPlayerView);
        descriptionTv = findViewById(R.id.stepDescription);
        totalSteps = findViewById(R.id.total_steps);
        previous = findViewById(R.id.previousVideoStep);
        next = findViewById(R.id.nextVideoStep);

        stepsVideoList = getIntent().getParcelableArrayListExtra("stepsList");
        position = getIntent().getExtras().getInt("position");
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
                } else {
                    Toast.makeText(ExoPlayerActivity.this, "This is the First Step", Toast.LENGTH_SHORT).show();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == stepsVideoList.size() - 1) {
                    Toast.makeText(ExoPlayerActivity.this, "This is the Last Step", Toast.LENGTH_SHORT).show();
                } else {
                    position++;
                    videoURL = stepsVideoList.get(position).getVideoURL();
                    description = stepsVideoList.get(position).getDescription();
                    callexoplayer();
                    exoPlayer.seekTo(0);
                }
            }
        });

    }

    private void callexoplayer() {
        try {

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this,trackSelector);

            Uri videoURI = Uri.parse(videoURL);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            MediaSource mediaSource = new ExtractorMediaSource(videoURI,dataSourceFactory, extractorsFactory, null , null);

            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);

        }catch (Exception e){
            Log.e("ExoPlayerActivity","exoplayer error : "+e.toString());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
    /*
    * Release Player
    */
    private void releasePlayer(){
        if (exoPlayer != null){
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

    }
}
