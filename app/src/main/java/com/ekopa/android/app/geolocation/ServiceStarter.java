package com.ekopa.android.app.geolocation;

/**
 * Created by Almodad on 7/15/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceStarter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent("com.ekopa.android.app.geolocation.LocationService");
        i.setClass(context, LocationService.class);
        context.startService(i);
    }
}
