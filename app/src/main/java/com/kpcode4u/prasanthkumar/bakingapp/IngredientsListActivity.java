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
    private static final String ingredientsKey = "ingredientsKey";
    private static final String recipeNameKey = "recipeNameKey";
    private static final String stepsKey = "stepsKey";


    private Ingredientsadapter ingredientsadapter;
    private int position;
    private String SAVED_LAYOUT_MANAGER = "SavedLayoutManager";

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

            ingredientsList = getIntent().getParcelableArrayListExtra(ingredientsKey);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Intent i=getIntent();
        ingredientsList = getIntent().getParcelableArrayListExtra(ingredientsKey);
        outState.putParcelableArrayList(ingredientsKey,ingredientsList);

        position = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        outState.putInt(SAVED_LAYOUT_MANAGER, position);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState != null){
            ingredientsList = savedInstanceState.getParcelableArrayList(ingredientsKey);
            position = savedInstanceState.getInt(SAVED_LAYOUT_MANAGER);
        }
        super.onRestoreInstanceState(savedInstanceState);

    }
}
