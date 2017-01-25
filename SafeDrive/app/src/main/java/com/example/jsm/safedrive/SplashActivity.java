package com.example.jsm.safedrive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.Amitlibs.utils.ComplexPreferences;
import com.example.jsm.safedrive.bean.AccidentZoneBean;
import com.example.jsm.safedrive.bean.NearByBean;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    ArrayList<AccidentZoneBean>accidentZoneBeenlist;
    ArrayList<NearByBean>nearByBeenlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        accidentZoneBeenlist = new ArrayList<>();



        AccidentZoneBean accidentZoneBean1 = new AccidentZoneBean("Nyay Nagar, Sukhlia, Indore, Madhya Pradesh 452010","6 accidents recorded in last 3 months",22.759334,75.879901);
        AccidentZoneBean accidentZoneBean2 = new AccidentZoneBean("Palasia Square, Manorama Ganj, Indore, Madhya Pradesh","6 accidents recorded in last 3 months",22.723138,75.885999);
        AccidentZoneBean accidentZoneBean3 = new AccidentZoneBean("Bhawarkuan Square, Indrapuri Colony, Indore, Madhya Pradesh","6 accidents recorded in last 3 months",22.692525,75.867599);
        AccidentZoneBean accidentZoneBean4 = new AccidentZoneBean("Agrasen Square, Navlakha, Indore, Madhya Pradesh, Indore, Madhya Pradesh","6 accidents recorded in last 3 months",22.703602,75.873409);
        AccidentZoneBean accidentZoneBean5 = new AccidentZoneBean("Regal Square, South Tukoganj, Indore, Madhya Pradesh","6 accidents recorded in last 3 months",22.719993,75.871206);
        AccidentZoneBean accidentZoneBean6 = new AccidentZoneBean("Pardesi Pura Main Road, Pardesipura, Indore, Madhya Pradesh 452011","6 accidents recorded in last 3 months",22.719993,75.871206);


        accidentZoneBeenlist.add(accidentZoneBean1);
        accidentZoneBeenlist.add(accidentZoneBean2);
        accidentZoneBeenlist.add(accidentZoneBean3);
        accidentZoneBeenlist.add(accidentZoneBean4);
        accidentZoneBeenlist.add(accidentZoneBean5);
        accidentZoneBeenlist.add(accidentZoneBean6);

        ComplexPreferences accidentZonePref = ComplexPreferences.getComplexPreferences(SplashActivity.this, Constant.ACCIDENT_ADD_LIST_PREF, MODE_PRIVATE);
        accidentZonePref.putObject(Constant.ACCIDENT_ADD_LIST_OBJ, accidentZoneBeenlist);
        accidentZonePref.commit();

        NearByBean nearByBean1 = new NearByBean(22.752584,75.897054,"Parking Place:- Plot No 4-1, Pu4 Commercial, Scheme No 54, A B Road, Scheme No 54, Indore, Madhya Pradesh 452008");
        NearByBean nearByBean2 = new NearByBean(22.728165,75.870003,"Parking Place:- Vallabh Nagar, Shivaji Nagar, Indore, Madhya Pradesh 452003");
        NearByBean nearByBean3 = new NearByBean(22.720192,75.853376,"Parking Place:- Khajuri Bazaar, Cloth Market, Indore, Madhya Pradesh 452002");
        NearByBean nearByBean4 = new NearByBean(22.702040,75.882561,"Parking Place:- Club, Residency,, Residency, Doordarshan Kendra, Navlakha, Indore, Madhya Pradesh 452001");
        NearByBean nearByBean5 = new NearByBean(22.715656,75.871021,"Parking Place:- Sardar Patel Marg, South Tukoganj, Chhoti Gwaltoli, Indore, Madhya Pradesh 452001");
        NearByBean nearByBean6 = new NearByBean(22.715656,75.871021,"Parking Place:- 66, Ghar Road, Near Sirpur Talab Main Gate, Ghar Road, Indore, Madhya Pradesh 452007");

        nearByBeenlist = new ArrayList<>();
        nearByBeenlist.add(nearByBean1);
        nearByBeenlist.add(nearByBean2);
        nearByBeenlist.add(nearByBean3);
        nearByBeenlist.add(nearByBean4);
        nearByBeenlist.add(nearByBean5);
        nearByBeenlist.add(nearByBean6);

        ComplexPreferences preferencesNearBy = ComplexPreferences.getComplexPreferences(SplashActivity.this,Constant.NEARBY_PREF,MODE_PRIVATE);
        preferencesNearBy.putObject(Constant.NEARBY_PREF_OBJ,nearByBeenlist);
        preferencesNearBy.commit();

        Thread splashThread = new Thread(){
            @Override
            public void run() {
                try {

                    sleep(2000);
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();

                }catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

            }
        };
        splashThread.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
