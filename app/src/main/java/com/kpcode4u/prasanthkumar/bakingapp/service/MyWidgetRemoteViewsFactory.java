package com.kpcode4u.prasanthkumar.bakingapp.service;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.JsonIOException;
import com.kpcode4u.prasanthkumar.bakingapp.R;
import com.kpcode4u.prasanthkumar.bakingapp.model.Ingredients;


import java.util.ArrayList;

public class MyWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private ArrayList<Ingredients> ingredientsList;

    public MyWidgetRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        try{
            this.ingredientsList = intent.getParcelableArrayListExtra("ingredientslist");
        }catch (JsonIOException e){
            e.printStackTrace();
        }

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
        remoteViews.setTextViewText(R.id.textview_quantity, ingredientsList.get(position).getQuantity());

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
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
