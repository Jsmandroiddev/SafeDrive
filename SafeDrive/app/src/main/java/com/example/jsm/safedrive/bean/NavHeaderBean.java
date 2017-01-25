package com.example.jsm.safedrive.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;


/**
 * Created by JSM on 12/17/2016.
 */

public class NavHeaderBean extends BaseObservable {

    String appName;

    public NavHeaderBean(String appName) {
        this.appName = appName;
    }

    @Bindable
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
