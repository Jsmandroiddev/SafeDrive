package com.example.jsm.safedrive.bean;

import java.io.Serializable;

/**
 * Created by JSM on 1/22/2017.
 */

public class NearByBean implements Serializable {

    String parkingAddress;
    Double lat;
    Double lng;

    public NearByBean(Double lat, Double lng, String parkingAddress) {
        this.lat = lat;
        this.lng = lng;
        this.parkingAddress = parkingAddress;
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

    public String getParkingAddress() {
        return parkingAddress;
    }

    public void setParkingAddress(String parkingAddress) {
        this.parkingAddress = parkingAddress;
    }
}
