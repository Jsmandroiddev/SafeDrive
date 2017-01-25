package com.example.jsm.safedrive;

import android.content.Intent;
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

import com.example.jsm.safedrive.bean.AccidentZoneBean;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.jsm.safedrive.R.id.select_place_map;

public class SelectAccidentZone extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    PlaceAutocompleteFragment placeAutocompleteFragment;
    Button btnAccidentAddress;
    String finalAddress;
    Double lat = new Double(0);
    Double lng = new Double(0);
    final int GET_ACCESS_FINE_LOCATION_PERMISSION = 0;
    View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_accident_zone);
        layout = findViewById(R.id.activity_accident_zone);

        placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        btnAccidentAddress = (Button) findViewById(R.id.btn_select_place);



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestACCESS_FINE_LOCATIONPermission();

            return;

        } else {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(select_place_map);
            mapFragment.getMapAsync(this);

        }

        buildGoogleApiClient();

        btnAccidentAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalAddress != null && !finalAddress.isEmpty() && lat != null && lng != null && lat != 0 && lng != 0) {
                    Toast.makeText(SelectAccidentZone.this, finalAddress + lat + "," + lng, Toast.LENGTH_SHORT).show();
                    AccidentZoneBean accidentZoneBean = new AccidentZoneBean(finalAddress, "2 accident", lat, lng);
                    Intent intent = new Intent();
                    intent.putExtra("final address", accidentZoneBean);
                    setResult(601, intent);
                    finish();
                } else {
                    Toast.makeText(SelectAccidentZone.this, "First Select Location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestACCESS_FINE_LOCATIONPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
        {
            Snackbar.make(layout, "Wif Access Permission is Required",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(SelectAccidentZone.this,
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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(select_place_map);
        mapFragment.getMapAsync(this);

        buildGoogleApiClient();

        return;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        UiSettings uiSettings = mGoogleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestACCESS_FINE_LOCATIONPermission();
                return;
            }
            mGoogleMap.setMyLocationEnabled(true);

        } else {

            mGoogleMap.setMyLocationEnabled(true);
        }

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.719568, 75.857727), 13));

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {


        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        markerOptions.draggable(true);
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestACCESS_FINE_LOCATIONPermission();
            return;
        }
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Toast.makeText(SelectAccidentZone.this, place.getAddress().toString(), Toast.LENGTH_SHORT).show();
                Location selctedLocation = new Location(LocationManager.GPS_PROVIDER);
                selctedLocation.setLatitude(place.getLatLng().latitude);
                selctedLocation.setLongitude(place.getLatLng().longitude);
//                onLocationChanged(selctedLocation);

                LatLng latLng = new LatLng(selctedLocation.getLatitude(), selctedLocation.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                markerOptions.draggable(true);
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

                finalAddress = place.getAddress().toString();
                lat = place.getLatLng().latitude;
                lng = place.getLatLng().longitude;


            }

            @Override
            public void onError(Status status) {

            }
        });

        mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Location selctedLocation = new Location(LocationManager.GPS_PROVIDER);
                selctedLocation.setLatitude(marker.getPosition().latitude);
                selctedLocation.setLongitude(marker.getPosition().longitude);
                onLocationChanged(selctedLocation);

                String address = getAddress(selctedLocation);
                placeAutocompleteFragment.setText(address);
                finalAddress = address;
                lat = selctedLocation.getLatitude();
                lng = selctedLocation.getLongitude();
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    private String getAddress(Location location) {
        Geocoder geocoder = new Geocoder(SelectAccidentZone.this, Locale.ENGLISH);
        String ret = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ");
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
}
