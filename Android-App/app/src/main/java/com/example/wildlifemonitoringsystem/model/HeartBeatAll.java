package com.example.wildlifemonitoringsystem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeartBeatAll {

    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("heart_beat")
    @Expose
    private Double heartBeat;
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

    public Double getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(Double heartBeat) {
        this.heartBeat = heartBeat;
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