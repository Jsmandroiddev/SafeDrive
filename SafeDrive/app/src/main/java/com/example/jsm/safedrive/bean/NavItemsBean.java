package com.example.jsm.safedrive.bean;

import android.databinding.BaseObservable;
import android.graphics.drawable.Drawable;

/**
 * Created by JSM on 12/17/2016.
 */

public class NavItemsBean extends BaseObservable {

    String navRowTitle;
    Drawable image;

    public NavItemsBean(Drawable image, String navRowTitle) {
        this.image = image;
        this.navRowTitle = navRowTitle;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getNavRowTitle() {
        return navRowTitle;
    }

    public void setNavRowTitle(String navRowTitle) {
        this.navRowTitle = navRowTitle;
    }
}
