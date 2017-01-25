package com.example.jsm.safedrive;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.example.jsm.safedrive.bean.EmergencyBean;
import com.example.jsm.safedrive.bean.RegisterBean;
import com.example.jsm.safedrive.databinding.ActivityLoginBinding;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding activityLoginBinding;
    final int PERMISSION_REQUEST_CONTACT = 0;
    ArrayList<EmergencyBean> emergencyBeenlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityLoginBinding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        emergencyBeenlist = new ArrayList<>();


        askForContactPermission();


        ComplexPreferences userRegisPref = ComplexPreferences.getComplexPreferences(LoginActivity.this, Constant.REGIS_PREFRENCE, MODE_PRIVATE);
        RegisterBean registerBean = userRegisPref.getObject(Constant.REGISTER_PREFRENCE_OBJ, RegisterBean.class);

        if (registerBean != null) {

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
            finish();

        }
        activityLoginBinding.tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);*/

                String logmail = activityLoginBinding.edLogusername.getText().toString().trim();
                String logpass = activityLoginBinding.edLoguserpass.getText().toString().trim();

                TextInputLayout tiluemail = activityLoginBinding.tilLoguname;
                TextInputLayout tilupass = activityLoginBinding.tilLogupass;

                if (logmail.isEmpty()) {
                    tiluemail.setError("Please enter username");
                } else if (isValidEmailAddress(logmail) != true) {
                    tiluemail.setError("Invalid Email");
                } else if (logpass.isEmpty()) {
                    tilupass.setError("Please enter password");
                } else {
                    tilupass.setErrorEnabled(false);
                    ComplexPreferences userRegisPref = ComplexPreferences.getComplexPreferences(LoginActivity.this, Constant.REGIS_PREFRENCE, MODE_PRIVATE);

                    RegisterBean registerBean = userRegisPref.getObject(Constant.REGISTER_PREFRENCE_OBJ, RegisterBean.class);

                    if (registerBean != null) {
                        if (logmail.equalsIgnoreCase(registerBean.getUemail()) && logpass.equalsIgnoreCase(registerBean.getUpass())) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Welcome " + registerBean.getUname(), Toast.LENGTH_SHORT).show();
                            LoginActivity.this.finish();
                        }
                        /*Toast.makeText(LoginActivity.this, "Invalid Login ID or Password", Toast.LENGTH_SHORT).show();*/
                    } else {
                        Toast.makeText(LoginActivity.this, "Not Registered user please first register", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        activityLoginBinding.tvDontHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 101);
            }
        });
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    class LoginTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... strings) {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("method", strings[0]));
            nameValuePairs.add(new BasicNameValuePair("email", strings[1]));
            nameValuePairs.add(new BasicNameValuePair("password", strings[2]));

            JSONObject mJsonObject = new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.REGISTER_URL, nameValuePairs, HttpUrlConnectionJSONParser.Http.POST);
            return mJsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            if (jsonObject != null) {
                try {
                    String status = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("true")) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                        RegisterBean registerBean = new Gson().fromJson(jsonObject1.toString(), RegisterBean.class);

                        ComplexPreferences loginUserDetailPref = ComplexPreferences.getComplexPreferences(LoginActivity.this, Constant.REGIS_PREFRENCE, MODE_PRIVATE);
                        loginUserDetailPref.putObject(Constant.REGISTER_PREFRENCE_OBJ, registerBean);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(LoginActivity.this, "null", Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(jsonObject);
        }
    }

    class GetContactsTask extends AsyncTask<Void, Void, ArrayList<EmergencyBean>> {
        @Override
        protected ArrayList<EmergencyBean> doInBackground(Void... voids) {

            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (phones.moveToNext()) {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).trim();
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).trim();

                if (name != null && phoneNumber != null) {
                    EmergencyBean emergencyBean = new EmergencyBean(name, phoneNumber);
                    emergencyBeenlist.add(emergencyBean);
                }

            }
            phones.close();
            return emergencyBeenlist;
        }

        @Override
        protected void onPostExecute(ArrayList<EmergencyBean> emergencyBeen) {
            super.onPostExecute(emergencyBeen);
            if (emergencyBeen != null && !emergencyBeen.isEmpty()) {
                ComplexPreferences preferencesEmergencyAllConPref = ComplexPreferences.getComplexPreferences(LoginActivity.this, Constant.ALLCONTACTS_PREF, MODE_PRIVATE);
                preferencesEmergencyAllConPref.putObject(Constant.ALLCONTACTS_PREF_OBJ, emergencyBeenlist);
                preferencesEmergencyAllConPref.commit();
            } else {
                Log.i("EmergencyList", "No Contacts fetched");
            }
        }
    }

    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) !=
                    PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]{android.Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                new GetContactsTask().execute();
            }
        } else {
            new GetContactsTask().execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new GetContactsTask().execute();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "No Permissions ", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
