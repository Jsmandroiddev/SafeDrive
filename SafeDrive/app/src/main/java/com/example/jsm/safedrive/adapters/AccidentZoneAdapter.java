package com.example.jsm.safedrive.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jsm.safedrive.R;
import com.example.jsm.safedrive.bean.AccidentZoneBean;

import java.util.ArrayList;

/**
 * Created by JSM on 12/29/2016.
 */

public class AccidentZoneAdapter extends BaseAdapter {

    ArrayList<AccidentZoneBean>zoneBeenlist;
    Context context;
    LayoutInflater layoutInflater;


    public AccidentZoneAdapter(Context context, ArrayList<AccidentZoneBean> zoneBeenlist) {
        this.context = context;
        this.zoneBeenlist = zoneBeenlist;
    }

    @Override
    public int getCount() {
        return zoneBeenlist.size();
    }

    @Override
    public Object getItem(int i) {
        return zoneBeenlist.get(i);
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

        View mView = layoutInflater.inflate(R.layout.list_row_accident_zone,null);

        TextView tvAccAddress = (TextView) mView.findViewById(R.id.tv_acc_address_head);
        TextView tvAccDetails = (TextView) mView.findViewById(R.id.tv_acc_address_details);

        tvAccAddress.setText(zoneBeenlist.get(i).getAccZoneAddressHeading());
        tvAccDetails.setText(zoneBeenlist.get(i).getAccZoneDetails());

        return mView;
    }
}
