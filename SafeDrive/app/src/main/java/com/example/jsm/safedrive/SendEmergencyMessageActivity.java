package com.example.jsm.safedrive;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.Amitlibs.utils.ComplexPreferences;
import com.example.jsm.safedrive.adapters.HomeEmergencyAdapter;
import com.example.jsm.safedrive.bean.EmergencyBean;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SendEmergencyMessageActivity extends AppCompatActivity {

    public static final int REQUESTCODE_FOR_EMERLIST = 1;
    public static final int REQUESTCODE_FOR_EMER_MSG = 2;


    ListView lvEmergencyContact;
    TextView tvEmergencyMsg;

    String emergencyMessage = null;

    HomeEmergencyAdapter homeEmergencyAdapter;
    ArrayList<EmergencyBean> emergencyBeenlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_call);

        lvEmergencyContact = (ListView) findViewById(R.id.lv_emergency_call);
        tvEmergencyMsg = (TextView) findViewById(R.id.tv_emergency_message);

        emergencyBeenlist = new ArrayList<>();

        ComplexPreferences preferencesStringMsg = ComplexPreferences.getComplexPreferences(SendEmergencyMessageActivity.this, Constant.EMERGENCY_MESSAGE_PREF, MODE_PRIVATE);
        if (preferencesStringMsg != null) {
            emergencyMessage = preferencesStringMsg.getObject(Constant.EMERGENCY_MESSAGE_PREF_OBJ, String.class);
            if (emergencyMessage != null) {
                tvEmergencyMsg.setText(emergencyMessage);
            }
        }


        ComplexPreferences preferencesEmergency = ComplexPreferences.getComplexPreferences(SendEmergencyMessageActivity.this, Constant.EMERGENCY_CONTACT_PREF, MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<EmergencyBean>>() {
        }.getType();

        if (preferencesEmergency != null) {
            ArrayList<EmergencyBean> beenlist = preferencesEmergency.getArray(Constant.EMERGENCY_CONTACT_PREF_OBJ, type);

            if (beenlist != null && !beenlist.isEmpty()) {
                emergencyBeenlist = beenlist;
            }
        }

        homeEmergencyAdapter = new HomeEmergencyAdapter(SendEmergencyMessageActivity.this, emergencyBeenlist);
        lvEmergencyContact.setAdapter(homeEmergencyAdapter);
        homeEmergencyAdapter.notifyDataSetChanged();

    }

}
