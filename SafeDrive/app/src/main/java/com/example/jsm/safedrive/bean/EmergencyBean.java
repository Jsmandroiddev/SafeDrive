package com.example.jsm.safedrive.bean;

import java.io.Serializable;

/**
 * Created by JSM on 1/20/2017.
 */

public class EmergencyBean implements Serializable {

    String emeConName;
    String emeConNumber;

    public EmergencyBean(String emeConName, String emeConNumber) {
        this.emeConName = emeConName;
        this.emeConNumber = emeConNumber;
    }

    public String getEmeConName() {
        return emeConName;
    }

    public void setEmeConName(String emeConName) {
        this.emeConName = emeConName;
    }

    public String getEmeConNumber() {
        return emeConNumber;
    }

    public void setEmeConNumber(String emeConNumber) {
        this.emeConNumber = emeConNumber;
    }
}
