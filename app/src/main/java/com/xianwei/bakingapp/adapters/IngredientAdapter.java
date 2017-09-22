package com.xianwei.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xianwei.bakingapp.R;
import com.xianwei.bakingapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xianwei li on 9/22/2017.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    List<Ingredient> ingredients;

    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootVIew = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);

        return new ViewHolder(rootVIew);
    }

    @Override
    public void onBindViewHolder(IngredientAdapter.ViewHolder holder, int position) {
        Ingredient currentItem = ingredients.get(position);
        holder.quantity.setText(currentItem.getQuantity());
        holder.unit.setText(currentItem.getMeasure());
        holder.description.setText(currentItem.getIngredient());
    }

    @Override
    public int getItemCount() {
        if (ingredients == null) return 0;
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredient_quantity)
        TextView quantity;
        @BindView(R.id.tv_ingredient_unit)
        TextView unit;
        @BindView(R.id.tv_ingredient_description)
        TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
