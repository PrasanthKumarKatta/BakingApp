package com.kpcode4u.prasanthkumar.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kpcode4u.prasanthkumar.bakingapp.ExoPlayerActivity;
import com.kpcode4u.prasanthkumar.bakingapp.ItemDetailActivity;
import com.kpcode4u.prasanthkumar.bakingapp.ItemDetailFragment;
import com.kpcode4u.prasanthkumar.bakingapp.ItemListActivity;
import com.kpcode4u.prasanthkumar.bakingapp.R;
import com.kpcode4u.prasanthkumar.bakingapp.model.Steps;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Prasanth kumar on 25/06/2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsInfo> {



    private ItemListActivity context;
    private ArrayList<Steps> stepsList;
    private boolean mTwoPane;
  /*  public StepsAdapter(Context context, ArrayList<Steps> stepsList ) {
        this.context = context;
        this.stepsList = stepsList;

    }*/
    public StepsAdapter(ItemListActivity context, ArrayList<Steps> stepsList , boolean mTwoPane) {
        this.context = context;
        this.stepsList = stepsList;
        this.mTwoPane=mTwoPane;
    }

    @Override
    public StepsInfo onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.steps_card_row,parent,false);
        return  new StepsInfo(v);
    }

    @Override
    public void onBindViewHolder(StepsInfo holder, int position) {
        holder.stepsTitle.setText(stepsList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    public class StepsInfo extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_steps) TextView stepsTitle;

        public StepsInfo(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelableArrayList("stepsList", stepsList);
                        arguments.putInt("position",pos);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        context.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent i = new Intent(context, ExoPlayerActivity.class);
                        //Intent i = new Intent(context, ItemDetailActivity.class);
                        i.putParcelableArrayListExtra("stepsList",stepsList);
                        i.putExtra("position",pos);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        Toast.makeText(context, "StepsAdapter to ExoPlayer", Toast.LENGTH_SHORT).show();
                    }

/*
                    Context context = v.getContext();
                    Intent i = new Intent(context, ExoPlayerActivity.class);
                    //Intent i = new Intent(context, ItemDetailActivity.class);
                    i.putParcelableArrayListExtra("stepsList",stepsList);
                    i.putExtra("position",pos);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    Toast.makeText(context, "StepsAdapter to ExoPlayer", Toast.LENGTH_SHORT).show();
*/

                }
            });


        }
    }
}
