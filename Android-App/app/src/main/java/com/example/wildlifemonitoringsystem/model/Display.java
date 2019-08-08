package com.example.wildlifemonitoringsystem.model;

public class Display {
    private Double value;
    private Long timestramp;

    public Display(Double value, Long timestramp) {

        this.value = value;
        this.timestramp = timestramp;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getTimestramp() {
        return timestramp;
    }

    public void setTimestramp(Long timestramp) {
        this.timestramp = timestramp;
    }
}
