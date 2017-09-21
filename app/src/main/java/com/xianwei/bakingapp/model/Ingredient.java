package com.xianwei.bakingapp.model;

/**
 * Created by xianwei li on 9/20/2017.
 */

public class Ingredient {
    private String quality;
    private String measure;
    private String ingredient;

    public Ingredient(String quality, String measure, String ingredient) {
        this.quality = quality;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public String getQuality() {
        return quality;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
