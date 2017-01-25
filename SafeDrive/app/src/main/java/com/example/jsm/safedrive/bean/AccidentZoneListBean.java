package com.example.jsm.safedrive.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by JSM on 1/3/2017.
 */

public class AccidentZoneListBean implements Serializable{

    ArrayList<AccidentZoneBean>accidentZoneBeen;

    public AccidentZoneListBean(ArrayList<AccidentZoneBean> accidentZoneBeen) {
        this.accidentZoneBeen = accidentZoneBeen;
    }

    public ArrayList<AccidentZoneBean> getAccidentZoneBeen() {
        return accidentZoneBeen;
    }

    public void setAccidentZoneBeen(ArrayList<AccidentZoneBean> accidentZoneBeen) {
        this.accidentZoneBeen = accidentZoneBeen;
    }
}
