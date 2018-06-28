package com.kpcode4u.prasanthkumar.bakingapp.api;

import com.kpcode4u.prasanthkumar.bakingapp.model.RecipesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Prasanth kumar on 24/06/2018.
 */

public interface Service {
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<RecipesResponse>> getRecipes();
}
