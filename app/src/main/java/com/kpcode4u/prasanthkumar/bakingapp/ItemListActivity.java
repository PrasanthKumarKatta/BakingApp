package com.kpcode4u.prasanthkumar.bakingapp;

import android.content.Context;
import android.content.Intent;
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

    private static final String title_key = "titleKey";
    private static final String shortdescription_key = "shortDescription";
    private static final String description_key = "description";
    private static final String videoURL_key = "videoURL";
    private static final String SAVED_LAYOUT_MANAGER = "SavedLayoutManager" ;

    private String SELECTED_RECIPSE_key = "selectedRecipe";

    @BindView(R.id.ingredients_textView) TextView ingredientTextView;
//    @BindView(R.id.recyclerView_Steps) RecyclerView recyclerView;

    private StepsAdapter stepsAdapter;
    private ArrayList<RecipesResponse> recipeList;
    private ArrayList<Steps> stepsList;
    private ArrayList<Ingredients> ingredientsList;
    private int position;
    String recipseIngredients;
    private String recipeName;
    private String ingredient_Name,ingredients_Measure,ingredients_Quantity;


    private boolean mTwoPane;
    Toolbar toolbar;
    private ItemListActivity context;
    @BindView(R.id.item_list) RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        ButterKnife.bind(this);


        if(savedInstanceState == null ){
            getRecipseSteps();
            //  setTitle(recipeName);
        }


       // View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);
    }

   /* private void initViews() {

        stepsAdapter = new StepsAdapter(this,stepsList,mTwoPane);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(stepsAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.scrollToPosition(position);
        stepsAdapter.notifyDataSetChanged();

    }
*/
    private void getRecipseSteps() {

        // Bundle selectedRecipeBundle = getIntent().getExtras();
        // Intent intent = getIntent();
        stepsList = new ArrayList<>();
        ingredientsList = new ArrayList<>();

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null) {

           // setTitle(recipeName);
            toolbar.setTitle(""+bundle.getString("titlekey"));

            stepsList = bundle.getParcelableArrayList("StepsKey");

          /*  String step = stepsList.get(0).getShortDescription();
            Toast.makeText(this, "steps"+step, Toast.LENGTH_SHORT).show();
*/
            ingredientsList = bundle.getParcelableArrayList("IngredientsKey");


            ingredientTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //    ingredientsList = new ArrayList<>();
                    Intent intent = new Intent(ItemListActivity.this,IngredientsListActivity.class);

                 //   Intent intent = new Intent(ItemListActivity.this,ItemDetailActivity.class);

                    //  Bundle bundle = new Bundle();
                    intent.putParcelableArrayListExtra("IngredientsKey",ingredientsList);
                    intent.putParcelableArrayListExtra("StepsKey",stepsList);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent,1);

                 /*   if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelableArrayList("ingredientsList", ingredientsList);
                        arguments.putInt("position",position);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        context.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                       // Intent i = new Intent(context, ExoPlayerActivity.class);
                        Intent i = new Intent(context, ItemDetailActivity.class);
                        i.putParcelableArrayListExtra("ingredientsList",ingredientsList);
                        i.putExtra("position",position);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        Toast.makeText(context, "ingredientsActivity masterdetails", Toast.LENGTH_SHORT).show();
                    }*/
                    
                }
            });

            addToWidget();
        }

    }

    private void addToWidget() {

        Intent broadCastIntent = new Intent(getApplicationContext(),BakingWidget.class);
        broadCastIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        broadCastIntent.putExtra("ingredients",ingredientsList);
        broadCastIntent.putExtra("recipeNames",recipeName);
        getApplicationContext().sendBroadcast(broadCastIntent);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent();
        i.putParcelableArrayListExtra("StepsKey",getIntent().getExtras().getParcelableArrayList("StepsKey"));
        i.putParcelableArrayListExtra("IngredientsKey",getIntent().getExtras().getParcelableArrayList("IngredientsKey"));
        setResult(RESULT_OK, i);
        finish();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new StepsAdapter(ItemListActivity.this, stepsList, mTwoPane));
        recyclerView.scrollToPosition(position);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        getRecipseSteps();
        setupRecyclerView((RecyclerView) recyclerView);
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

     /*    outState.putString("title",recipeList.get(position).getName());
         outState.putParcelableArrayList("ingredientsList",ingredientsList);
         outState.putParcelableArrayList("stepsKey",stepsList);
         outState.putBoolean("mTwo",mTwoPane);
     */
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            position = savedInstanceState.getInt(SAVED_LAYOUT_MANAGER);
        }
        super.onRestoreInstanceState(savedInstanceState);

        /*  recipeName = savedInstanceState.getString("title");
           ingredientsList = savedInstanceState.getParcelableArrayList("ingredientsList");
           stepsList = savedInstanceState.getParcelableArrayList("stepsKey");
           mTwoPane = savedInstanceState.getBoolean("mTwo");
        */
    }


}
