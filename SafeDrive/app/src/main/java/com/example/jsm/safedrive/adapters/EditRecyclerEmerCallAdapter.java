package com.example.jsm.safedrive.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jsm.safedrive.R;
import com.example.jsm.safedrive.bean.EmergencyBean;

import java.util.ArrayList;

/**
 * Created by JSM on 1/20/2017.
 */

public class EditRecyclerEmerCallAdapter extends RecyclerView.Adapter<EditRecyclerEmerCallAdapter.MyViewHolder> {

    Context context;
    ArrayList<EmergencyBean> emergencyBeenList;

    addContact addContactListner;

    public interface addContact {
        void addnewContact(EmergencyBean emergencyBean);
    }

    public EditRecyclerEmerCallAdapter(Context context, ArrayList<EmergencyBean> emergencyBeen, addContact addContactListner) {
        this.context = context;
        this.emergencyBeenList = emergencyBeen;
        this.addContactListner = addContactListner;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_edit_emer_recycler, null);
        MyViewHolder myViewHolder = new MyViewHolder(mview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final EmergencyBean emergencyBean = emergencyBeenList.get(position);

        holder.tvEditEmerUname.setText(emergencyBean.getEmeConName());
        holder.tvEditEmerUphone.setText(emergencyBean.getEmeConNumber());


        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContactListner.addnewContact(emergencyBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return emergencyBeenList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEditEmerUname;
        TextView tvEditEmerUphone;
        ImageView imageViewEditEmerAllCon;
        LinearLayout mParentLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            mParentLayout = (LinearLayout) itemView.findViewById(R.id.parentll);
            tvEditEmerUname = (TextView) itemView.findViewById(R.id.tv_edit_emer_all_con_uname);
            tvEditEmerUphone = (TextView) itemView.findViewById(R.id.tv_edit_emer_all_con_uphone);
            imageViewEditEmerAllCon = (ImageView) itemView.findViewById(R.id.img_editemr_recycler_all_con);
        }
    }
}
