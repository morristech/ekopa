package com.ekopa.android.app.geolocation;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ekopa.android.app.api.ApiClient;
import com.ekopa.android.app.api.ApiInterface;
import com.ekopa.android.app.helper.PrefManager;
import com.ekopa.android.app.model.ResponseModel;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Almodad on 7/15/2016.
 */
public class LocationService extends Service implements LocationListener {
    public static final String BROADCAST_ACTION = "Customer Location";
    private static final int LOCATION_AGE = 60000; // milliseconds
//    private static final int LOCATION_TIMEOUT = 24 * 60 * 60 * 1000 * 7; // milliseconds
    private static final int LOCATION_TIMEOUT = 1000; // milliseconds
    private LocationManager mLocationManager;
    private Location mCurrentLocation;
    private Timer mTimer;
    private PrefManager prefManager;
    Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        prefManager = new PrefManager(getApplicationContext());
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mTimer = new Timer();
        init();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged(): ",
                location.getProvider() + " / " +
                        location.getLatitude() + " / " + location.getLongitude() + " / " +
                        new Date(location.getTime()).toString());

        // check location age
        long timeDelta = System.currentTimeMillis() - location.getTime();
        if (timeDelta > LOCATION_AGE) {
            Log.d("onLocationChanged(): ", "gotten location is too old");
            // gotten location is too old
            return;
        } else {
            if(location != null) {
                com.ekopa.android.app.model.Location location1 = new com.ekopa.android.app.model.Location();
                location1.setAccess_token(prefManager.getUserDetails().get(prefManager.KEY_USERTOKEN));
                location1.setLat("" + location.getLatitude());
                location1.setLon("" + location.getLongitude());
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<ResponseModel> call = apiService.updateCustomerLocation(location1);
                call.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.body() != null && response.body().getStatusCode().equals(200)) {
                            //success
                            Log.e("SUCCESS RESPONSE", response.body().getDescription());
                        } else if (response.body().getStatusCode().equals(500)) {
                            //fail
                            Log.e("SERVER FAIL RESPONSE", response.body().getDescription());
                        } else if (response.body().getStatusCode().equals(404)) {
                            //Service not available
                            Log.e("404 RESPONSE", response.body().getDescription());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        //fail
                    }
                });
            }
        }

        // return location
        mCurrentLocation = new Location(location);
        stop();
    }


    @Override
    public void onProviderDisabled(String provider) {
        Log.d("onProviderDisabled(): ", provider);
    }


    @Override
    public void onProviderEnabled(String provider) {
        Log.d("onProviderEnabled(): ", provider);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("onStatusChanged(): ", provider);
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("onStatusChanged():", " status OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("onStatusChanged(): ", "status TEMPORARILY_UNAVAILABLE");
                break;
            case LocationProvider.AVAILABLE:
                Log.d("onStatusChanged():", "status AVAILABLE");
                break;
        }
    }


    public void stop() {
        Log.d("Geolocation.stop()", "Stop");
        if (mTimer != null) mTimer.cancel();
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocationManager.removeUpdates(this);
            mLocationManager = null;
        }
    }

    private void init() {
        // get last known location
        Location lastKnownLocation = getLastKnownLocation(mLocationManager);

        // try to listen last known location
        if (lastKnownLocation != null) {
            onLocationChanged(lastKnownLocation);
        }

        if (mCurrentLocation == null) {
            // start timer to check timeout
            TimerTask task = new TimerTask() {
                public void run() {
                    if (mCurrentLocation == null) {
                        Log.d("Geolocation.timer:", "timeout");
                        stop();
                    }
                }
            };
            mTimer.schedule(task, LOCATION_TIMEOUT);

            // register location updates
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0.0f, this);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            try {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0l, 0.0f, this);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    // returns last known freshest location from network or GPS
    private Location getLastKnownLocation(LocationManager locationManager) {
        Log.d("getLastKnownLocation()", "getLastKnownLocation");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        long timeNet = 0l;
        long timeGps = 0l;

        if(locationNet!=null)
        {
            timeNet = locationNet.getTime();
        }

        if(locationGps!=null)
        {
            timeGps = locationGps.getTime();
        }

        if(timeNet>timeGps) return locationNet;
        else return locationGps;
    }
}
