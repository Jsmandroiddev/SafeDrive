package com.example.jsm.safedrive.broadcastreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.jsm.safedrive.SendEmergencyMessageActivity;

/**
 * Created by Amit Gupta on 24-01-2017.
 */

public class OutgoingCallReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE_FOR_SMS = 1;


    @Override
    public void onReceive(Context context, Intent intent) {


        Bundle bundle = intent.getExtras();
        // if (intent.getAction().equals("android.provider.Telephony.SECRET_CODE")) {
        if (null == bundle)
            return;


        String phonenumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

        Log.i("OutgoingCallReceiver", phonenumber);
        Log.i("OutgoingCallReceiver", bundle.toString());

        if (phonenumber.equals("9999"))

        {
            setResultData(null);

            Intent i = new Intent(context, SendEmergencyMessageActivity.class);
            i.putExtra("extra_phone", phonenumber);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        // }
    }

}
