package com.example.jsm.safedrive.bean;

/**
 * Created by Amit Gupta on 21-01-2017.
 */

public class SpeedSafetyFeatureBean {
    boolean isFeatureEnable = false;
    int maxSpeed = 0;

    public boolean isFeatureEnable() {
        return isFeatureEnable;
    }

    public void setFeatureEnable(boolean featureEnable) {
        isFeatureEnable = featureEnable;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
}
