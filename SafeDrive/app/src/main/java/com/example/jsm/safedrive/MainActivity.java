package com.example.jsm.safedrive;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.jsm.safedrive.databinding.ActivityMainBinding;
import com.example.jsm.safedrive.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new HomeFragment()).commit();

    }

    public ActivityMainBinding getMainBinding()
    {
        return activityMainBinding;
    }

}
