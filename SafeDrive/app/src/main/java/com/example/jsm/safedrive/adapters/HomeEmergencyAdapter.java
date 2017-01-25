package com.example.jsm.safedrive.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jsm.safedrive.R;
import com.example.jsm.safedrive.bean.EmergencyBean;

import java.util.ArrayList;

/**
 * Created by JSM on 1/22/2017.
 */

public class HomeEmergencyAdapter extends BaseAdapter {

    Context context;
    ArrayList<EmergencyBean> emergencyBeenList;
    LayoutInflater layoutInflater;

    public HomeEmergencyAdapter(Context context, ArrayList<EmergencyBean> emergencyBeenList) {
        this.context = context;
        this.emergencyBeenList = emergencyBeenList;
    }

    @Override
    public int getCount() {
        return emergencyBeenList.size();
    }

    @Override
    public Object getItem(int i) {
        return emergencyBeenList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (layoutInflater==null)
        {
            layoutInflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View mview = layoutInflater.inflate(R.layout.list_row_lv_home_emer_call,null);

        TextView tvEmerUserName = (TextView) mview.findViewById(R.id.tv_home_emerCall_username);
        TextView tvEmerUserPhone = (TextView) mview.findViewById(R.id.tv_home_emerCall_userPhone);

        tvEmerUserName.setText(emergencyBeenList.get(i).getEmeConName());
        tvEmerUserPhone.setText(emergencyBeenList.get(i).getEmeConNumber());

        return mview;
    }
}
