package com.example.jsm.safedrive;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.Amitlibs.utils.ComplexPreferences;
import com.example.jsm.safedrive.bean.SpeedSafetyFeatureBean;

public class SpeedSafetyActivity extends AppCompatActivity {

    EditText editTextMaxSpeed;
    Button btnSpeedSave;
    String strMaxSpeed;
    Switch aSwitch;
    ComplexPreferences mSpeedSafetyPref;
    SpeedSafetyFeatureBean mSpeedSafetyFeatureBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_safety);


        editTextMaxSpeed = (EditText) findViewById(R.id.ed_max_speed);
        btnSpeedSave = (Button) findViewById(R.id.btnSaveMaxSpeed);
        aSwitch = (Switch) findViewById(R.id.speed_safety_switch);

        mSpeedSafetyPref = ComplexPreferences.getComplexPreferences(SpeedSafetyActivity.this, Constant.SPEED_SAFETY_FEATURE_PREF, MODE_PRIVATE);
        mSpeedSafetyFeatureBean = mSpeedSafetyPref.getObject(Constant.SPEED_SAFETY_FEATURE_PREF_OBJ, SpeedSafetyFeatureBean.class);


        if (mSpeedSafetyFeatureBean != null) {
            editTextMaxSpeed.setText("" + mSpeedSafetyFeatureBean.getMaxSpeed());
            aSwitch.setChecked(mSpeedSafetyFeatureBean.isFeatureEnable());
            aSwitch.setText(mSpeedSafetyFeatureBean.isFeatureEnable() ? "ON" : "OFF");
            aSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(SpeedSafetyActivity.this, "" + aSwitch.isChecked(), Toast.LENGTH_SHORT).show();
                    if (aSwitch.isChecked()) {
                        if (mSpeedSafetyFeatureBean != null) {
                            mSpeedSafetyFeatureBean.setFeatureEnable(true);
                            mSpeedSafetyPref.putObject(Constant.SPEED_SAFETY_FEATURE_PREF_OBJ, mSpeedSafetyFeatureBean);
                            mSpeedSafetyPref.commit();
                            aSwitch.setText(mSpeedSafetyFeatureBean.isFeatureEnable() ? "ON" : "OFF");
                        } else {
                            mSpeedSafetyFeatureBean = new SpeedSafetyFeatureBean();
                            mSpeedSafetyFeatureBean.setFeatureEnable(true);
                            mSpeedSafetyPref.putObject(Constant.SPEED_SAFETY_FEATURE_PREF_OBJ, mSpeedSafetyFeatureBean);
                            mSpeedSafetyPref.commit();
                            aSwitch.setText(mSpeedSafetyFeatureBean.isFeatureEnable() ? "ON" : "OFF");
                        }

                    } else {
                        if (mSpeedSafetyFeatureBean != null) {
                            mSpeedSafetyFeatureBean.setFeatureEnable(false);
                            mSpeedSafetyPref.putObject(Constant.SPEED_SAFETY_FEATURE_PREF_OBJ, mSpeedSafetyFeatureBean);
                            mSpeedSafetyPref.commit();
                            aSwitch.setText(mSpeedSafetyFeatureBean.isFeatureEnable() ? "ON" : "OFF");
                        } else {
                            mSpeedSafetyFeatureBean = new SpeedSafetyFeatureBean();
                            mSpeedSafetyFeatureBean.setFeatureEnable(false);
                            mSpeedSafetyPref.putObject(Constant.SPEED_SAFETY_FEATURE_PREF_OBJ, mSpeedSafetyFeatureBean);
                            mSpeedSafetyPref.commit();
                            aSwitch.setText(mSpeedSafetyFeatureBean.isFeatureEnable() ? "ON" : "OFF");
                        }

                    }
                }
            });
        }
        btnSpeedSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strMaxSpeed = editTextMaxSpeed.getText().toString();
                if (mSpeedSafetyFeatureBean != null) {
                    if (strMaxSpeed.matches(".*\\d+.*")) {
                        mSpeedSafetyFeatureBean.setMaxSpeed(Integer.parseInt(strMaxSpeed));
                        mSpeedSafetyPref.putObject(Constant.SPEED_SAFETY_FEATURE_PREF_OBJ, mSpeedSafetyFeatureBean);
                        mSpeedSafetyPref.commit();
                        Toast.makeText(SpeedSafetyActivity.this, "Max speed saved successfully", Toast.LENGTH_SHORT).show();
                    } else
                        editTextMaxSpeed.setError("Enter a valid number");
                } else {
                    if (strMaxSpeed.matches(".*\\d+.*")) {
                        mSpeedSafetyFeatureBean = new SpeedSafetyFeatureBean();
                        mSpeedSafetyFeatureBean.setMaxSpeed(Integer.parseInt(strMaxSpeed));
                        mSpeedSafetyPref.putObject(Constant.SPEED_SAFETY_FEATURE_PREF_OBJ, mSpeedSafetyFeatureBean);
                        mSpeedSafetyPref.commit();
                        Toast.makeText(SpeedSafetyActivity.this, "Max speed saved successfully", Toast.LENGTH_SHORT).show();
                    } else
                        editTextMaxSpeed.setError("Enter a valid number");
                }
            }
        });
    }
}
