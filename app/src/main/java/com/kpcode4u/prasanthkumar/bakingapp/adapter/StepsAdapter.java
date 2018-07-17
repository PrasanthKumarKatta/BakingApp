package com.kpcode4u.prasanthkumar.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kpcode4u.prasanthkumar.bakingapp.ItemDetailActivity;
import com.kpcode4u.prasanthkumar.bakingapp.ItemDetailFragment;
import com.kpcode4u.prasanthkumar.bakingapp.ItemListActivity;
import com.kpcode4u.prasanthkumar.bakingapp.R;
import com.kpcode4u.prasanthkumar.bakingapp.model.Ingredients;
import com.kpcode4u.prasanthkumar.bakingapp.model.Steps;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Prasanth kumar on 25/06/2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsInfo> {

    private static final String ingredientsKey = "ingredientsKey";
    private static final String recipeNameKey = "recipeNameKey";
    private static final String stepsKey = "stepsKey";
    private static final String positionKey = "position";


    private ItemListActivity context;
    private ArrayList<Steps> stepsList;
    private ArrayList<Ingredients> ingredientsArrayList;
    private String recipeName;
    private boolean mTwoPane;
  /*  public StepsAdapter(Context context, ArrayList<Steps> stepsList ) {
        this.context = context;
        this.stepsList = stepsList;

    }*/
    public StepsAdapter(ItemListActivity context, ArrayList<Ingredients> ingredientsArrayList, ArrayList<Steps> stepsList,String recipeName , boolean mTwoPane) {
        this.context = context;
        this.ingredientsArrayList = ingredientsArrayList;
        this.stepsList = stepsList;
        this.recipeName = recipeName;
        this.mTwoPane=mTwoPane;
    }

    @Override
    public StepsInfo onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.steps_card_row,parent,false);
        return  new StepsInfo(v);
    }

    @Override
    public void onBindViewHolder(StepsInfo holder, int position) {

     /*   if (position == 0){
            holder.mIngredients = ingredientsArrayList;
            holder.stepsTitle.setText(ingredientsArrayList.get(position).getIngredient());
        } else {
         //   holder.steps = stepsList.get(position - 1 );
            holder.stepsTitle.setText(stepsList.get(position-1).getShortDescription());
        }*/
        holder.stepsTitle.setText(stepsList.get(position).getShortDescription());

    }

    @Override
    public int getItemCount() {
        if (stepsList != null) {
            return stepsList.size();
        }
        return 0;
    }

    public class StepsInfo extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_steps) TextView stepsTitle;

        public ArrayList<Ingredients> mIngredients;
        public ArrayList<Steps> steps;

        public StepsInfo(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();

                      /*  if (pos == 0){

                            arguments.putParcelableArrayList(ingredientsKey,ingredientsArrayList);

                        } else {
                      */      arguments.putParcelableArrayList(stepsKey, stepsList);
                            arguments.putInt(positionKey,pos);
                            arguments.putString(recipeNameKey,recipeName);


                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        context.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();


                    } else {
                        Context context = v.getContext();
                        //Intent i = new Intent(context, ExoPlayerActivity.class);
                        Intent i = new Intent(context, ItemDetailActivity.class);
                        i.putParcelableArrayListExtra(stepsKey,stepsList);
                        i.putExtra(recipeNameKey,recipeName);
                        i.putExtra(positionKey,pos);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                      //  Toast.makeText(context, "StepsAdapter to ExoPlayer", Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }
    }
}
