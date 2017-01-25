package com.example.jsm.safedrive;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jsm.safedrive.MyDialogFragment.onSubmitListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.jsm.safedrive.R.id.shareloc_map;

public class ShareLocationActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, onSubmitListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    Button btnShareLoc;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Location mLastLocation;

    final int GET_ACCESS_FINE_LOCATION_PERMISSION = 0;
    View layout;

    public static String sharedAddress = "off";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_share_location);

        layout = findViewById(R.id.shareloc_layout);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestACCESS_FINE_LOCATIONPermission();

                return;

            } else {

                btnShareLoc = (Button) findViewById(R.id.btn_share_mylocation);
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(shareloc_map);
                mapFragment.getMapAsync(this);

                btnShareLoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyDialogFragment fragment1 = new MyDialogFragment();
                        fragment1.mListener = ShareLocationActivity.this;
                        fragment1.show(getFragmentManager(), "");
                        //Toast.makeText(ShareLocationActivity.this,sharedAddress,Toast.LENGTH_SHORT).show();
                    }
                });

                buildGoogleApiClient();

            }

        } else {
            btnShareLoc = (Button) findViewById(R.id.btn_share_mylocation);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(shareloc_map);
            mapFragment.getMapAsync(this);

            btnShareLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyDialogFragment fragment1 = new MyDialogFragment();
                    fragment1.mListener = ShareLocationActivity.this;
                    fragment1.show(getFragmentManager(), "");
                    //Toast.makeText(ShareLocationActivity.this,sharedAddress,Toast.LENGTH_SHORT).show();
                }
            });

            buildGoogleApiClient();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(ShareLocationActivity.this,"please turn on your GPS",Toast.LENGTH_SHORT).show();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestACCESS_FINE_LOCATIONPermission();
                return;
            }
        }

        mMap.setMyLocationEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.719568, 75.857727), 13));

        MyDialogFragment fragment1 = new MyDialogFragment();
        fragment1.mListener = ShareLocationActivity.this;
        fragment1.show(getFragmentManager(), "");

    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void requestACCESS_FINE_LOCATIONPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(layout, "Wif Access Permission is Required",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(ShareLocationActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    GET_ACCESS_FINE_LOCATION_PERMISSION);

                        }
                    }).show();


        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    GET_ACCESS_FINE_LOCATION_PERMISSION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case GET_ACCESS_FINE_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    btnShareLoc = (Button) findViewById(R.id.btn_share_mylocation);
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(shareloc_map);
                    mapFragment.getMapAsync(this);
                    btnShareLoc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MyDialogFragment fragment1 = new MyDialogFragment();
                            fragment1.mListener = ShareLocationActivity.this;
                            fragment1.show(getFragmentManager(), "");
                            //Toast.makeText(ShareLocationActivity.this,sharedAddress,Toast.LENGTH_SHORT).show();
                        }
                    });
                    buildGoogleApiClient();

                    return;
                }else {
                    Toast.makeText(this, "You need to provide the permision in order to use the feature", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(final Location location) {

        String msg = "New Latitude: " + location.getLatitude()
                + "New Longitude: " + location.getLongitude();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient!=null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient!=null) {
            mGoogleApiClient.disconnect();
        }
    }

    public String getAddress(Location location) {
        Geocoder geocoder = new Geocoder(ShareLocationActivity.this, Locale.ENGLISH);
        String ret = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                ret = strReturnedAddress.toString();
            } else {
                ret = "No Address returned!";
            }
        } catch (IOException e) {
            e.printStackTrace();
            ret = "Can't get Address!";
        }
        return ret;
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000); // Update location every second

        if (mGoogleApiClient!=null) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestACCESS_FINE_LOCATIONPermission();
                return;
            }

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                sharedAddress = getAddress(mLastLocation);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void checkBoxStatus(boolean status) {

    }

}


