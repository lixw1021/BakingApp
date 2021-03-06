package com.xianwei.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
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
    private static final String SHARED_PREFERENCE_NAME = "recipesPref";
    private static final String RECIPES_PREFERENCE_KEY = "recipes";
    private static final String RECIPE_ID_PREFERENCE_KEY = "recipeId";
    private static final String RECIPE_SIZE_PREFERENCE_KEY = "recipeSize";
    private static final String WIDGET_UPDATE_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";

    //receive broadcast to update recipe
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), RecipeWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        updateRecipeId(context);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void updateRecipeId(Context context) {
        SharedPreferences SharedPrefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = SharedPrefs.edit();
        int recipeId = SharedPrefs.getInt(RECIPE_ID_PREFERENCE_KEY, 0);
        int recipesSize = SharedPrefs.getInt(RECIPE_SIZE_PREFERENCE_KEY, 1);

        if (recipeId < recipesSize - 1) {
            recipeId++;
        } else {
            recipeId = 0;
        }
        editor.putInt(RECIPE_ID_PREFERENCE_KEY, recipeId);
        editor.apply();
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

        // broadcast to update widget
        Intent updateRecipeIntent = new Intent();
        updateRecipeIntent.setAction(WIDGET_UPDATE_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
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
        SharedPreferences SharedPrefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        int recipeId = SharedPrefs.getInt(RECIPE_ID_PREFERENCE_KEY, 0);

        String jsonString = SharedPrefs.getString(RECIPES_PREFERENCE_KEY, null);
        Type type = new TypeToken<List<Recipe>>() {}.getType();
        Gson gson = new Gson();
        List<Recipe> recipes = gson.fromJson(jsonString , type);

        return recipes.get(recipeId).getName();
    }

}

