package com.example.itakg.twitterclone.Activities;


import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.itakg.twitterclone.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Location extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MyLocationListener listener;
    private LocationManager manager;
    public static android.location.Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listener = new MyLocationListener();
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        MyThread thread = new MyThread();
        thread.start();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }


    class MyLocationListener implements LocationListener {


        public MyLocationListener() {
            Location.location = new android.location.Location("Start");
        }

        @Override
        public void onLocationChanged(android.location.Location location) {
            Location.location = location;

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMap.clear();
                            LatLng loc = new LatLng(Location.location.getLatitude(), Location.location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(loc).title("Your Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                        }
                    });
                    Thread.sleep(1000);
                } catch (Exception ex) {

                }
            }
        }
    }

}
