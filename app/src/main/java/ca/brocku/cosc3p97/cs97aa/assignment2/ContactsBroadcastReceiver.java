package ca.brocku.cosc3p97.cs97aa.assignment2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ContactsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("BroadcastReceiver", "broadcast received for intent type [" + intent.getType() +  "]");
    }
}
