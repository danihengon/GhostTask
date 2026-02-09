package com.antigravity.ghosttask.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reminder {

    private final UUID id;
    private String title;
    private String description;
    private final LocalDateTime createdAt;
    private boolean completed;

    // Location targeting
    private double latitude;
    private double longitude;
    private double radiusMeters;

    // Trigger settings
    private boolean notifyOnlyOnce;
    private LocalDateTime lastTriggeredAt;
    private int timesTriggered;

    public Reminder(String title, String description, double latitude, double longitude, double radiusMeters, boolean notifyOnlyOnce) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.completed = false;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radiusMeters = radiusMeters;
        this.notifyOnlyOnce = notifyOnlyOnce;
        this.timesTriggered = 0;
    }

    // Getters
    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isCompleted() { return completed; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getRadiusMeters() { return radiusMeters; }
    public boolean isNotifyOnlyOnce() { return notifyOnlyOnce; }
    public LocalDateTime getLastTriggeredAt() { return lastTriggeredAt; }
    public int getTimesTriggered() { return timesTriggered; }

    // Setters for mutable fields
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setRadiusMeters(double radiusMeters) { this.radiusMeters = radiusMeters; }
    public void setNotifyOnlyOnce(boolean notifyOnlyOnce) { this.notifyOnlyOnce = notifyOnlyOnce; }

    // Domain methods

    public void markCompleted() {
        this.completed = true;
    }

    public void registerTrigger() {
        this.lastTriggeredAt = LocalDateTime.now();
        this.timesTriggered++;
        
        if (this.notifyOnlyOnce) {
            this.markCompleted();
        }
    }
}
