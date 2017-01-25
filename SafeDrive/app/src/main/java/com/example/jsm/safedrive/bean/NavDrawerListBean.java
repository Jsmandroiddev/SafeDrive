package com.example.jsm.safedrive.bean;

import android.databinding.BaseObservable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JSM on 12/27/2016.
 */

public class NavDrawerListBean extends BaseObservable {

    public List<NavHeaderBean>headerBeenlist = new ArrayList<>();

    public NavDrawerListBean(List<NavHeaderBean> headerBeenlist) {
        this.headerBeenlist = headerBeenlist;
    }
}
