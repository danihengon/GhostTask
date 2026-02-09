package com.antigravity.ghosttask.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReminderResponse {
    private UUID id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private boolean completed;
    private double latitude;
    private double longitude;
    private double radiusMeters;
    private boolean notifyOnlyOnce;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRadiusMeters() {
        return radiusMeters;
    }

    public void setRadiusMeters(double radiusMeters) {
        this.radiusMeters = radiusMeters;
    }

    public boolean isNotifyOnlyOnce() {
        return notifyOnlyOnce;
    }

    public void setNotifyOnlyOnce(boolean notifyOnlyOnce) {
        this.notifyOnlyOnce = notifyOnlyOnce;
    }
}
