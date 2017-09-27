package com.xianwei.bakingapp.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by xianwei li on 9/27/2017.
 */

public class RecipeUpdateService extends IntentService {

    public static final String ACTION_UPDATE_RECIPE
            = "com.xianwei.bakingapp.widget.action.update_recipe";

    public RecipeUpdateService() {
        super("RecipeUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i("1234", "onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPE.equals(action)) {
                handleActionUpdateRecipeId();
            }
        }
    }

    private void handleActionUpdateRecipeId() {
        SharedPreferences SharedPrefs = getSharedPreferences("recipesPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = SharedPrefs.edit();
        int recipeId = SharedPrefs.getInt("recipeId", 0);
        int recipesSize = SharedPrefs.getInt("recipesSize", 1);

        if (recipeId < recipesSize - 1) {
            recipeId++;
        } else {
            recipeId = 0;
        }
        Log.i("1234", "recipeId" + recipeId);
        editor.putInt("recipeId", recipeId);
        editor.apply();
    }
}
