package com.kpcode4u.prasanthkumar.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.kpcode4u.prasanthkumar.bakingapp.model.Ingredients;
import com.kpcode4u.prasanthkumar.bakingapp.service.MyWidgetRemoteViewService;

import java.util.ArrayList;

public class BakingWidget extends AppWidgetProvider {

    ArrayList<Ingredients> ingredientsList;
    String recipe;

    private static final  String ACTION_CLICK = "com.kpcode4u.prasanthkumar.bakingapp.BAKING";
    private static final String ingredientsKey = "ingredientsKey";
    private static final String recipeNameKey = "recipeNameKey";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        //Get all ids
        ComponentName thisWidget = new ComponentName(context,BakingWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds){
            updateAppWidget(context, appWidgetManager, widgetId);
        }
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int widgetId) {

        //Create some random data
        // int number = (new Random().nextInt(100));
        CharSequence widgetText = context.getString(R.string.app_name);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.baking_app_widget);
        Log.w("widgetExample",String.valueOf(widgetText));

        //Set the text
        remoteViews.setTextViewText(R.id.appwidget_text,String.valueOf(widgetText));

        //Register an onClickListener
        Intent intent = new Intent(context, MyWidgetRemoteViewService.class);

        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        intent.putExtra(ingredientsKey, (new Gson().toJson(ingredientsList)));
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.appwidget_ingredients, intent);
        remoteViews.setTextViewText(R.id.appwidget_text, recipe);

        /*
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
*/

        appWidgetManager.updateAppWidget(widgetId, remoteViews);

    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try{

            ingredientsList = intent.getParcelableArrayListExtra(ingredientsKey);
            recipe = intent.getStringExtra(recipeNameKey);

            //recipe = bundle.getString("recipe");
            if (ingredientsList != null){

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisWidget = new ComponentName(context.getPackageName(),BakingWidget.class.getName());
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

                onUpdate(context, appWidgetManager, appWidgetIds);
            }
            super.onReceive(context, intent);

        }catch (Exception e){

        }

    }
}
