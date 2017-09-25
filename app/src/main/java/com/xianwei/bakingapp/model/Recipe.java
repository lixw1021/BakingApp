package com.xianwei.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xianwei li on 9/20/2017.
 */

public class Recipe implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.servingNum);
        dest.writeString(this.imageUrl);
        dest.writeList(this.ingredients);
        dest.writeList(this.steps);
    }

    protected Recipe(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.servingNum = in.readString();
        this.imageUrl = in.readString();
        this.ingredients = new ArrayList<>();
        in.readList(this.ingredients, Ingredient.class.getClassLoader());
        this.steps = new ArrayList<>();
        in.readList(this.steps, Step.class.getClassLoader());
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
