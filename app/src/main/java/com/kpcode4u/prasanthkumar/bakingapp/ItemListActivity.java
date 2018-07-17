package com.kpcode4u.prasanthkumar.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kpcode4u.prasanthkumar.bakingapp.adapter.StepsAdapter;

import com.kpcode4u.prasanthkumar.bakingapp.model.Ingredients;
import com.kpcode4u.prasanthkumar.bakingapp.model.RecipesResponse;
import com.kpcode4u.prasanthkumar.bakingapp.model.Steps;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private static final String SAVED_LAYOUT_MANAGER = "SavedLayoutManager" ;

    private static final String ingredientsKey = "ingredientsKey";
    private static final String recipeNameKey = "recipeNameKey";
    private static final String stepsKey = "stepsKey";


    @BindView(R.id.ingredients_textView) TextView ingredientTextView;

    private StepsAdapter mStepsAdapter;
    private ArrayList<RecipesResponse> mRecipeList;
    private ArrayList<Steps> mStepsList;
    private ArrayList<Ingredients> mIngredientsList;
    private int position;
    String recipseIngredients;
    private String recipeName;
    private String ingredient_Name,ingredients_Measure,ingredients_Quantity;


    private boolean mTwoPane;
    private ItemListActivity mContext;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.item_list) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);


        if ( findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }

      getRecipseSteps();

        assert recyclerView != null;
        setupRecyclerView(recyclerView);
    }

    private void getRecipseSteps() {

        mStepsList = new ArrayList<>();
        mIngredientsList = new ArrayList<>();

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null) {

            recipeName = bundle.getString(recipeNameKey);

            toolbar.setTitle(""+recipeName);

            mStepsList = bundle.getParcelableArrayList(stepsKey);
            mIngredientsList = bundle.getParcelableArrayList(ingredientsKey);

            //widget adding
            SharedPreferences sharedPreferences = getSharedPreferences("Preference_baking",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            StringBuffer stringBuffer = new StringBuffer();
            int count = 1;
            for (int i = 0; i< mIngredientsList.size(); i++){

                stringBuffer.append(count +""+mIngredientsList.get(i).getIngredient()+"-"+
                        mIngredientsList.get(i).getQuantity()+mIngredientsList.get(i).getMeasure()+"\n");
                count++;
            }
            String line = recipeName +"\n" + stringBuffer.toString();
            editor.putString("widget_List",line);
            editor.apply();


            Intent i = new Intent(this, BakingAppWidget.class);
            i.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] id_List = AppWidgetManager.getInstance(getApplicationContext()).getAppWidgetIds(new ComponentName(getApplication(), BakingAppWidget.class));
            i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,id_List);
            sendBroadcast(i);

/*
            //Widget
            Intent broadCastIntent = new Intent(getApplicationContext(),BakingWidget.class);
            broadCastIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            broadCastIntent.putParcelableArrayListExtra(ingredientsKey,mIngredientsList);
            broadCastIntent.putExtra(recipeNameKey, recipeName);
            int[] id_List = AppWidgetManager.getInstance(getApplicationContext()).getAppWidgetIds(new ComponentName(getApplication(), BakingAppWidget.class));
            broadCastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,id_List);

            getApplicationContext().sendBroadcast(broadCastIntent);
*/

            ingredientTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ItemListActivity.this,IngredientsListActivity.class);

                    intent.putParcelableArrayListExtra(ingredientsKey,mIngredientsList);
                    intent.putParcelableArrayListExtra(stepsKey,mStepsList);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    
                }
            });

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent();
        i.putParcelableArrayListExtra(stepsKey,getIntent().getExtras().getParcelableArrayList("StepsKey"));
        i.putParcelableArrayListExtra(ingredientsKey,getIntent().getExtras().getParcelableArrayList("IngredientsKey"));
        setResult(RESULT_OK, i);
        finish();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new StepsAdapter(ItemListActivity.this, mIngredientsList ,mStepsList,recipeName, mTwoPane));
        recyclerView.scrollToPosition(position);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        getRecipseSteps();
        setupRecyclerView(recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getRecipseSteps();
        setupRecyclerView( recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRecipseSteps();
        setupRecyclerView( recyclerView);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        position = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        outState.putInt(SAVED_LAYOUT_MANAGER, position);
/*
         outState.putString("title",mRecipeList.get(position).getName());
         outState.putParcelableArrayList("ingredientsList",mIngredientsList);
         outState.putParcelableArrayList("stepsKey",mStepsList);
         outState.putBoolean("mTwo",mTwoPane);
*/
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

     /*   recipeName = savedInstanceState.getString(recipeNameKey);
        mIngredientsList = savedInstanceState.getParcelableArrayList(ingredientsKey);
        mStepsList = savedInstanceState.getParcelableArrayList(stepsKey);
        mTwoPane = savedInstanceState.getBoolean("mtwo");
*/
        if (savedInstanceState != null){
            position = savedInstanceState.getInt(SAVED_LAYOUT_MANAGER);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }


}
