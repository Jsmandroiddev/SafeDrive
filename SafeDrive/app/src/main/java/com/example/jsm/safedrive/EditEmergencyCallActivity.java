package com.example.jsm.safedrive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.Amitlibs.utils.ComplexPreferences;
import com.example.jsm.safedrive.adapters.EditRecyclerEmerCallAdapter;
import com.example.jsm.safedrive.adapters.EmergencyCallAdapter;
import com.example.jsm.safedrive.bean.EmergencyBean;
import com.example.jsm.safedrive.bean.EmergencyBeanListClass;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class EditEmergencyCallActivity extends AppCompatActivity implements EditRecyclerEmerCallAdapter.addContact {

    public static final int RESULTCODE_FOR_EMERLIST = 1;
    public static final int RESULTCODE_FOR_EMER_MSG = 2;

    RecyclerView recyclerViewEditEmerCon;
    ListView lvEditEmerCon;
    Button btnEmeSave, btnEmeCancel;
    EditText edEmergencyMessage;

    ArrayList<EmergencyBean> emergencyBeenlist;
    ArrayList<EmergencyBean> addListViewContactlist;

    EditRecyclerEmerCallAdapter recyclerEmerCallAdapter;
    EmergencyCallAdapter emergencyCallAdapter;

    String emergencyMessage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_emergency_call);


        recyclerViewEditEmerCon = (RecyclerView) findViewById(R.id.rv_edit_emergency_all_contacts);
        lvEditEmerCon = (ListView) findViewById(R.id.lv_edit_emergency_call);
        btnEmeCancel = (Button) findViewById(R.id.btn_edit_emer_con_cancel);
        btnEmeSave = (Button) findViewById(R.id.btn_edit_emer_con_save);
        edEmergencyMessage = (EditText) findViewById(R.id.ed_emergency_message);

        emergencyBeenlist = new ArrayList<>();
        addListViewContactlist = new ArrayList<>();

        lvEditEmerCon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(EditEmergencyCallActivity.this, "Item Click Listner worked" + i, Toast.LENGTH_SHORT).show();
                addListViewContactlist.remove(i);
                emergencyCallAdapter = new EmergencyCallAdapter(EditEmergencyCallActivity.this, addListViewContactlist);
                lvEditEmerCon.setAdapter(emergencyCallAdapter);
            }
        });

        ComplexPreferences preferencesEmerAllContacts = ComplexPreferences.getComplexPreferences(EditEmergencyCallActivity.this, Constant.ALLCONTACTS_PREF, MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<EmergencyBean>>() {
        }.getType();

        if (preferencesEmerAllContacts != null) {
            ArrayList<EmergencyBean> beenList = preferencesEmerAllContacts.getArray(Constant.ALLCONTACTS_PREF_OBJ, type);

            if (beenList != null && !beenList.isEmpty()) {
                emergencyBeenlist = beenList;
            }
        }


        recyclerViewEditEmerCon.setHasFixedSize(true);
        recyclerViewEditEmerCon.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));

        if (emergencyBeenlist != null) {

            recyclerEmerCallAdapter = new EditRecyclerEmerCallAdapter(EditEmergencyCallActivity.this, emergencyBeenlist, this);
            recyclerViewEditEmerCon.setAdapter(recyclerEmerCallAdapter);
        }

        final Intent intent = getIntent();
        // ArrayList<EmergencyBean> existingsList = (ArrayList<EmergencyBean>) intent.getSerializableExtra("existingcontacts");
        EmergencyBeanListClass emergencyBeanListClass = (EmergencyBeanListClass) intent.getSerializableExtra("emersavedata");
        ArrayList<EmergencyBean> existingsList = emergencyBeanListClass.getArrayListEmer();
        emergencyMessage = emergencyBeanListClass.getEmerMsg();
        edEmergencyMessage.setText(emergencyMessage);

        if (existingsList != null && !existingsList.isEmpty()) {
            addListViewContactlist = existingsList;
            emergencyCallAdapter = new EmergencyCallAdapter(EditEmergencyCallActivity.this, addListViewContactlist);
            lvEditEmerCon.setAdapter(emergencyCallAdapter);
        }


        btnEmeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tempMsg = null;
                ArrayList<EmergencyBean> tempArray = new ArrayList<EmergencyBean>();

                emergencyMessage = edEmergencyMessage.getText().toString();


                if (emergencyMessage != null) {
                    tempMsg = emergencyMessage;
                    ComplexPreferences preferencesMessage = ComplexPreferences.getComplexPreferences(EditEmergencyCallActivity.this, Constant.EMERGENCY_MESSAGE_PREF, MODE_PRIVATE);
                    preferencesMessage.putObject(Constant.EMERGENCY_MESSAGE_PREF_OBJ, emergencyMessage);
                    preferencesMessage.commit();

                }

                if (addListViewContactlist != null) {
                    tempArray = addListViewContactlist;
                    ComplexPreferences preferences = ComplexPreferences.getComplexPreferences(EditEmergencyCallActivity.this, Constant.EMERGENCY_CONTACT_PREF, MODE_PRIVATE);
                    preferences.putObject(Constant.EMERGENCY_CONTACT_PREF_OBJ, addListViewContactlist);
                    preferences.commit();
                }

                EmergencyBeanListClass emergencyBeanListClass1 = new EmergencyBeanListClass(tempArray, tempMsg);
                Intent intent1 = new Intent();
                intent1.putExtra("return", emergencyBeanListClass1);
                setResult(RESULTCODE_FOR_EMERLIST, intent1);
                finish();
            }
        });

        btnEmeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditEmergencyCallActivity.this.finish();
            }
        });
    }

    @Override
    public void addnewContact(EmergencyBean emergencyBean) {
        boolean contactAlreadyAdded = false;
        for (int i = 0; i < addListViewContactlist.size(); i++) {
            if (emergencyBean.getEmeConNumber().equalsIgnoreCase(addListViewContactlist.get(i).getEmeConNumber())) {
                contactAlreadyAdded = true;
                break;
            }
        }
        if (!contactAlreadyAdded)
            addListViewContactlist.add(emergencyBean);
        else Toast.makeText(this, "Contact is already in list", Toast.LENGTH_SHORT).show();
        if (addListViewContactlist != null && !addListViewContactlist.isEmpty()) {
            emergencyCallAdapter = new EmergencyCallAdapter(EditEmergencyCallActivity.this, addListViewContactlist);

            lvEditEmerCon.setAdapter(emergencyCallAdapter);
            emergencyCallAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(EditEmergencyCallActivity.this, "ALl Contacts List is empty or null", Toast.LENGTH_SHORT).show();
        }

    }
}
