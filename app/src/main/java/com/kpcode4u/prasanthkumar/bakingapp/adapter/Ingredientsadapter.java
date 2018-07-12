package com.kpcode4u.prasanthkumar.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kpcode4u.prasanthkumar.bakingapp.R;
import com.kpcode4u.prasanthkumar.bakingapp.model.Ingredients;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Prasanth kumar on 28/06/2018.
 */

public class Ingredientsadapter extends RecyclerView.Adapter<Ingredientsadapter.IngredientInfo> {

    Context context;
    ArrayList<Ingredients> ingredientsList;

    public Ingredientsadapter(Context context, ArrayList<Ingredients> ingredientsList) {
        this.context = context;
        this.ingredientsList = ingredientsList;
    }

    @Override
    public IngredientInfo onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.ingredientd_row,parent,false);
        return new IngredientInfo(v);
    }

    @Override
    public void onBindViewHolder(IngredientInfo holder, int position) {
        holder.ingredientTitle.setText(ingredientsList.get(position).getIngredient());
        holder.measurement.setText(ingredientsList.get(position).getMeasure());
        holder.quantity.setText(String.valueOf(ingredientsList.get(position).getQuantity()));


    }

    @Override
    public int getItemCount() {
        if (ingredientsList == null){
            return 0;
            //Toast.makeText(, "No Ingredients found", Toast.LENGTH_SHORT).show();
        }
        return ingredientsList.size();
    }

    public class IngredientInfo extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_ingredients) TextView ingredientTitle;
        @BindView(R.id.textview_mesurement) TextView measurement;
        @BindView(R.id.textview_quantity) TextView quantity;

        public IngredientInfo(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
