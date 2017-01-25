package com.example.jsm.safedrive;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.Amitlibs.utils.ComplexPreferences;
import com.example.jsm.safedrive.adapters.AccidentZoneAdapter;
import com.example.jsm.safedrive.bean.AccidentZoneBean;
import com.example.jsm.safedrive.bean.ResponceBean;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AccidentZoneActivity extends AppCompatActivity {

    Switch aSwitch;
    ListView listView;
    Button addAddressbutton;
    ArrayList<AccidentZoneBean> accidentZoneBeenlist = new ArrayList<>();
    private static final int REQUEST_SELECT_PLACE = 1000;
    private static final String LOG_TAG = "PlaceSelectionListener";
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    ArrayList<AccidentZoneBean> zoneBeenlist = new ArrayList<>();
    View layout;

    int flag = 0;
    String addressHead;
    final int GET_ACCESS_FINE_LOCATION_PERMISSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_zone);

        aSwitch = (Switch) findViewById(R.id.switch1);
        listView = (ListView) findViewById(R.id.lv_acc_zone);
        addAddressbutton = (Button) findViewById(R.id.btn_add_acc_zone);

        layout = findViewById(R.id.activity_accident_zone);

        ComplexPreferences accidentZonePref2 = ComplexPreferences.getComplexPreferences(AccidentZoneActivity.this, Constant.ACCIDENT_ADD_LIST_PREF, MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<AccidentZoneBean>>() {
        }.getType();
        accidentZoneBeenlist = accidentZonePref2.getArray(Constant.ACCIDENT_ADD_LIST_OBJ, type);

        if (accidentZoneBeenlist != null && !accidentZoneBeenlist.isEmpty()) {
            AccidentZoneAdapter accidentZoneAdapter = new AccidentZoneAdapter(AccidentZoneActivity.this, accidentZoneBeenlist);

            listView.setAdapter(accidentZoneAdapter);
            accidentZoneAdapter.notifyDataSetChanged();
        }

        addAddressbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AccidentZoneActivity.this, SelectAccidentZone.class);
                startActivityForResult(intent, 601);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestACCESS_FINE_LOCATIONPermission();
                return;

            }
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
                            ActivityCompat.requestPermissions(AccidentZoneActivity.this,
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

                } else {
                    Toast.makeText(this, "You need to provide the permision in order to use the feature", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
                break;
        }
    }

    private void showDialog(final AccidentZoneBean accidentZoneBeanObj) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AccidentZoneActivity.this);

        final AlertDialog dialog = builder.create();

        dialog.show();

        dialog.setContentView(R.layout.custom_dialog);
        TextView dialogBody = (TextView) dialog.findViewById(R.id.tv_accZone_diaText);
        dialogBody.setText(accidentZoneBeanObj.getAccZoneAddressHeading().toString());

        TextView title = (TextView) dialog.findViewById(R.id.tv_accZ_Dialog_title);
        title.setText("Do you want to add this Address to Accident Zone");

        Button okBtn = (Button) dialog.findViewById(R.id.btn_accZone_dialog_ok);
        Button cnclBtn = (Button) dialog.findViewById(R.id.btn_accZone_dialog_cancel);

        okBtn.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {

                                         addressHead = accidentZoneBeanObj.getAccZoneAddressHeading().toString();
                                         //Constant.LANDMARKS.put(place.getAddress().toString(),place.getLatLng());
/*
                   AcciZoneNotifyBean hashBean = new AcciZoneNotifyBean(place.getId(),place.getAddress().toString(),place.getLatLng());
                   hashBeenlist.add(hashBean);

                   ComplexPreferences preferencesHash = ComplexPreferences.getComplexPreferences(AccidentZoneActivity.this,Constant.HASH_MAP_PREF,MODE_PRIVATE);
                   preferencesHash.putObject(Constant.HASH_MAP_PREF_OBJ,hashBeenlist);
                   preferencesHash.commit();*/


                                         Double lat = accidentZoneBeanObj.getLat();
                                         Double lng = accidentZoneBeanObj.getLng();
                                         String coord1 = lat.toString();
                                         String coord2 = lng.toString();

                                         new AccidentAddressTask().execute(addressHead, "6 accidents recorded in last 3 months", coord1, coord2);
                                         dialog.dismiss();


                                     }
                                 }
        );

        cnclBtn.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View view) {
                                           dialog.dismiss();
                                       }
                                   }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 601) {

            final AccidentZoneBean accidentZoneBeanObj = (AccidentZoneBean) data.getSerializableExtra("final address");
            showDialog(accidentZoneBeanObj);
        }
    }

   /* @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onPlaceSelected(final Place place) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(AccidentZoneActivity.this);

        final AlertDialog dialog = builder.create();

        dialog.show();

        dialog.setContentView(R.layout.custom_dialog);
        TextView dialogBody = (TextView) dialog.findViewById(R.id.tv_accZone_diaText);
        dialogBody.setText(place.getName().toString() + "," + place.getAddress().toString());

        TextView title = (TextView) dialog.findViewById(R.id.tv_accZ_Dialog_title);
        title.setText("Do you want to add this Address to Accident Zone");

        Button okBtn = (Button) dialog.findViewById(R.id.btn_accZone_dialog_ok);
        Button cnclBtn = (Button) dialog.findViewById(R.id.btn_accZone_dialog_cancel);

        okBtn.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {

                                         Toast.makeText(AccidentZoneActivity.this, place.getName().toString(), Toast.LENGTH_SHORT).show();


                                         addressHead = place.getName().toString();
                                         //Constant.LANDMARKS.put(place.getAddress().toString(),place.getLatLng());
*//*
                AcciZoneNotifyBean hashBean = new AcciZoneNotifyBean(place.getId(),place.getAddress().toString(),place.getLatLng());
                hashBeenlist.add(hashBean);

                ComplexPreferences preferencesHash = ComplexPreferences.getComplexPreferences(AccidentZoneActivity.this,Constant.HASH_MAP_PREF,MODE_PRIVATE);
                preferencesHash.putObject(Constant.HASH_MAP_PREF_OBJ,hashBeenlist);
                preferencesHash.commit();*//*

                                         LatLng latLng = place.getLatLng();
                                         Double lat = latLng.latitude;
                                         Double lng = latLng.longitude;
                                         String coord1 = lat.toString();
                                         String coord2 = lng.toString();

                                         new AccidentAddressTask().execute(addressHead, "6 accidents recorded in last 3 months", coord1, coord2);
                                         dialog.dismiss();


                                     }
                                 }

        );

        cnclBtn.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View view) {
                                           dialog.dismiss();
                                       }
                                   }
        );

    }

    @Override
    public void onError(Status status) {
        Log.e(LOG_TAG, "onError: Status = " + status.toString());
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                this.onError(status);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    class AccidentAddressTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {

           /* List<NameValuePair>nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("accZoneAddressHeading",strings[0]));
            nameValuePairs.add(new BasicNameValuePair("accZoneDetails",strings[1]));*/

            JSONObject jsonObject = getUserStatusDummyData(strings[0], strings[1], strings[2], strings[3]);
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            if (jsonObject != null) {
                try {
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        //AccidentZoneListBean accidentZoneListBean = new Gson().fromJson(jsonObject1.toString(),AccidentZoneListBean.class);
                        Type typeaccidentListBean = new TypeToken<ArrayList<AccidentZoneBean>>() {
                        }.getType();

                        accidentZoneBeenlist = new Gson().fromJson(jsonArray.toString(), typeaccidentListBean);

                        ComplexPreferences accidentZonePref = ComplexPreferences.getComplexPreferences(AccidentZoneActivity.this, Constant.ACCIDENT_ADD_LIST_PREF, MODE_PRIVATE);
                        accidentZonePref.putObject(Constant.ACCIDENT_ADD_LIST_OBJ, accidentZoneBeenlist);
                        accidentZonePref.commit();

                        AccidentZoneAdapter accidentZoneAdapter = new AccidentZoneAdapter(AccidentZoneActivity.this, accidentZoneBeenlist);
                        listView.setAdapter(accidentZoneAdapter);
                        accidentZoneAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(jsonObject);
        }

    }


    private JSONObject getUserStatusDummyData(String address, String addressdetail, String lat, String lng) {

        //        TODO: Response of getUserInfo should be like the sample given below
        //{"errorCode":"","message":"","response":{"userId":"1","userDOB":"11/09/1988","userEmail":"jsmandroiddeveloper@gmail.com","userImage":"http://www.hit4hit.org/img/login/user-icon-6.png","userMobileNo":"1231231231","userName":"Amit"},"status":"Suscess"}*//**//*

        JSONObject jsonObjectFromUrl = null;


        ComplexPreferences accidentZonePref2 = ComplexPreferences.getComplexPreferences(AccidentZoneActivity.this, Constant.ACCIDENT_ADD_LIST_PREF, MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<AccidentZoneBean>>() {
        }.getType();

        ArrayList<AccidentZoneBean> complList = accidentZonePref2.getArray(Constant.ACCIDENT_ADD_LIST_OBJ, type);

        if (complList != null && !complList.isEmpty()) {
            zoneBeenlist = complList;
        }

        Double l1 = Double.parseDouble(lat);
        Double l2 = Double.parseDouble(lng);

        AccidentZoneBean accidentZoneBean = new AccidentZoneBean(address, addressdetail, l1, l2);
        zoneBeenlist.add(accidentZoneBean);

        ResponceBean mResponceBean = new ResponceBean("Success", zoneBeenlist, "", "");

        String response = new Gson().toJson(mResponceBean);
        try {
            jsonObjectFromUrl = new JSONObject(response);
            Log.d("RMS Demo", jsonObjectFromUrl.toString());
        } catch (Throwable t) {
            Log.e("RMS Demo", "Could not parse malformed JSON: \"" + jsonObjectFromUrl + "\"");
        }
        return jsonObjectFromUrl;
    }
}
