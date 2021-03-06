package com.xianwei.bakingapp.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xianwei.bakingapp.R;
import com.xianwei.bakingapp.model.Ingredient;
import com.xianwei.bakingapp.model.Recipe;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by xianwei li on 9/26/2017.
 */

/*create a list of ingredient in widget*/
public class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String SHARED_PREFERENCE_NAME = "recipesPref";
    private static final String RECIPES_PREFERENCE_KEY = "recipes";
    private static final String RECIPE_ID_PREFERENCE_KEY = "recipeId";

    private Context context;
    private List<Ingredient> ingredients;

    public RecipeRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

        ingredients = getIngredientsFromPreferences(context);
    }

    public List<Ingredient> getIngredientsFromPreferences(Context context) {
        SharedPreferences SharedPrefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        int recipeId = SharedPrefs.getInt(RECIPE_ID_PREFERENCE_KEY, 0);

        String jsonString = SharedPrefs.getString(RECIPES_PREFERENCE_KEY, null);
        Type type = new TypeToken<List<Recipe>>() {
        }.getType();
        Gson gson = new Gson();
        List<Recipe> recipes = gson.fromJson(jsonString, type);

        return recipes.get(recipeId).getIngredients();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (ingredients == null) return 0;
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (ingredients == null) return null;
        Ingredient item = ingredients.get(position);

        RemoteViews ingredientItemView = new RemoteViews(context.getPackageName(), R.layout.item_ingredient);
        ingredientItemView.setTextViewText(R.id.tv_ingredient_quantity, item.getQuantity());
        ingredientItemView.setTextViewText(R.id.tv_ingredient_unit, item.getMeasure());
        ingredientItemView.setTextViewText(R.id.tv_ingredient_description, item.getIngredient());

        return ingredientItemView;
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
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
