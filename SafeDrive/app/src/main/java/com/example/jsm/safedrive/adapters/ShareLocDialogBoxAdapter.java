package com.example.jsm.safedrive.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jsm.safedrive.R;
import com.example.jsm.safedrive.bean.ShareLocDialoBoxBean;

import java.util.ArrayList;

/**
 * Created by JSM on 1/9/2017.
 */

public class ShareLocDialogBoxAdapter extends RecyclerView.Adapter<ShareLocDialogBoxAdapter.MyViewHolder> {

    Context context;
    View mView;
    ArrayList<ShareLocDialoBoxBean> dialoBoxBeenlist;
    ArrayList<ShareLocDialoBoxBean> sentList = new ArrayList<>();
    getselectedUser getselectedUserListener;


   // getCount getCountlistner;

    public interface getselectedUser{
        void getUser(ArrayList<ShareLocDialoBoxBean>locDialoBoxBeen);
    }

    public ShareLocDialogBoxAdapter(ArrayList<ShareLocDialoBoxBean> dialoBoxBeenlist, getselectedUser getselectedUserListener) {
        this.dialoBoxBeenlist = dialoBoxBeenlist;
        this.getselectedUserListener = getselectedUserListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (parent != null) {
            parent.removeView(parent.getRootView());
        }

        mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shareloc_dialog, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(mView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ShareLocDialoBoxBean shareLocDialoBoxBean = dialoBoxBeenlist.get(position);


        holder.shareLoctextView.setText(shareLocDialoBoxBean.getContactName());
        holder.shareLocimageView.setImageBitmap(shareLocDialoBoxBean.getUserImage());
        holder.shareLoctextView.setText(shareLocDialoBoxBean.getContactName());


        if (shareLocDialoBoxBean.isChecked()) {
           holder.imgselect.setVisibility(View.VISIBLE);
        }
        else {
           holder.imgselect.setVisibility(View.INVISIBLE);
        }

        holder.shareLocimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!shareLocDialoBoxBean.isChecked()) {

                    shareLocDialoBoxBean.setChecked(true);
                    sentList.add(shareLocDialoBoxBean);
                    holder.imgselect.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                }
                else {
                    shareLocDialoBoxBean.setChecked(false);
                    sentList.remove(shareLocDialoBoxBean);
                    holder.imgselect.setVisibility(View.INVISIBLE);
                    notifyDataSetChanged();
                }
            }
        });

        if (sentList!=null && !sentList.isEmpty())
        {
            getselectedUserListener.getUser(sentList);
        }

    }

    @Override
    public int getItemCount() {
       // getCountlistner.getAdapterCount(getItemCount());
        return dialoBoxBeenlist.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView shareLocimageView;
        ImageView imgsharelocSelect,imgselect;
        TextView shareLoctextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            shareLocimageView = (ImageView) itemView.findViewById(R.id.img_share_loc_dialog);
            shareLoctextView = (TextView) itemView.findViewById(R.id.tv_shareloc_dialog_contact);
            imgsharelocSelect = (ImageView) itemView.findViewById(R.id.img_share_loc_dialog);
            imgselect = (ImageView) itemView.findViewById(R.id.img_share_loc_selected);


        }
    }

}
