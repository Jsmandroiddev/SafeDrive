package com.example.jsm.safedrive;

import android.Manifest;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Amitlibs.utils.ComplexPreferences;
import com.example.jsm.safedrive.bean.AccidentZoneBean;
import com.example.jsm.safedrive.bean.SpeedSafetyFeatureBean;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.jsm.safedrive.R.id.map;

public class TripRecordingActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    Button btnstart, btnstp;
    Polyline line;
    LocationManager locationManager;
    TextToSpeech t1;
    final int GET_ACCESS_FINE_LOCATION_PERMISSION = 0;

    ArrayList<LatLng> points;
    ArrayList<String> tripAddress;
    ArrayList<AccidentZoneBean> accZoneBeenlist;

    Double lat = new Double(0);
    Double lng = new Double(0);
    int flag = 1;
    View layout;
    boolean tripRecordingStarted = false;
    int currentSpeed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_trip_recording);

        layout = findViewById(R.id.trip_recoring_layout);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestACCESS_FINE_LOCATIONPermission();
                return;

            } else {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(map);

                points = new ArrayList<LatLng>();
                tripAddress = new ArrayList<>();
                accZoneBeenlist = new ArrayList<>();


                btnstart = (Button) findViewById(R.id.btnTripstart);
                btnstp = (Button) findViewById(R.id.btnTripstop);

                ComplexPreferences accizonePref = ComplexPreferences.getComplexPreferences(TripRecordingActivity.this, Constant.ACCIDENT_ADD_LIST_PREF, MODE_PRIVATE);
                Type type = new TypeToken<ArrayList<AccidentZoneBean>>() {
                }.getType();
                ArrayList<AccidentZoneBean> arrayAcciZone = accizonePref.getArray(Constant.ACCIDENT_ADD_LIST_OBJ, type);

                if (arrayAcciZone != null && !arrayAcciZone.isEmpty()) {
                    accZoneBeenlist = arrayAcciZone;
                }

                mapFragment.getMapAsync(this);
            }
        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(map);

            points = new ArrayList<LatLng>();
            tripAddress = new ArrayList<>();
            accZoneBeenlist = new ArrayList<>();


            btnstart = (Button) findViewById(R.id.btnTripstart);
            btnstp = (Button) findViewById(R.id.btnTripstop);

            ComplexPreferences accizonePref = ComplexPreferences.getComplexPreferences(TripRecordingActivity.this, Constant.ACCIDENT_ADD_LIST_PREF, MODE_PRIVATE);
            Type type = new TypeToken<ArrayList<AccidentZoneBean>>() {
            }.getType();
            ArrayList<AccidentZoneBean> arrayAcciZone = accizonePref.getArray(Constant.ACCIDENT_ADD_LIST_OBJ, type);

            if (arrayAcciZone != null && !arrayAcciZone.isEmpty()) {
                accZoneBeenlist = arrayAcciZone;
            }
            mapFragment.getMapAsync(this);
        }
    }

    private void requestACCESS_FINE_LOCATIONPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(layout, "Wif Access Permission is Required",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(TripRecordingActivity.this,
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case GET_ACCESS_FINE_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(map);

                    points = new ArrayList<LatLng>();
                    tripAddress = new ArrayList<>();
                    accZoneBeenlist = new ArrayList<>();

                    btnstart = (Button) findViewById(R.id.btnTripstart);
                    btnstp = (Button) findViewById(R.id.btnTripstop);

                    ComplexPreferences accizonePref = ComplexPreferences.getComplexPreferences(TripRecordingActivity.this, Constant.ACCIDENT_ADD_LIST_PREF, MODE_PRIVATE);
                    Type type = new TypeToken<ArrayList<AccidentZoneBean>>() {
                    }.getType();
                    ArrayList<AccidentZoneBean> arrayAcciZone = accizonePref.getArray(Constant.ACCIDENT_ADD_LIST_OBJ, type);

                    if (arrayAcciZone != null && !arrayAcciZone.isEmpty()) {
                        accZoneBeenlist = arrayAcciZone;
                    }

                    mapFragment.getMapAsync(this);
                } else {
                    Toast.makeText(this, "You need to provide the permision in order to use the feature", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.SUCCESS) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        super.onResume();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!accZoneBeenlist.isEmpty() && accZoneBeenlist != null) {
            for (int i = 0; i < accZoneBeenlist.size(); i++) {
                lat = accZoneBeenlist.get(i).getLat();
                lng = accZoneBeenlist.get(i).getLng();

                if (lat != null && lng != null) {
                    final Circle circle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(lat, lng))
                            .radius(150)
                            .strokeColor(Color.RED)
                            .strokeWidth(3)
                            .fillColor(80444444));

                    ValueAnimator valueAnimator = new ValueAnimator();
                    valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
                    valueAnimator.setRepeatMode(ValueAnimator.RESTART);
                    valueAnimator.setIntValues(0, 100);
                    valueAnimator.setDuration(1000);
                    valueAnimator.setEvaluator(new IntEvaluator());
                    valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            float animatedFraction = valueAnimator.getAnimatedFraction();
                            Log.e("", "" + animatedFraction);
                            circle.setRadius(animatedFraction * 200);
                        }
                    });
                    valueAnimator.start();
                }
            }
        }

        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TripRecordingActivity.this, "Trip Recording Started", Toast.LENGTH_SHORT).show();
                //addGeofencesHandler(view);
                points.clear();
                if (!accZoneBeenlist.isEmpty() && accZoneBeenlist != null) {
                    for (int i = 0; i < accZoneBeenlist.size(); i++) {
                        lat = accZoneBeenlist.get(i).getLat();
                        lng = accZoneBeenlist.get(i).getLng();

                        if (lat != null && lng != null) {
                            Circle circle = mMap.addCircle(new CircleOptions()
                                    .center(new LatLng(lat, lng))
                                    .radius(150)
                                    .strokeColor(Color.RED)
                                    .strokeWidth(3)
                                    .fillColor(80444444));
                        }
                    }
                }
                flag = 1;
                tripRecordingStarted = true;
                btnstart.setVisibility(View.GONE);
                btnstp.setVisibility(View.VISIBLE);
                startUpdates();
            }
        });

        btnstp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnstart.setVisibility(View.VISIBLE);
                btnstp.setVisibility(View.INVISIBLE);
                flag = 1;
                tripRecordingStarted = false;
                stopUpdates();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You need to provide the permision in order to use the feature", Toast.LENGTH_SHORT).show();
                this.finish();
                return;
            }
        }
        mMap.setMyLocationEnabled(true);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        } else
            locationManager.requestLocationUpdates(bestProvider, 1000, 5, this);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.719568, 75.857727), 13));

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("TripRecording", "Location Changes Listner called");
        String msg = "New Latitude: " + location.getLatitude()
                + "New Longitude: " + location.getLongitude();
        Double lat1 = location.getLatitude();
        Double lng1 = location.getLongitude();

        boolean locationHasSpeed = location.hasSpeed();

        float speed = location.getSpeed();
        speed = (26 * 18) / 5;
        Log.i("TripRecording", "Speed:- " + locationHasSpeed);
