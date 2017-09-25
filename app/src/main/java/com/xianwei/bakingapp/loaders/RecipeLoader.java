package com.xianwei.bakingapp.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.xianwei.bakingapp.Utils.QueryUtils;
import com.xianwei.bakingapp.model.Recipe;

import java.util.List;

/**
 * Created by xianwei li on 9/20/2017.
 */

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {
    private static final String URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private List<Recipe> cache;

    public RecipeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (cache == null) {
            forceLoad();
        }
    }

    @Override
    public List<Recipe> loadInBackground() {
        return QueryUtils.fetchDataFromServer(URL);
    }

    @Override
    public void deliverResult(List<Recipe> data) {
        super.deliverResult(data);
        cache = data;
    }
}
