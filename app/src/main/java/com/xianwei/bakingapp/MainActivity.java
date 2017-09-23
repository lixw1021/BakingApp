package com.xianwei.bakingapp;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.xianwei.bakingapp.adapters.RecipeAdapter;
import com.xianwei.bakingapp.loaders.RecipeLoader;
import com.xianwei.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Recipe>> {

    @BindView(R.id.rv_recipe)
    RecyclerView recipesRecyclerView;

    private RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportLoaderManager().initLoader(0, null, this);
        mRecipeAdapter = new RecipeAdapter(this);
        recipesRecyclerView.setAdapter(mRecipeAdapter);
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        Log.i("1234", "onCreateLoader");
        return new RecipeLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        Log.i("1234", "onLoadFinished");
        mRecipeAdapter.setRecipes(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
        mRecipeAdapter.setRecipes(null);
    }
}
