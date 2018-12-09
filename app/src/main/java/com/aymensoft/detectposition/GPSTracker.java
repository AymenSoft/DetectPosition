package com.aymensoft.detectposition;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * get user position lat long
 * @author Aymen Masmoudi[17.10.2018]last update[07.12.2018]
 */
@SuppressWarnings("PMD")
@SuppressLint("MissingPermission")
public class GPSTracker implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private LocationManager locationManager;

    private final Context mContext;

    // flag for GPS status
    private boolean canGetLocation = true;

    private Location location, networkLocation, gpsLocation, googleApiLocation; // location
    double latitude; // latitude
    double longitude; // longitude
    String provider;
    private float accuracy, gpsTrackerAccuracy;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 0 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000; // 1 minute

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(mContext).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    googleApiLocation=location;
                }
            }
        });

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(true);
        criteria.setSpeedRequired(true);
        criteria.setCostAllowed(true);
        criteria.setBearingRequired(true);
        //API level 9 and up
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);


        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // getting network status
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            this.canGetLocation=false;
        }else {
            locationManager.requestLocationUpdates(
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,criteria, this,null);
            gpsLocation  = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            networkLocation  = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (googleApiLocation != null) {
            Log.e("location","googleapi"+googleApiLocation.getAccuracy());
            location=googleApiLocation;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            accuracy = location.getAccuracy();
            provider = location.getProvider();
            gpsTrackerAccuracy = location.getAccuracy();
        }
        if (networkLocation != null) {
            Log.e("location","network"+networkLocation.getAccuracy());
            location=networkLocation;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            accuracy = location.getAccuracy();
            provider = location.getProvider();
            gpsTrackerAccuracy = location.getAccuracy();
        }else if (gpsLocation != null) {
            Log.e("location","gps"+gpsLocation.getAccuracy());
            location=gpsLocation;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            accuracy = location.getAccuracy();
            provider = location.getProvider();
            gpsTrackerAccuracy = location.getAccuracy();
        }


        //stop using gps and wait for the next call
        stopUsingGPS();

        return location;
    }

    /*
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    private void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    public float getAccuracy(){
        if (location != null) {
            accuracy = location.getAccuracy();
        }
        return accuracy;
    }

    public String getProvider() {
        if (location != null) {
            provider = location.getProvider();
        }
        return provider;
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        accuracy = location.getAccuracy();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
