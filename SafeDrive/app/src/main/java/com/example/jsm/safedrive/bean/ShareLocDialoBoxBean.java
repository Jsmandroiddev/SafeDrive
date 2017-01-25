package com.example.jsm.safedrive.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by JSM on 1/10/2017.
 */

public class ShareLocDialoBoxBean implements Serializable {

    String contactNumb;
    String contactName;
    Bitmap userImage;
    boolean isChecked;

    public ShareLocDialoBoxBean(String contactName, String contactNumb, boolean isChecked, Bitmap userImage) {
        this.contactName = contactName;
        this.contactNumb = contactNumb;
        this.isChecked = isChecked;
        this.userImage = userImage;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumb() {
        return contactNumb;
    }

    public void setContactNumb(String contactNumb) {
        this.contactNumb = contactNumb;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }
}
