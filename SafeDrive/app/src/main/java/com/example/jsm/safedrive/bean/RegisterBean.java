package com.example.jsm.safedrive.bean;

import android.databinding.BaseObservable;

import java.io.Serializable;

/**
 * Created by JSM on 12/24/2016.
 */

public class RegisterBean extends BaseObservable implements Serializable
{
    String uid;
    String uname;
    String uemail;
    String upass;
    String umobile;

    public RegisterBean(String uemail, String uid, String umobile, String uname, String upass) {
        this.uemail = uemail;
        this.uid = uid;
        this.umobile = umobile;
        this.uname = uname;
        this.upass = upass;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getuid() {
        return uid;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }

    public String getUmobile() {
        return umobile;
    }

    public void setUmobile(String umobile) {
        this.umobile = umobile;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpass() {
        return upass;
    }

    public void setUpass(String upass) {
        this.upass = upass;
    }
}
