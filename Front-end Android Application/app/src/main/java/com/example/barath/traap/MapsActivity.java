package com.example.barath.traap;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.BreakIterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private GoogleMap myMap;
    static TextView latitude;
    static TextView longitude;
   static public double lat;
     static public double lang;

    Button bus;
    Button route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        CheckUserPermsions();

       //  latitude = (TextView) findViewById(R.id.lat);
       //  longitude = (TextView) findViewById(R.id.longi);
         bus = (Button) findViewById(R.id.button);



        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                fetchData process = new fetchData();
                process.execute();

                // Add a marker in Sydney and move the camera
                LatLng current = new LatLng(lat,lang);
                mMap.addMarker(new MarkerOptions().position(current).title("Live Bus Location"));
               // mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
            }
        });




    }

    void CheckUserPermsions(){
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return ;
            }
        }

        runlisner();
    }

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    runlisner();
                } else {

                    Toast.makeText( this,"cannot access " , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    void runlisner(){
        locationlisner myloc=new locationlisner();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,3, 1000, myloc);

        mythread myth = new mythread();
        myth.start();

    }

    class mythread extends  Thread{
        public void  run(){


            while(true){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(locationlisner.location != null) {
                            LatLng present = new LatLng(locationlisner.location.getLatitude(), locationlisner.location.getLongitude());

                            mMap.addMarker(new MarkerOptions().position(present).title("My Location"));
                         //   mMap.moveCamera(CameraUpdateFactory.newLatLng(present));
                        }
                    }
                });


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }






    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        }
    }

