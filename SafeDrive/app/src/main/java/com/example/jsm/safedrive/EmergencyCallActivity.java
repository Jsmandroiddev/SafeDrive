package com.example.jsm.safedrive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.Amitlibs.utils.ComplexPreferences;
import com.example.jsm.safedrive.adapters.HomeEmergencyAdapter;
import com.example.jsm.safedrive.bean.EmergencyBean;
import com.example.jsm.safedrive.bean.EmergencyBeanListClass;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.jsm.safedrive.EditEmergencyCallActivity.RESULTCODE_FOR_EMERLIST;

public class EmergencyCallActivity extends AppCompatActivity {

    public static final int REQUESTCODE_FOR_EMERLIST = 1;
    public static final int REQUESTCODE_FOR_EMER_MSG = 2;


    ImageView imgEditIcon;
    ListView lvEmergencyContact;
    TextView tvEmergencyMsg;

    String emergencyMessage = null;

    HomeEmergencyAdapter homeEmergencyAdapter;
    ArrayList<EmergencyBean> emergencyBeenlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_call);

        imgEditIcon = (ImageView) findViewById(R.id.img_emer_edit_icon);
        lvEmergencyContact = (ListView) findViewById(R.id.lv_emergency_call);
        tvEmergencyMsg = (TextView) findViewById(R.id.tv_emergency_message);

        emergencyBeenlist = new ArrayList<>();

        ComplexPreferences preferencesStringMsg = ComplexPreferences.getComplexPreferences(EmergencyCallActivity.this, Constant.EMERGENCY_MESSAGE_PREF, MODE_PRIVATE);
        if (preferencesStringMsg != null) {
            emergencyMessage = preferencesStringMsg.getObject(Constant.EMERGENCY_MESSAGE_PREF_OBJ, String.class);
            if (emergencyMessage != null) {
                tvEmergencyMsg.setText(emergencyMessage);
            }
        }


        ComplexPreferences preferencesEmergency = ComplexPreferences.getComplexPreferences(EmergencyCallActivity.this, Constant.EMERGENCY_CONTACT_PREF, MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<EmergencyBean>>() {
        }.getType();

        if (preferencesEmergency != null) {
            ArrayList<EmergencyBean> beenlist = preferencesEmergency.getArray(Constant.EMERGENCY_CONTACT_PREF_OBJ, type);

            if (beenlist != null && !beenlist.isEmpty()) {
                emergencyBeenlist = beenlist;
            }
        }

        homeEmergencyAdapter = new HomeEmergencyAdapter(EmergencyCallActivity.this, emergencyBeenlist);
        lvEmergencyContact.setAdapter(homeEmergencyAdapter);
        homeEmergencyAdapter.notifyDataSetChanged();


        imgEditIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emeMsg = null;
                ArrayList<EmergencyBean>mlist = new ArrayList<EmergencyBean>();

                Intent intent = new Intent(EmergencyCallActivity.this, EditEmergencyCallActivity.class);

                if (emergencyMessage != null) {
                    emeMsg = emergencyMessage;
                }
                if (!emergencyBeenlist.isEmpty() && emergencyBeenlist != null) {
                    mlist = emergencyBeenlist;
                }
                EmergencyBeanListClass emergencyBeanListClass = new EmergencyBeanListClass(mlist,emeMsg);
                intent.putExtra("emersavedata",emergencyBeanListClass);
                startActivityForResult(intent, REQUESTCODE_FOR_EMERLIST);
            }
        });

        tvEmergencyMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULTCODE_FOR_EMERLIST) {

            EmergencyBeanListClass emergencyBeanListClass = (EmergencyBeanListClass) data.getSerializableExtra("return");
            if (emergencyBeanListClass!=null)
            {
                emergencyBeenlist = emergencyBeanListClass.getArrayListEmer();
                emergencyMessage = emergencyBeanListClass.getEmerMsg();
            }

            if (emergencyBeenlist != null && !emergencyBeenlist.isEmpty()) {
                homeEmergencyAdapter = new HomeEmergencyAdapter(EmergencyCallActivity.this, emergencyBeenlist);
                lvEmergencyContact.setAdapter(homeEmergencyAdapter);
                homeEmergencyAdapter.notifyDataSetChanged();
            }
            if (emergencyMessage!=null)
            {
                tvEmergencyMsg.setText(emergencyMessage);
            }
        }
    }
}
