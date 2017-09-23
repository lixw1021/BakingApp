package com.xianwei.bakingapp.Utils;


import com.xianwei.bakingapp.model.Ingredient;
import com.xianwei.bakingapp.model.Recipe;
import com.xianwei.bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xianwei li on 9/20/2017.
 */

public class QueryUtils {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String SERVINGS = "servings";
    private static final String IMAGE = "image";
    private static final String INGREDIENTS = "ingredients";
    private static final String STEPS = "steps";
    private static final String QUANTITY = "quantity";
    private static final String MEASURE = "measure";
    private static final String INGREDIENT = "ingredient";
    private static final String SHORT_DESCRIPTION = "shortDescription";
    private static final String DESCRIPTION = "description";
    private static final String VIDEO_URL = "videoURL";
    private static final String THUMBNAIL_URL = "thumbnailURL";


    public static List<Recipe> fetchDataFromServer(String url) {
        String jsonResponse = getJsonResponse(url);
        return parseJson(jsonResponse);
    }

    private static List<Recipe> parseJson(String jsonResponse) {

        if (jsonResponse == null) return null;
        List<Recipe> recipes = new ArrayList<>();
        List<Ingredient> ingredients = null;
        List<Step> steps = null;

        try {
            JSONArray response = new JSONArray(jsonResponse);
            for (int i = 0; i < response.length(); i++) {
                JSONObject item = response.getJSONObject(i);

                String recipeId = item.optString(ID);
                String recipeName = item.optString(NAME);
                String servingNum = item.optString(SERVINGS );
                String recipeImageUrl = item.optString(IMAGE );

                JSONArray ingredientJsonArray;
                if (item.has(INGREDIENTS)) {
                    ingredients = new ArrayList<>();
                    ingredientJsonArray = item.getJSONArray(INGREDIENTS);
                    for (int j = 0; j < ingredientJsonArray.length(); j++) {
                        JSONObject ingredientObj = ingredientJsonArray.getJSONObject(j);
                        String quantity = ingredientObj.optString(QUANTITY);
                        String measure = ingredientObj.optString(MEASURE);
                        String ingredient = ingredientObj.optString(INGREDIENT);
                        ingredients.add(new Ingredient(quantity, measure, ingredient));
                    }
                }

                JSONArray stepJsonArray;
                if (item.has(STEPS)) {
                    steps = new ArrayList<>();
                    stepJsonArray = item.getJSONArray(STEPS);
                    for (int k = 0; k < stepJsonArray.length(); k++) {
                        JSONObject stepObj = stepJsonArray.getJSONObject(k);
                        String id = stepObj.optString(ID);
                        String shortDescription = stepObj.optString(SHORT_DESCRIPTION);
                        String description = stepObj.optString(DESCRIPTION);
                        String videoUrl = stepObj.optString(VIDEO_URL);
                        String thumbnailUrl = stepObj.optString(THUMBNAIL_URL);
                        steps.add(new Step(
                                id,
                                shortDescription,
                                description,
                                videoUrl,
                                thumbnailUrl));
                    }
                }
                recipes.add(new Recipe(
                        recipeId,
                        recipeName,
                        servingNum,
                        recipeImageUrl,
                        ingredients,
                        steps));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return recipes;
    }

    private static String getJsonResponse(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response;
        String jsonString = null;
        try {
            response = client.newCall(request).execute();
            if (response.body() != null) {
                jsonString = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }
}
