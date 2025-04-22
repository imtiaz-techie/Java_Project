package com.fitnesstracker;

import java.time.LocalDate;

public class Workout {
    private int id;
    private String type;
    private int duration;
    private String intensity;
    private int caloriesBurned;
    private LocalDate date;

    public Workout(String type, int duration, String intensity, int caloriesBurned, LocalDate date) {
        this.type = type;
        this.duration = duration;
        this.intensity = intensity;
        this.caloriesBurned = caloriesBurned;
        this.date = date;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
} 