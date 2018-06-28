package com.kpcode4u.prasanthkumar.bakingapp.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Prasanth kumar on 24/06/2018.
 */

public class RecipesResponse implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("ingredients")
    @Expose
    private ArrayList<Ingredients> ingredients;

    @SerializedName("steps")
    @Expose
    private  ArrayList<Steps> steps;

    @SerializedName("servings")
    @Expose
    private Integer servings;

    public RecipesResponse(Integer id, String name, ArrayList<Ingredients> ingredients, ArrayList<Steps> steps, Integer servings) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
    }

    protected RecipesResponse(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Steps> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Steps> steps) {
        this.steps = steps;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ingredientsList",ingredients);
        bundle.putParcelableArrayList("stepsList",steps);
        dest.writeBundle(bundle);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RecipesResponse> CREATOR = new Parcelable.Creator<RecipesResponse>(){

        @Override
        public RecipesResponse createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public RecipesResponse[] newArray(int size) {
            return new RecipesResponse[0];
        }
    };
}
