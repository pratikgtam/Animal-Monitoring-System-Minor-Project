package com.example.wildlifemonitoringsystem.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllAnimals {

    @SerializedName("geo_location")
    @Expose
    private List<Double> geoLocation = null;
    @SerializedName("heart_beat")
    @Expose
    private Double heartBeat;
    @SerializedName("heart_beat_range")
    @Expose
    private List<Double> heartBeatRange = null;
    @SerializedName("body_tempr")
    @Expose
    private Double bodyTempr;
    @SerializedName("body_tempr_range")
    @Expose
    private List<Double> bodyTemprRange = null;
    @SerializedName("surr_tempr")
    @Expose
    private Double surrTempr;
    @SerializedName("heart_beat_all")
    @Expose
    private List<String> heartBeatAll = null;
    @SerializedName("body_tempr_all")
    @Expose
    private List<String> bodyTemprAll = null;
    @SerializedName("surr_tempr_all")
    @Expose
    private List<String> surrTemprAll = null;
    @SerializedName("geo_location_all")
    @Expose
    private List<String> geoLocationAll = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("animal_id")
    @Expose
    private String animalId;
    @SerializedName("image_src")
    @Expose
    private String imageSrc;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("timestramp")
    @Expose
    private Long timestramp;

    private Double humidity;

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public List<Double> getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(List<Double> geoLocation) {
        this.geoLocation = geoLocation;
    }

    public Double getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(Double heartBeat) {
        this.heartBeat = heartBeat;
    }

    public List<Double> getHeartBeatRange() {
        return heartBeatRange;
    }

    public void setHeartBeatRange(List<Double> heartBeatRange) {
        this.heartBeatRange = heartBeatRange;
    }

    public Double getBodyTempr() {
        return bodyTempr;
    }

    public void setBodyTempr(Double bodyTempr) {
        this.bodyTempr = bodyTempr;
    }

    public List<Double> getBodyTemprRange() {
        return bodyTemprRange;
    }

    public void setBodyTemprRange(List<Double> bodyTemprRange) {
        this.bodyTemprRange = bodyTemprRange;
    }

    public Double getSurrTempr() {
        return surrTempr;
    }

    public void setSurrTempr(Double surrTempr) {
        this.surrTempr = surrTempr;
    }

    public List<String> getHeartBeatAll() {
        return heartBeatAll;
    }

    public void setHeartBeatAll(List<String> heartBeatAll) {
        this.heartBeatAll = heartBeatAll;
    }

    public List<String> getBodyTemprAll() {
        return bodyTemprAll;
    }

    public void setBodyTemprAll(List<String> bodyTemprAll) {
        this.bodyTemprAll = bodyTemprAll;
    }

    public List<String> getSurrTemprAll() {
        return surrTemprAll;
    }

    public void setSurrTemprAll(List<String> surrTemprAll) {
        this.surrTemprAll = surrTemprAll;
    }

    public List<String> getGeoLocationAll() {
        return geoLocationAll;
    }

    public void setGeoLocationAll(List<String> geoLocationAll) {
        this.geoLocationAll = geoLocationAll;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
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

