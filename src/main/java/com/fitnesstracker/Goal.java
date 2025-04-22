package com.fitnesstracker;

import java.time.LocalDate;

public class Goal {
    private int id;
    private String description;
    private int targetValue;
    private LocalDate deadline;
    private boolean completed;

    public Goal(String description, int targetValue, LocalDate deadline) {
        this.description = description;
        this.targetValue = targetValue;
        this.deadline = deadline;
        this.completed = false;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(int targetValue) {
        this.targetValue = targetValue;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
} 