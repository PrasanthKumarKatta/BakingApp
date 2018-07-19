package com.kpcode4u.prasanthkumar.bakingapp.UI;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.kpcode4u.prasanthkumar.bakingapp.R;
import com.kpcode4u.prasanthkumar.bakingapp.model.Ingredients;
import com.kpcode4u.prasanthkumar.bakingapp.model.Steps;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity {

  private static final String ingredientsKey = "ingredientsKey";
  private static final String recipeNameKey = "recipeNameKey";
  private static final String stepsKey = "stepsKey";
  private static final String positionKey = "position";

  private ArrayList<Steps> stepsVideoList;
  private ArrayList<Ingredients> ingredientsList;
  private int position;
  @BindView(R.id.toolbar) Toolbar toolbar;
  private String recipse;
    private String mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        stepsVideoList = new ArrayList<>();
        ingredientsList = new ArrayList<>();

        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        */
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            stepsVideoList = getIntent().getParcelableArrayListExtra(stepsKey);
            ingredientsList = getIntent().getParcelableArrayListExtra(ingredientsKey);
            position = getIntent().getExtras().getInt(positionKey);
            recipse =getIntent().getExtras().getString(recipeNameKey);
            setTitle(""+recipse);

            Bundle arguments = new Bundle();
            arguments.putParcelableArrayList(stepsKey,stepsVideoList);
            arguments.putParcelableArrayList(ingredientsKey,ingredientsList);
            arguments.putInt(positionKey,position);


            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.item_detail_container, fragment)
                        .commit();
        }

    }
    public Activity getActivity() {
        Context context = this;
        while (context instanceof ContextWrapper){
            if (context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent i = new Intent(this, ItemListActivity.class);
           // i.putParcelableArrayListExtra("ingredientsList",ItemListActivity.getInstance().getIngList());
            i.putParcelableArrayListExtra("stepsList",stepsVideoList);
           // i.putExtra("recipeName",ItemListActivity.getInstance().getRecipieTitle);
            navigateUpTo(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
