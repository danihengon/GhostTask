package com.antigravity.ghosttask.api.dto;

public class CreateReminderRequest {
    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private double radiusMeters;
    private boolean notifyOnlyOnce;

    // Getters and Setters
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
