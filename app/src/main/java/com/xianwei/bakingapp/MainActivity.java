package com.xianwei.bakingapp;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.xianwei.bakingapp.adapters.RecipeAdapter;
import com.xianwei.bakingapp.loaders.RecipeLoader;
import com.xianwei.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Recipe>> {

    @BindView(R.id.layout_main)
    LinearLayout linearLayout;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_recipe)
    RecyclerView recipesRecyclerView;

    private static final String SHARED_PREFERENCE_NAME = "recipesPref";
    private static final String RECIPES_PREFERENCE_KEY = "recipes";
    private static final String RECIPE_ID_PREFERENCE_KEY = "recipeId";
    private static final String RECIPE_SIZE_PREFERENCE_KEY = "recipeSize";
    private static final int GRID_LAYOUT_ITEM_NUM = 2;

    private RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (isConnected()) {
            getSupportLoaderManager().initLoader(0, null, this);
        } else {
            showSnackbar("Please check your internet");
        }
        mRecipeAdapter = new RecipeAdapter(this);
        recipesRecyclerView.setAdapter(mRecipeAdapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recipesRecyclerView.setLayoutManager(new GridLayoutManager(this, GRID_LAYOUT_ITEM_NUM));
        } else {
            recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new RecipeLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        if (data != null) {
            mRecipeAdapter.setRecipes(data);
            saveDataToSharedPreference(data);
        } else {
            showSnackbar("Does't get any data from server");
        }
    }

    // save data in sharedPreference and use in widget
    private void saveDataToSharedPreference(List<Recipe> data) {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String serializedRecipes = gson.toJson(data);
        editor.putString(RECIPES_PREFERENCE_KEY, serializedRecipes);
        editor.putInt(RECIPE_ID_PREFERENCE_KEY, 0);
        editor.putInt(RECIPE_SIZE_PREFERENCE_KEY, data.size());
        editor.apply();
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
        mRecipeAdapter.setRecipes(null);
    }

    public boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public void showSnackbar(String message) {
        Snackbar.make(linearLayout, message, Snackbar.LENGTH_LONG).show();
    }
}
