package com.kpcode4u.prasanthkumar.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.kpcode4u.prasanthkumar.bakingapp.adapter.RecipeAdapter;
import com.kpcode4u.prasanthkumar.bakingapp.api.Client;
import com.kpcode4u.prasanthkumar.bakingapp.api.Service;
import com.kpcode4u.prasanthkumar.bakingapp.model.RecipesResponse;
import com.kpcode4u.prasanthkumar.bakingapp.model.Steps;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesMainActivity extends AppCompatActivity {

    private static final String SAVED_LAYOUT_MANAGER = "SavedLayoutManager" ;
    @BindView(R.id.recyclerview_Recipe) RecyclerView recyclerView;
    @BindView(R.id.mainContent_swipeRef)
    SwipeRefreshLayout swipeRefreshLayout;

    private RecipeAdapter recipeAdapter;
    private ArrayList<RecipesResponse> recipesList = new ArrayList<>();
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_main);
        ButterKnife.bind(this);

        initViews();
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

    private void initViews() {

        recipeAdapter = new RecipeAdapter(this,recipesList);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(recipeAdapter);
        recipeAdapter.notifyDataSetChanged();

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews();
                Toast.makeText(RecipesMainActivity.this, "Recipses Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        getRecipses();
    }

    private void getRecipses() {

        try {

            Client client = new Client();
            Service apiService = client.getClient().create(Service.class);

            Call<List<RecipesResponse>> call =apiService.getRecipes();
            call.enqueue(new Callback<List<RecipesResponse>>() {
                @Override
                public void onResponse(Call<List<RecipesResponse>> call, Response<List<RecipesResponse>> response) {
                    //List<RecipesResponse> responseList = response;

                    List<RecipesResponse> recipesList = response.body();
                    recyclerView.setAdapter(new RecipeAdapter(getApplicationContext(), (ArrayList<RecipesResponse>) recipesList));
                    recyclerView.scrollToPosition(position);
                    if (swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<List<RecipesResponse>> call, Throwable t) {

                    Log.d("Error",t.getMessage());
                    Toast.makeText(RecipesMainActivity.this, "Error in Fetching Data", Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Log.d("Error",e.getMessage());
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRecipses();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getRecipses();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        position = ((GridLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        outState.putInt(SAVED_LAYOUT_MANAGER, position);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState != null){
            position = savedInstanceState.getInt(SAVED_LAYOUT_MANAGER);
        }
        super.onRestoreInstanceState(savedInstanceState);

    }
}
