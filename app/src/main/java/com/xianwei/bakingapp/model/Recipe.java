package com.xianwei.bakingapp.model;

import java.util.List;

/**
 * Created by xianwei li on 9/20/2017.
 */

public class Recipe {
    private String id;
    private String name;
    private String servingNum;
    private String imageUrl;
    private List<Ingredient> ingredients;
    private List<Step> steps;

    public Recipe(String id, String name, String servingNum, String imageUrl,
                  List<Ingredient> ingredients, List<Step> steps) {
        this.id = id;
        this.name = name;
        this.servingNum = servingNum;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getServingNum() {
        return servingNum;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServingNum(String servingNum) {
        this.servingNum = servingNum;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
