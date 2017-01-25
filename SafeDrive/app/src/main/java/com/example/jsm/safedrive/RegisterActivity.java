package com.example.jsm.safedrive;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.example.jsm.safedrive.bean.RegisterBean;
import com.example.jsm.safedrive.databinding.ActivityRegisterBinding;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding activityRegisterBinding;
    ArrayList<RegisterBean>registerBeenList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityRegisterBinding = DataBindingUtil.setContentView(RegisterActivity.this, R.layout.activity_register);

        registerBeenList = new ArrayList<>();

        activityRegisterBinding.tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uname = activityRegisterBinding.edUname.getText().toString().trim();
                String uemail = activityRegisterBinding.edEmail.getText().toString().trim();
                String upass = activityRegisterBinding.edPass.getText().toString().trim();
                String umobile = activityRegisterBinding.edMobile.getText().toString().trim();

                TextInputLayout tiluname = activityRegisterBinding.tilRegUname;
                TextInputLayout tilemail = activityRegisterBinding.tilRegEmail;
                TextInputLayout tilupass = activityRegisterBinding.tilRegPass;
                TextInputLayout tilumobile = activityRegisterBinding.tilRegMobile;

                if (uname.isEmpty()) {
                    tiluname.setErrorEnabled(true);
                    tiluname.setError("PLease enter name");

                } else if (isValidName(uname) != true) {
                    tiluname.setError(null);
                    tiluname.setErrorEnabled(false);

                    tiluname.setError("Invalid name");

                } else if (uemail.isEmpty()) {
                    tiluname.setError(null);
                    tiluname.setErrorEnabled(false);

                    tilemail.setErrorEnabled(true);
                    tilemail.setError("Please enter email Id");

                    //activityRegisterBinding.tilRegEmail.setError("Please enter email Id");
                } else if (isValidEmailAddress(uemail) != true) {
                    tilemail.setError(null);
                    tilemail.setErrorEnabled(false);

                    tilemail.setError("Invalid email");
                    //activityRegisterBinding.tilRegEmail.setError("Invalid email Id ");

                } else if (upass.isEmpty()) {
                    tilupass.setError("Please enter Password");
                    //activityRegisterBinding.tilRegEmail.setErrorEnabled(false);
                    //activityRegisterBinding.tilRegPass.setError("Please enter Password");
                } else if (upass.length() < 4) {
                    tilupass.setError("Password should be min 4 digit long");
                    //activityRegisterBinding.tilRegPass.setError("Password should be min 8 digit long");

                } else if (umobile.isEmpty()) {
                    tilumobile.setError("Please enter mobile number");
                    //activityRegisterBinding.tilRegPass.setErrorEnabled(false);
                    //activityRegisterBinding.tilRegMobile.setError("Please enter mobile number");

                } else if (isValidMobileNo(umobile) != true) {
                    tilumobile.setError("Invalid");
                    //activityRegisterBinding.tilRegMobile.setError("Please enter valid mobile number");
                } else {
                    //activityRegisterBinding.tilRegMobile.setErrorEnabled(false);

                    ComplexPreferences mUserRegistrationInfo = ComplexPreferences.getComplexPreferences(RegisterActivity.this, Constant.REGIS_PREFRENCE, MODE_PRIVATE);
                    RegisterBean registerBean = new RegisterBean(uemail, "" + System.currentTimeMillis(), umobile, uname, upass);
                    mUserRegistrationInfo.putObject(Constant.REGISTER_PREFRENCE_OBJ, registerBean);
                    mUserRegistrationInfo.commit();
                    Toast.makeText(RegisterActivity.this, "User Register Successfully", Toast.LENGTH_SHORT).show();
                    RegisterActivity.this.finish();
                    //new UserRegisterTask().execute("userregistration",uname,uemail,upass,umobile);
                }
            }
        });
    }

    public boolean isValidName(String st) {
        if (st.matches("[a-zA-Z]*")) {
            return true;
        } else
            return false;
    }

    public boolean isValidMobileNo(String stN) {
        if (stN.matches("^\\d{10}$")) {
            return true;
        } else
            return false;
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    class UserRegisterTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... strings) {

            /*JSONObject registerjsonObject = null;*/
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("method", strings[0]));
            nameValuePairs.add(new BasicNameValuePair("name", strings[1]));
            nameValuePairs.add(new BasicNameValuePair("email", strings[2]));
            nameValuePairs.add(new BasicNameValuePair("password", strings[3]));
            nameValuePairs.add(new BasicNameValuePair("mobile", strings[4]));

            JSONObject mJsonObject = new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.REGISTER_URL, nameValuePairs, HttpUrlConnectionJSONParser.Http.POST);

           /* registerjsonObject = getUserStatusDummyData(strings[0],strings[1],strings[2],strings[3]);*/

            return mJsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            if (jsonObject != null) {
                try {
                    String status = jsonObject.getString("success");
                    if (status.equalsIgnoreCase("true")) {
                        String message = jsonObject.getString("message");
                        if (message.equalsIgnoreCase("User Registered successfully")) {
                            JSONObject mUserObj = jsonObject.getJSONObject("data");

                            RegisterBean registerBean = new Gson().fromJson(mUserObj.toString(), RegisterBean.class);

                            ComplexPreferences registerPref = ComplexPreferences.getComplexPreferences(RegisterActivity.this, Constant.REGIS_PREFRENCE, MODE_PRIVATE);
                            registerPref.putObject(Constant.REGISTER_PREFRENCE_OBJ, registerBean);
                            registerPref.commit();

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(jsonObject);
        }
    }

   /* private JSONObject getUserStatusDummyData(String userName, String userEmail, String userPass, String userMobile) {

        //        TODO: Response of getUserInfo should be like the sample given below
        //{"errorCode":"","message":"","response":{"userId":"1","userDOB":"11/09/1988","userEmail":"jsmandroiddeveloper@gmail.com","userImage":"http://www.hit4hit.org/img/login/user-icon-6.png","userMobileNo":"1231231231","userName":"Amit"},"status":"Suscess"}*//**//*

        JSONObject jsonObjectFromUrl = null;

        RegisterBean registerBean = new RegisterBean(userEmail,"1",userMobile,userName,userPass);

        ResponceBean mResponceBean = new ResponceBean("Success", registerBean, "", "");

        String response = new Gson().toJson(mResponceBean);
        try {
            jsonObjectFromUrl = new JSONObject(response);
            Log.d("RMS Demo", jsonObjectFromUrl.toString());
        } catch (Throwable t) {
            Log.e("RMS Demo", "Could not parse malformed JSON: \"" + jsonObjectFromUrl + "\"");
        }
        return jsonObjectFromUrl;
    }
*/
}
