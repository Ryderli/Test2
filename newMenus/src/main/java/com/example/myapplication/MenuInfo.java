package com.example.myapplication;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/22 0022.
 */

public class MenuInfo {
    private String image;
    private String name;
    private String imtro;
    private String ingredients;
    private String burden;
    ArrayList<StepInfo> stepInfo;

    public MenuInfo(String image, String name, String imtro, String ingredients, String burden, ArrayList<StepInfo> stepInfo) {
        this.image = image;
        this.name = name;
        this.imtro = imtro;
        this.ingredients = ingredients;
        this.burden = burden;
        this.stepInfo = stepInfo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImtro() {
        return imtro;
    }

    public void setImtro(String imtro) {
        this.imtro = imtro;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getBurden() {
        return burden;
    }

    public void setBurden(String burden) {
        this.burden = burden;
    }

    public ArrayList<StepInfo> getStepInfo() {
        return stepInfo;
    }

    public void setStepInfo(ArrayList<StepInfo> stepInfo) {
        this.stepInfo = stepInfo;
    }
}
