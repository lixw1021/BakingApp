package com.xianwei.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xianwei.bakingapp.DetailActivity;
import com.xianwei.bakingapp.R;
import com.xianwei.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xianwei li on 9/20/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter <RecipeAdapter.ViewHolder> {
    private static final String RECIPE = "recipe";
    private Context context;
    private List<Recipe> recipes;

    public RecipeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {
        Recipe currentRecipe = recipes.get(position);
        holder.name.setText(currentRecipe.getName());
        holder.servingNum.setText(currentRecipe.getServingNum());
        holder.recipe = currentRecipe;
    }

    @Override
    public int getItemCount() {
        if (recipes == null) return 0;
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_food_name)
        TextView name;
        @BindView(R.id.tv_servings_number)
        TextView servingNum;
        Recipe recipe;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(RECIPE, recipe);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }
}
