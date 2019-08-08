package com.example.wildlifemonitoringsystem.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeoLocationAll {

    @SerializedName("geo_location")
    @Expose
    public List<Double> geoLocation = null;
    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("animal")
    @Expose
    public String animal;
    @SerializedName("__v")
    @Expose
    public Integer v;
    @SerializedName("timestramp")
    @Expose
    public Long timestramp;

    public List<Double> getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(List<Double> geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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