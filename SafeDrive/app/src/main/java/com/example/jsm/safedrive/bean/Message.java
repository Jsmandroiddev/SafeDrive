package com.example.jsm.safedrive.bean;

import java.io.Serializable;

/**
 * Created by JSM on 1/22/2017.
 */

public class Message implements Serializable {

    String mesag;

    public Message(String mesag) {
        this.mesag = mesag;
    }

    public String getMesag() {
        return mesag;
    }

    public void setMesag(String mesag) {
        this.mesag = mesag;
    }
}
