package com.example.wildlifemonitoringsystem.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BodyTemprAll {

    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("body_tempr")
    @Expose
    public Double bodyTempr;
    @SerializedName("animal")
    @Expose
    public String animal;
    @SerializedName("__v")
    @Expose
    public Integer v;
    @SerializedName("timestramp")
    @Expose
    public Long timestramp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getBodyTempr() {
        return bodyTempr;
    }

    public void setBodyTempr(Double bodyTempr) {
        this.bodyTempr = bodyTempr;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public Long getTimestramp() {
        return timestramp;
    }

    public void setTimestramp(Long timestramp) {
        this.timestramp = timestramp;
    }
}