//        locationHasSpeed = true;
//        speed=20;
        if (locationHasSpeed) {
            Log.i("TripRecording", "Speed:- " + speed);
            ((TextView) findViewById(R.id.speedtxt)).setText("Speed:- " + speed);
            currentSpeed = (int) speed;
            AudioManager audio_mngr = (AudioManager) getBaseContext().getSystemService(TripRecordingActivity.this.AUDIO_SERVICE);

            ComplexPreferences mSpeedSafetyPref = ComplexPreferences.getComplexPreferences(TripRecordingActivity.this, Constant.SPEED_SAFETY_FEATURE_PREF, MODE_PRIVATE);
            SpeedSafetyFeatureBean mSpeedSafetyFeatureBean = mSpeedSafetyPref.getObject(Constant.SPEED_SAFETY_FEATURE_PREF_OBJ, SpeedSafetyFeatureBean.class);
            if (mSpeedSafetyFeatureBean != null && mSpeedSafetyFeatureBean.isFeatureEnable()) {
                if (currentSpeed > mSpeedSafetyFeatureBean.getMaxSpeed()) {
                    if (audio_mngr.getMode() != AudioManager.RINGER_MODE_SILENT)
                        new SceilentPhone().execute(mSpeedSafetyFeatureBean.getMaxSpeed());
                } else {
                    if (audio_mngr.getMode() != AudioManager.RINGER_MODE_NORMAL)
                        new SceilentPhone().execute(mSpeedSafetyFeatureBean.getMaxSpeed());
                }
            } else {
                Log.i("TripRecording", "Speed Safety feature is disable");
            }
        }
        if (flag == 1) {
            new NotifyAcciZoneTask().execute(lat1.toString(), lng1.toString());
        }
        Log.i("SAfeDrive", msg + " your current location");

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
        if (tripRecordingStarted) {
            tripAddress.add(getAddress(location));

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            points.add(latLng);
            PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            for (int i = 0; i < points.size(); i++) {
                LatLng point = points.get(i);
                options.add(point);

            }
            line = mMap.addPolyline(options); //add Polyline
        }
    }

    private void addMarker(LatLng latLng) {

        mMap.addMarker(new MarkerOptions().position(latLng));
    }

    private void stopUpdates() {
        Location location = mMap.getMyLocation();
        if (location != null)
            addMarker(new LatLng(location.getLatitude(), location.getLongitude()));


        AlertDialog.Builder builder = new AlertDialog.Builder(TripRecordingActivity.this);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setContentView(R.layout.trip_rec_dialog);

        Button discardbtn = (Button) dialog.findViewById(R.id.trip_rec_discard_btn);
        Button saveRecbtn = (Button) dialog.findViewById(R.id.trip_rec_save_btn);

        saveRecbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ComplexPreferences tripRecPreferences = ComplexPreferences.getComplexPreferences(TripRecordingActivity.this, Constant.TRIP_RECORDING_LIST_PREF, MODE_PRIVATE);
                if (tripAddress != null && tripAddress.size() != 0) {
                    tripRecPreferences.putObject(Constant.ACCIDENT_ADD_LIST_OBJ, tripAddress);
                    tripRecPreferences.commit();
                    Toast.makeText(TripRecordingActivity.this, "Trip Saved Successfully", Toast.LENGTH_SHORT).show();
                    tripAddress.clear();
                }
                if (!points.isEmpty()) {
                    points.clear();
                }

                dialog.dismiss();

            }
        });

        discardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tripAddress != null && tripAddress.size() != 0) {
                    tripAddress.clear();
                }
                if (!points.isEmpty()) {
                    points.clear();
                }
                mMap.clear();
                dialog.dismiss();
            }
        });
        startUpdates();
    }


    private void startUpdates() {
        Location location = mMap.getMyLocation();
        if (location != null)
            addMarker(new LatLng(location.getLatitude(), location.getLongitude()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show();
                this.finish();
                return;
            }
        }
        //locationManager.removeUpdates(TripRecordingActivity.this);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000, 5, this);
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


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public String getAddress(Location location) {
        Geocoder geocoder = new Geocoder(TripRecordingActivity.this, Locale.ENGLISH);
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

    class NotifyAcciZoneTask extends AsyncTask<String, Void, ArrayList<Double>> {
        Double distanceAcciZone = new Double(0);

        @Override
        protected ArrayList<Double> doInBackground(String... strings) {

            Double currLat = Double.parseDouble(strings[0]);
            Double currLng = Double.parseDouble(strings[1]);

            ArrayList<Double> listDistances = new ArrayList<>();

            if (!accZoneBeenlist.isEmpty() && accZoneBeenlist != null) {
                for (int i = 0; i < accZoneBeenlist.size(); i++) {
                    lat = accZoneBeenlist.get(i).getLat();
                    lng = accZoneBeenlist.get(i).getLng();
                    if (lat != null && lng != null) {
                        distanceAcciZone = distance(currLat, currLng, lat, lng);
                        listDistances.add(distanceAcciZone);
                    }
                }
            }
            return listDistances;
        }

        @Override
        protected void onPostExecute(ArrayList<Double> arrayListDistances) {
            super.onPostExecute(arrayListDistances);

            if (arrayListDistances != null && !arrayListDistances.isEmpty()) {

                for (int i = 0; i < arrayListDistances.size(); i++) {
                    Double aDouble1 = arrayListDistances.get(i) * 1000;
                    int finalDistance = aDouble1.intValue();
                    if (finalDistance < 150) {
                        Toast.makeText(TripRecordingActivity.this, "Drive Safely You Are In Accident Zone", Toast.LENGTH_LONG).show();
                        t1.speak("Drive Safely You Are In Accident Zone, 10 accidents recorded in last 2 months", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }

            }
        }
    }

    class SceilentPhone extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... speed) {
            /*try {
                wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            if (currentSpeed > speed[0])
                return true;
            else
                return false;
        }

        @Override
        protected void onPostExecute(Boolean shouldScilentPhone) {
            super.onPostExecute(shouldScilentPhone);
            if (shouldScilentPhone) {
                AudioManager audio_mngr = (AudioManager) getBaseContext().getSystemService(TripRecordingActivity.this.AUDIO_SERVICE);
                audio_mngr.setMode(AudioManager.RINGER_MODE_SILENT);
                audio_mngr.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                Toast.makeText(TripRecordingActivity.this, "Phone stetted to be silent", Toast.LENGTH_SHORT).show();
            } else {
                AudioManager audio_mngr = (AudioManager) getBaseContext().getSystemService(TripRecordingActivity.this.AUDIO_SERVICE);
                audio_mngr.setMode(AudioManager.RINGER_MODE_NORMAL);
                audio_mngr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                Toast.makeText(TripRecordingActivity.this, "Phone stetted to be normal", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onPause() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
}
