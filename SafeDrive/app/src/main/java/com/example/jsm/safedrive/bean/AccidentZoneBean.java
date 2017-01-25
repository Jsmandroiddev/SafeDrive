package com.example.jsm.safedrive.bean;

import java.io.Serializable;

/**
 * Created by JSM on 12/29/2016.
 */

public class AccidentZoneBean implements Serializable{

    String accZoneAddressHeading;
    String accZoneDetails;
    Double lat;
    Double lng;

    public AccidentZoneBean(String accZoneAddressHeading, String accZoneDetails, Double lat, Double lng) {
        this.accZoneAddressHeading = accZoneAddressHeading;
        this.accZoneDetails = accZoneDetails;
        this.lat = lat;
        this.lng = lng;
    }

    public String getAccZoneAddressHeading() {
        return accZoneAddressHeading;
    }

    public void setAccZoneAddressHeading(String accZoneAddressHeading) {
        this.accZoneAddressHeading = accZoneAddressHeading;
    }

    public String getAccZoneDetails() {
        return accZoneDetails;
    }

    public void setAccZoneDetails(String accZoneDetails) {
        this.accZoneDetails = accZoneDetails;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
