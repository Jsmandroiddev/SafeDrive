package com.example.jsm.safedrive;

import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.Amitlibs.utils.ComplexPreferences;
import com.example.jsm.safedrive.bean.NearByBean;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NearbyActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    final int GET_ACCESS_FINE_LOCATION_PERMISSION = 0;
    View layout;

    ArrayList<NearByBean>nearByBeenparkinglist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);

        layout = findViewById(R.id.mapNearBy);

        nearByBeenparkinglist = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestACCESS_FINE_LOCATIONPermission();
            return;

        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapNearBy);

            ComplexPreferences preferencesNearby = ComplexPreferences.getComplexPreferences(NearbyActivity.this,Constant.NEARBY_PREF,MODE_PRIVATE);
            Type type = new TypeToken<ArrayList<NearByBean>>() {
            }.getType();
            ArrayList<NearByBean>arraynearBy = preferencesNearby.getArray(Constant.NEARBY_PREF_OBJ,type);

            if (arraynearBy!=null && !arraynearBy.isEmpty())
            {
                nearByBeenparkinglist = arraynearBy;
            }
            mapFragment.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestACCESS_FINE_LOCATIONPermission();
            return;

        }

        Location location = locationManager.getLastKnownLocation(bestProvider);
        mMap.setMyLocationEnabled(true);
        if (location != null) {
            onLocationChanged(location);
        } else
            locationManager.requestLocationUpdates(bestProvider,2000,50,this);

        // Add a marker in Sydney and move the camera
        if (nearByBeenparkinglist!=null && !nearByBeenparkinglist.isEmpty()) {
            for (int i = 0; i <nearByBeenparkinglist.size(); i++)
            {
                LatLng parkinLatlng = new LatLng(nearByBeenparkinglist.get(i).getLat(), nearByBeenparkinglist.get(i).getLng());
                mMap.addMarker(new MarkerOptions().position(parkinLatlng).title(nearByBeenparkinglist.get(i).getParkingAddress()));
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.719568, 75.857727), 13));

    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void requestACCESS_FINE_LOCATIONPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(layout, "Wif Access Permission is Required",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(NearbyActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    GET_ACCESS_FINE_LOCATION_PERMISSION);

                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    GET_ACCESS_FINE_LOCATION_PERMISSION);
        }
    }
}
