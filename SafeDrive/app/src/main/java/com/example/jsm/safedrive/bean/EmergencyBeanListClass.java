package com.example.jsm.safedrive.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by JSM on 1/23/2017.
 */

public class EmergencyBeanListClass implements Serializable {

    ArrayList<EmergencyBean>arrayListEmer;
    String emerMsg;

    public EmergencyBeanListClass(ArrayList<EmergencyBean> arrayListEmer, String emerMsg) {
        this.arrayListEmer = arrayListEmer;
        this.emerMsg = emerMsg;
    }

    public ArrayList<EmergencyBean> getArrayListEmer() {
        return arrayListEmer;
    }

    public void setArrayListEmer(ArrayList<EmergencyBean> arrayListEmer) {
        this.arrayListEmer = arrayListEmer;
    }

    public String getEmerMsg() {
        return emerMsg;
    }

    public void setEmerMsg(String emerMsg) {
        this.emerMsg = emerMsg;
    }
}
