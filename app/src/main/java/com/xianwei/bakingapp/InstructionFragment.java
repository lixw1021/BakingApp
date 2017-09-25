package com.xianwei.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xianwei.bakingapp.adapters.IngredientAdapter;
import com.xianwei.bakingapp.adapters.StepAdapter;
import com.xianwei.bakingapp.model.Ingredient;
import com.xianwei.bakingapp.model.Recipe;
import com.xianwei.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xianwei li on 9/21/2017.
 */

public class InstructionFragment extends Fragment {
    @BindView(R.id.re_ingredients)
    RecyclerView ingredientsRv;
    @BindView(R.id.rv_steps)
    RecyclerView stepsRv;

    private static final String RECIPE = "recipe";
    private Recipe recipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_instruction, container, false);
        ButterKnife.bind(this, rootView);
        ((DetailActivity) getActivity()).getSupportActionBar().show();

        if (recipe != null) {
            setupIngredient(recipe.getIngredients());
            setupStep(recipe.getSteps());
        }
        return rootView;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(RECIPE);
            setupIngredient(recipe.getIngredients());
            setupStep(recipe.getSteps());
        }
    }

    private void setupIngredient(List<Ingredient> ingredients) {
        IngredientAdapter adapter = new IngredientAdapter(ingredients);
        ingredientsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientsRv.setAdapter(adapter);
    }

    private void setupStep(List<Step> steps) {
        StepAdapter adapter = new StepAdapter(steps);
        stepsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        stepsRv.setAdapter(adapter);
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE, recipe);
    }
}
