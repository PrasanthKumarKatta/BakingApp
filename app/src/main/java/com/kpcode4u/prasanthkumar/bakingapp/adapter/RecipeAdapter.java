package com.kpcode4u.prasanthkumar.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kpcode4u.prasanthkumar.bakingapp.ItemListActivity;
import com.kpcode4u.prasanthkumar.bakingapp.R;
import com.kpcode4u.prasanthkumar.bakingapp.model.Ingredients;
import com.kpcode4u.prasanthkumar.bakingapp.model.RecipesResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Prasanth kumar on 24/06/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipesInfo> {

    private Context context;
    private ArrayList<RecipesResponse> recipesList;
    private ArrayList<Ingredients> ingredientsList;
    private String SELECTED_RECIPSE_key = "selectedRecipe";

    public RecipeAdapter(Context context, ArrayList<RecipesResponse> recipesList) {
        this.context = context;
        this.recipesList = recipesList;
    }

    @Override
    public RecipesInfo onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recipes_card,parent,false);
        return new RecipesInfo(v);
    }

    @Override
    public void onBindViewHolder(RecipesInfo holder, final int position) {
        holder.recipeTitle.setText(recipesList.get(position).getName());

   /*  holder.setItemClickListener(new ItemClickListener() {
         @Override
         public void onItemClickListener(RecipesResponse clickedItemIndexRecipse) {

             passDataToDetailsActivity(clickedItemIndexRecipse);
         }
     });
*/
    }


    @Override
    public int getItemCount() {
        if (recipesList == null){
            return 0;
        }
        return recipesList.size();
    }

    public class RecipesInfo extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        @BindView(R.id.recipe_title_id) TextView recipeTitle;

        public RecipesInfo(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){

                       // Intent intent = new Intent(context, RecipseDetailsActivity.class);
                        Intent intent = new Intent(context, ItemListActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putInt("position",pos);

                        bundle.putInt("id",recipesList.get(pos).getId());
                        intent.putExtra("titlekey",recipesList.get(pos).getName());
                        bundle.putInt("Servings",recipesList.get(pos).getServings());

                        intent.putParcelableArrayListExtra("StepsKey",recipesList.get(pos).getSteps());
                        intent.putParcelableArrayListExtra("IngredientsKey",recipesList.get(pos).getIngredients());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }
                }
            });
        }

    }
}
