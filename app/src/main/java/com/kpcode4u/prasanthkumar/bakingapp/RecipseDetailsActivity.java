package com.kpcode4u.prasanthkumar.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kpcode4u.prasanthkumar.bakingapp.adapter.StepsAdapter;
import com.kpcode4u.prasanthkumar.bakingapp.model.Ingredients;
import com.kpcode4u.prasanthkumar.bakingapp.model.RecipesResponse;
import com.kpcode4u.prasanthkumar.bakingapp.model.Steps;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipseDetailsActivity extends AppCompatActivity {
    private static final String title_key = "titleKey";
    private static final String shortdescription_key = "shortDescription";
    private static final String description_key = "description";
    private static final String videoURL_key = "videoURL";

    private String SELECTED_RECIPSE_key = "selectedRecipe";


    @BindView(R.id.recyclerView_Steps) RecyclerView recyclerView;

    private StepsAdapter stepsAdapter;
    private ArrayList<RecipesResponse> recipe;
    private ArrayList<Steps> stepsList;
    private ArrayList<Ingredients> ingredientsList;
    private int position;
    String recipseIngredients;
    private String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipse_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
    /*    if (intent.hasExtra(shortdescription_key)){

            String shortDes = getIntent().getExtras().getString(shortdescription_key);
            String description = getIntent().getExtras().getString(description_key);
            String videoURL = getIntent().getExtras().getString(videoURL_key);

            Toast.makeText(this, "shortDes"+shortDes+"\n", Toast.LENGTH_SHORT).show();
        }
    */
        String cakeTitle = intent.getExtras().getString(title_key);
        setTitle(cakeTitle);
     /*   if (intent.hasExtra(title_key)){
            stepsList = new ArrayList<>();
           // stepsList = intent.getExtras().getStringArray("ingredients");

            Toast.makeText(this, "shortdes: "+stepsList.get(1).getShortDescription(), Toast.LENGTH_SHORT).show();
        }*/

     if(savedInstanceState == null ){
         getRecipseSteps();
     }
        initViews();
    }

    private void initViews() {
        stepsList = new ArrayList<>();
        stepsAdapter = new StepsAdapter(this,stepsList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(stepsAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.scrollToPosition(position);
        stepsAdapter.notifyDataSetChanged();

        //getRecipseSteps();
    }

    private void getRecipseSteps() {

        Bundle selectedRecipeBundle = getIntent().getExtras();
        Intent intent = getIntent();
        stepsList = new ArrayList<>();
        ingredientsList = new ArrayList<>();

      //stepsList = intent.getParcelableArrayExtra("StepsKey");

  //        recipe = new ArrayList<>();
 //        recipe = selectedRecipeBundle.getParcelableArrayList(SELECTED_RECIPSE_key);
 //        recipeName = recipe.get(0).getName();
 //        Toast.makeText(this, "id:"+recipe.get(0).getId()+"\n"+recipeName, Toast.LENGTH_SHORT).show();

        /*
        for (RecipesResponse recipe: recipe){

            Toast.makeText(this, " Recipse :"+recipe.getName(), Toast.LENGTH_SHORT).show();
        }
        */

    }

}
