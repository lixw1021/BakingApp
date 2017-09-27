package com.xianwei.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xianwei.bakingapp.R;
import com.xianwei.bakingapp.model.Recipe;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

        String title = getIngredientsTitle(context);
        views.setTextViewText(R.id.widget_title, title);

        Intent intent = new Intent(context, RecipeWidgetRemoteViewsService.class);
        views.setRemoteAdapter(R.id.widget_list, intent);

        Intent updateRecipeIntent = new Intent(context, RecipeUpdateService.class);
        updateRecipeIntent.setAction(RecipeUpdateService.ACTION_UPDATE_RECIPE);
        PendingIntent pendingIntent = PendingIntent.getService(
                                                                context,
                                                                0,
                                                                updateRecipeIntent,
                                                                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_title, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);
    }

    public String getIngredientsTitle(Context context) {
        SharedPreferences SharedPrefs = context.getSharedPreferences("recipesPref", MODE_PRIVATE);
        int recipeId = SharedPrefs.getInt("recipeId", 0);

        String jsonString = SharedPrefs.getString("recipes", null);
        Type type = new TypeToken<List<Recipe>>() {}.getType();
        Gson gson = new Gson();
        List<Recipe> recipes = gson.fromJson(jsonString , type);

        return recipes.get(recipeId).getName();
    }

}

