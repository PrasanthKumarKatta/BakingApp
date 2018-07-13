package com.kpcode4u.prasanthkumar.bakingapp.service;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.kpcode4u.prasanthkumar.bakingapp.R;
import com.kpcode4u.prasanthkumar.bakingapp.model.Ingredients;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final int ID_CONSTANT = 0x0101010;


    private Context context;
    private ArrayList<Ingredients> ingredientsList;

    public MyWidgetRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        try{
            try {
                String ingredients = intent.getExtras().get("Ingredentskey").toString();
                this.ingredientsList = convertJsonToIngredientsList(new JSONArray(ingredients),context);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }catch (JsonIOException e){
            e.printStackTrace();
        }

    }

    public static ArrayList<Ingredients> convertJsonToIngredientsList(JSONArray jsonArray,Context context) {

        ArrayList<Ingredients> bakeIngredientsList = new ArrayList<>();
        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Ingredients bakeIngredient = convertJsonToIngredients(jsonObject, context);
                bakeIngredientsList.add(bakeIngredient);
            }
        }catch(Exception e){

        }
        return bakeIngredientsList;

    }



    public static Ingredients convertJsonToIngredients(JSONObject jsonObject,Context context){
        Ingredients ingredients = null;
        try{

            Double quantity = (Double) jsonObject.get("quantity");
            String measure = jsonObject.get("measure").toString();
            String ingredient = jsonObject.get("ingredient").toString();

            ingredients = new Ingredients(quantity, measure, ingredient);


        }catch (Exception e){
            e.printStackTrace();
        }
        return ingredients;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        final long identifyToken = Binder.clearCallingIdentity();
        Binder.restoreCallingIdentity(identifyToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (ingredientsList != null){
            return  ingredientsList.size();
        }

        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ingredientd_row);
        remoteViews.setTextViewText(R.id.textview_ingredients,ingredientsList.get(position).getIngredient());
        remoteViews.setTextViewText(R.id.textview_mesurement, ingredientsList.get(position).getMeasure());
        remoteViews.setTextViewText(R.id.textview_quantity, (String.valueOf(ingredientsList.get(position).getQuantity())));

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return ID_CONSTANT + position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
