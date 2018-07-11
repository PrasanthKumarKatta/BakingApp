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

    private String SELECTED_RECIPSE_key = "selectedRecipe";

    @BindView(R.id.ingredients_textView) TextView ingredientTextView;
//    @BindView(R.id.recyclerView_Steps) RecyclerView recyclerView;

    private StepsAdapter stepsAdapter;
    private ArrayList<RecipesResponse> recipe;
    private ArrayList<Steps> stepsList;
    private ArrayList<Ingredients> ingredientsList;
    private int position;
    String recipseIngredients;
    private String recipeName;
    private String ingredient_Name,ingredients_Measure,ingredients_Quantity;


    private boolean mTwoPane;
    Toolbar toolbar;
    private ItemListActivity context;
    @BindView(R.id.item_list) View recyclerView;


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
        setupRecyclerView((RecyclerView) recyclerView);
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

            recipeName = bundle.getString("titlekey");
           // setTitle(recipeName);
            toolbar.setTitle("");


            stepsList = bundle.getParcelableArrayList("StepsKey");

            String step = stepsList.get(0).getShortDescription();
            Toast.makeText(this, "steps"+step, Toast.LENGTH_SHORT).show();

            ingredientsList = bundle.getParcelableArrayList("IngredientsKey");


            ingredientTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //    ingredientsList = new ArrayList<>();
                  //  Intent intent = new Intent(ItemListActivity.this,IngredientsListActivity.class);
                    Intent intent = new Intent(ItemListActivity.this,ItemDetailActivity.class);

                    //  Bundle bundle = new Bundle();
                    intent.putParcelableArrayListExtra("IngredientsKey",ingredientsList);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    
                    
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelableArrayList("stepsList", stepsList);
                        arguments.putInt("position",position);
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
                        i.putExtra("position",position);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        Toast.makeText(context, "StepsAdapter to ExoPlayer", Toast.LENGTH_SHORT).show();
                    }
                    
                }
            });

           // initViews();
        }

    }
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new StepsAdapter(ItemListActivity.this, stepsList, mTwoPane));
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
        setupRecyclerView((RecyclerView) recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRecipseSteps();
        setupRecyclerView((RecyclerView) recyclerView);
    }

    /* public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }*/
}
