package com.kpcode4u.prasanthkumar.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.kpcode4u.prasanthkumar.bakingapp.adapter.Ingredientsadapter;
import com.kpcode4u.prasanthkumar.bakingapp.model.Ingredients;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsListActivity extends AppCompatActivity {

    @BindView(R.id.ingredients_Recyclerview) RecyclerView mRecyclerView;

    ArrayList<Ingredients> ingredientsList;

    private Ingredientsadapter ingredientsadapter;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_list);
        ingredientsList=new ArrayList<>();
        ButterKnife.bind(this);

        if (savedInstanceState ==null){
            getIngredientsData();
        }

    }

    private void getIngredientsData() {
        //ingredientsList = new ArrayList<>();

        Intent i=getIntent();

            ingredientsList = getIntent().getParcelableArrayListExtra("IngredientsKey");
        Toast.makeText(this, ""+ingredientsList.size(), Toast.LENGTH_SHORT).show();

        ingredientsadapter = new Ingredientsadapter(this,ingredientsList);
        mRecyclerView.setAdapter(ingredientsadapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRecyclerView.scrollToPosition(position);
        ingredientsadapter.notifyDataSetChanged();

    }

    private void initViews() {

        Toast.makeText(this, "IngredientsList : "+ingredientsList.size(), Toast.LENGTH_SHORT).show();


    }
}
