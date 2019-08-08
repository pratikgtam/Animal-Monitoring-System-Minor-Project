package com.example.wildlifemonitoringsystem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SurrTemprAll {

    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("surr_tempr")
    @Expose
    public Double surrTempr;
    @SerializedName("animal")
    @Expose
    public String animal;
    @SerializedName("__v")
    @Expose
    public Integer v;
    @SerializedName("timestramp")
    @Expose
    public Long timestramp;

    private Double humidity;

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getSurrTempr() {
        return surrTempr;
    }

    public void setSurrTempr(Double surrTempr) {
        this.surrTempr = surrTempr;
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